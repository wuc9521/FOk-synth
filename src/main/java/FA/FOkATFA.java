package FA;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.*;
import utils.TupleGenerator;
import visitors.FOkVisitor;
import FO.Assignment;
import FO.Structure;

import java.util.stream.Collectors;

import antlr.FOkParser;
import antlr.FOkParser.FormulaContext;

/**
 * Alternating Tree Automata for the FO(k) logic
 */
@Getter
public class FOkATFA<T> extends NFA<Assignment, FormulaContext> {

    private final Set<TState> states; // the set of states of the automaton
    private TState initialState = new TState(null); // the initial state of the automaton
    private Set<TState> currentStates = ConcurrentHashMap.newKeySet(); // the current state of the automaton
    private FOkVisitor<T> visitor; // the visitor to visit the parse tree
    private Structure<T> structure; // the structure for the automaton to run on
    private Set<Transition> transitions = new HashSet<>(); // the set of transitions of the automaton
    private ExecutorService executor;

    /**
     * the constructor of the FOkTFA class
     * 
     * @param vars      a list of variables that can be used to construct the states
     * @param structure the structure for the automaton to run on
     *                  observe that both the type of visitor and type of TFA are
     *                  dependent on the type of the structure
     */
    public FOkATFA(List<String> vars, Structure<T> structure) {
        this.states = new HashSet<>();
        this.visitor = new FOkVisitor<>(structure);
        this.executor = Executors.newCachedThreadPool(); // the thread pool for the transitions
        // initialize all the states by invoking the generateTuple method
        TupleGenerator.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> { // tuple:
                                                                                                          // List<Element>
            Map<String, Structure<?>.Element> kvMap = new ConcurrentHashMap<>();
            // HashMap<String, Structure<?>.Element> kvMap = new HashMap<>();
            for (int i = 0; i < vars.size(); i++) {
                if (tuple.get(i) != null) {
                    kvMap.put(vars.get(i), (Structure<T>.Element) tuple.get(i));
                } else {// key = var, value = new Element(), by default a new element without input is
                        // undefined
                    kvMap.put(vars.get(i), structure.new Element());
                }
            }
            TState state = new TState(new Assignment(kvMap));
            if (kvMap.values().stream().allMatch(e -> e.isUndefined())) { // if all the variables are undefined, then it
                                                                          // is the initial state
                this.initialState = state;
            }
            this.states.add(state);
        });
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public class TState implements NFA.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        private Assignment assignment; // the assignment of all the variables
        private Set<Transition> transitions = new HashSet<>();

        public TState(Assignment assignment) {
            this.assignment = assignment;
        }

        @Override
        public boolean isAccepting() {
            return this.isAccepting;
        }

        /**
         * @param transition the transition to be added
         */
        public void addTransition(Transition transition) {
            transitions.add(transition);
        }

        /**
         * get the value of a variable in the assignment
         * 
         * @param var the variable to be checked
         * @return the value of the variable in the assignment
         */
        public Structure<?>.Element getValueOfVar(String var) {
            return this.assignment.getKvMap().get(var);
        }

        /**
         * check if the state is equal to another state except for a variable
         * 
         * @param state the state to be compared with
         * @param var   the variable to be excluded
         * @return whether the two states are equal except for the variable
         */
        public boolean equalsExceptVar(TState state, String var) {
            for (String key : this.assignment.getKvMap().keySet()) {
                if (key.equals(var)) { // don't forget the undefined case
                    continue;
                } else if (this.assignment.getKvMap().get(key) == null) {
                    if (state.assignment.getKvMap().get(key) != null) {
                        return false;
                    }
                } else if (!this.assignment.getKvMap().get(key).equals(state.assignment.getKvMap().get(key))) {
                    return false;
                }
            }
            return true;
        }
    }

    public void addState(TState state) {
        Map<String, Structure<?>.Element> kvMap = state.assignment.getKvMap();
        boolean isInitial = kvMap.values().stream().allMatch(e -> e.isUndefined());
        if (isInitial) {
            this.initialState = state;
        }
        this.states.add((TState) state);
    }

    public void removeState(State<Assignment> state) {
        this.states.remove(state);
    }

    public boolean hasState(State<Assignment> state) {
        return this.states.contains(state);
    }

    /**
     * Usually we might not need a separate class for the transition function, but
     * this is ALTERNATING automaton!!!
     */
    @Setter
    class Transition implements Callable<Set<State<Assignment>>> {
        private State<Assignment> state;
        private FormulaContext input;
        private FOkATFA<T> automaton;

        public Transition(State<Assignment> state, FormulaContext input) {
            this.state = state;
            this.input = input;
        }

        public Transition(FOkATFA<T> automaton, State<Assignment> state, FormulaContext input) {
            this.automaton = automaton;
            this.state = state;
            this.input = input;
        }

        @Override
        public Set<State<Assignment>> call() throws Exception {
            return automaton.transition(state, input); // Now calls the automaton's transition function
        }
    }

    /**
     * the transition function is defined as Q x Σ -> 2^Q,
     * since the alternating automaton is non-deterministic
     * 
     * @param state the current state of the automaton
     * @param input the input token
     * @return the set of states that the automaton can transit to
     *         formula
     *         1 : NOT formula
     *         2 | formula op=(IFF | IMPLIES | AND | OR) formula
     *         3 | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     *         4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *         5 | term EQUALS term
     *         6 | value
     *         7 | LPAREN formula RPAREN
     *         ;
     */
    @Override
    public Set<State<Assignment>> transition(State<Assignment> state, FormulaContext input) {
        Set<TState> nextStates = new HashSet<>();
        this.currentStates.add((TState) state); // add the current state to the set of current states
        if (input.NOT() != null) { // case 1: δ (γ, ¬) = (γ', 1)
            try {
                nextStates = this.handleNot(state, input); // mutually recursive
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else if (input.op != null) { // case 2
            try {
                nextStates = this.handleOp(state, input, input.op.getType()); // mutually recursive
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else if (input.qop != null) { // case 3
            try {
                nextStates = this.handleQuantifier(state, input, input.qop.getType()); // mutually recursive
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else if (input.RELATION() != null || input.EQUALS() != null || input.value() != null) { // case 4, 5, 6
            try {
                nextStates = this.handleRelation(state, input);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            // Iterator<TState> iterator = this.currentStates.iterator(); // 获取
            // currentStates 的迭代器
            // while (iterator.hasNext()) {
            // TState s = iterator.next(); // 获取下一个元素
            // if (this.visitor.getFormulaVal(input, s.getAssignment())) { // 检查条件是否满足
            // this.finalStates.add(s); // 如果满足条件，添加到 finalStates
            // nextStates.add(s); // 同时添加到 nextStates
            // }
            // }

        } else if (input.LPAREN() != null) { // case 7
            nextStates = this.transition(state, input.formula(0)).stream().map(s -> (TState) s)
                    .collect(Collectors.toSet());
        }
        return nextStates.stream().collect(Collectors.toSet());
    }

    /**
     * the accept function is defined as Σ* -> {0, 1}
     * 
     * @param input the input to be checked
     * @return whether the automaton accepts the input
     */
    public boolean accept(FormulaContext input) throws Exception {
        Set<Future<Set<State<Assignment>>>> futures = new HashSet<>();
        futures.add(executor.submit(new Transition(this, initialState, input)));

        for (Future<Set<State<Assignment>>> future : futures) {
            Set<State<Assignment>> result = future.get();
            for (State<Assignment> state : result) {
                if (state != null) { // don't forget the null case
                    this.currentStates.add((FOkATFA<T>.TState) state);
                }
            }
        }
        return currentStates.stream().anyMatch(State::isAccepting);
    }

    /**
     * shutdown the executor
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * determinize the automaton
     */
    @Override
    public void determinize() {

    }

    /**
     * intersect two automata
     * 
     * @param automaton
     * @return the intersection of two automata
     */
    @Override
    public NFA<Assignment, FormulaContext> intersect(NFA<Assignment, FormulaContext> automaton) {
        return null;
    }

    /**
     * handle the operation in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *                     formula
     * @param type         the type of the operation
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Set<TState> handleOp(
            State<Assignment> currentState,
            FormulaContext input,
            int type) throws InterruptedException, ExecutionException {
        assert input.op != null; // caller should ensure that the input is an operation
        final int opNum = input.formula().size();
        Set<TState> resultStates = new HashSet<>();
        assert opNum == 2; // the operation should have two operands
        switch (input.op.getType()) {
            case FOkParser.AND:
                resultStates = this.currentStates.stream().collect(Collectors.toSet());
                for (int i = 0; i < opNum; i++) {
                    final int finalI = i;
                    Set<Future<Set<TState>>> futures = new HashSet<>();
                    Iterator<TState> iterator = resultStates.iterator();
                    while (iterator.hasNext()) {
                        TState state = iterator.next();
                        futures.add(executor.submit(() -> {
                            Set<State<Assignment>> rawStates = transition(state, input.formula(finalI));
                            return rawStates.stream().map(s -> (TState) s).collect(Collectors.toSet());
                        }));
                    }
                    Set<TState> states = new HashSet<>();
                    for (Future<Set<TState>> future : futures) {
                        states.addAll(future.get());
                    }
                    resultStates = states;
                }
                return resultStates;
            case FOkParser.OR:
                Set<Future<Set<TState>>> futures = new HashSet<>();
                for (int i = 0; i < opNum; i++) {
                    final int finalI = i;
                    Iterator<TState> iterator = this.currentStates.iterator(); // 获取 currentStates 的迭代器
                    while (iterator.hasNext()) {
                        TState state = iterator.next(); // 获取下一个状态
                        Future<Set<TState>> future = executor.submit(() -> {
                            Set<State<Assignment>> rawStates = transition(state, input.formula(finalI));
                            return rawStates.stream().map(s -> (TState) s).collect(Collectors.toSet());
                        });
                        futures.add(future);
                    }
                }
                resultStates = new HashSet<>();
                for (Future<Set<TState>> future : futures) {
                    resultStates.addAll(future.get());
                    if (!resultStates.isEmpty()) { // For OR, we can short-circuit if any child returns a non-empty set
                        break;
                    }
                }
                return resultStates;
            case FOkParser.IMPLIES:
                // TODO: finish the implementation
                break;
            default:
                break;
        }
        return resultStates;
    }

    /**
     * handle the quantifier in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: qop=(FORALL | EXISTS) VARIABLE DOT
     *                     LPAREN formula RPAREN
     * @param type         the type of the quantifier
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Set<TState> handleQuantifier(State<Assignment> currentState, FormulaContext input, int type)
            throws InterruptedException, ExecutionException {
        String var = input.VARIABLE().getText();

        Set<TState> sameStates = this.states.stream().filter(
                s -> this.currentStates.stream().filter(s_ -> s_ != s).allMatch(s_ -> s_.equalsExceptVar(s, var)))
                .collect(Collectors.toSet());
        // 找到 states 中所有除了 var 之外别的变量的赋值都相同的 state
        Set<TState> resultStates = new HashSet<>();
        Set<Future<Set<TState>>> futures = new HashSet<>();
        Iterator<TState> iterator = sameStates.iterator(); // 获取 currentStates 的迭代器
        while (iterator.hasNext()) {
            TState state = iterator.next(); // 获取下一个元素
            futures.add(executor.submit(() -> {
                Set<State<Assignment>> rawStates = transition(state, input.formula(0));
                return rawStates.stream().map(s -> (TState) s).collect(Collectors.toSet());
            }));
        }
        switch (input.qop.getType()) {
            case FOkParser.FORALL:
                for (Future<Set<TState>> future : futures) {
                    resultStates.addAll(future.get());
                }
                return resultStates;
            case FOkParser.EXISTS:
                for (Future<Set<TState>> future : futures) {
                    if (!future.get().isEmpty()) {
                        break;
                    }
                } // make use of short-circuiting
                return resultStates;
            default:
                break;
        }
        return resultStates;
    }

    /**
     * handle the NOT operation in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: NOT formula
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Set<TState> handleNot(State<Assignment> currentState, FormulaContext input)
            throws InterruptedException, ExecutionException {
        Set<TState> resultStates = new HashSet<>();
        Set<TState> cpyStates = new HashSet<>(this.currentStates);
        cpyStates.removeAll(resultStates);
        Iterator<TState> iterator = cpyStates.iterator(); // 获取 currentStates 的迭代器
        Set<Future<Set<TState>>> futures = new HashSet<>();
        while (iterator.hasNext()) {
            TState state = iterator.next(); // 获取下一个元素
            futures.add(executor.submit(() -> {
                Set<State<Assignment>> rawStates = transition(state, input.formula(0));
                return rawStates.stream().map(s -> (TState) s).collect(Collectors.toSet());
            }));
        }
        for (Future<Set<TState>> future : futures) {
            resultStates.addAll(future.get());
        }
        return resultStates;
    }

    /**
     * handle the relation, equals and values in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula:
     *                     4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *                     5 | term EQUALS term
     *                     6 | value
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Set<TState> handleRelation(State<Assignment> currentState, FormulaContext input)
            throws InterruptedException, ExecutionException {
        // actually '=' is just a binary relation and 'value' is just a nullary relation
        Set<TState> resultStates = new HashSet<>();
        Iterator<TState> iterator = this.currentStates.iterator(); // 获取 currentStates 的迭代器
        Set<Future<Set<TState>>> futures = new HashSet<>();
        while (iterator.hasNext()) {
            TState state = iterator.next(); // 获取下一个元素
            futures.add(executor.submit(() -> {
                if (this.visitor.getFormulaVal(input, state.getAssignment())) {
                    state.setAccepting(true);
                    return Collections.singleton(state);
                }
                return Collections.emptySet();
            }));
        }
        for (Future<Set<TState>> future : futures) {
            resultStates.addAll(future.get());
        }
        return resultStates;
    }
}
// TODO: 关键, 在递归的过程中, transition 函数的第一个参数是接下来所有的状态的集合, 第二个参数是接下来token的集合.
// 实际上在递归的过程中就解释了转移函数.
// TODO: 一个个check一下有没有出错