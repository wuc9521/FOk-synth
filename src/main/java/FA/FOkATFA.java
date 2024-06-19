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
import utils.Data;

import java.util.stream.Collectors;

import antlr.FOkParser;
import antlr.FOkParser.FormulaContext;

/**
 * Alternating Tree Automata for the FO(k) logic
 */
@Getter
public class FOkATFA<T> extends NFA<Assignment, FormulaContext> {

    protected final Set<TState> states; // the set of states of the automaton
    protected TState initialState = new TState(null); // the initial state of the automaton
    protected volatile Set<TState> currentStates = Collections.synchronizedSet(new HashSet<>()); // the current state of the automaton
    protected FOkVisitor<T> visitor; // the visitor to visit the parse tree
    protected Structure<T> structure; // the structure for the automaton to run on
    protected Set<Transition> transitions = new HashSet<>(); // the set of transitions of the automaton
    protected ExecutorService executor;

    /**
     * the constructor of the FOkTFA class
     * @param structure the structure for the automaton to run on
     */
    public FOkATFA(Structure<T> structure){
        this(new ArrayList<>(), structure);
    }

    /**
     * the constructor of the FOkTFA class
     * 
     * @param vars      a list of variables that can be used to construct the states
     * @param structure the structure for the automaton to run on
     *                  observe that both the type of visitor and type of TFA are
     *                  dependent on the type of the structure
     */
    public FOkATFA(List<String> vars, Structure<T> structure) {
        this.structure = structure;
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
        private boolean shouldBeFalse = false; // whether the formula should be false under the assignment of the state
        private boolean isAccepting = false; // whether the state is accepting
        private Assignment assignment; // the assignment of all the variables
        private Set<Transition> transitions = new HashSet<>();

        public TState(Assignment assignment) {
            this.assignment = assignment;
        }

        public TState(Assignment assignment, boolean isAccepting) {
            this.assignment = assignment;
            this.isAccepting = isAccepting;
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
        private State<Assignment> state; // the start state of the transition
        private FormulaContext input; // the input token
        private FOkATFA<T> automaton; // the automaton that the transition belongs to

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
        ((TState) state).addTransition(new Transition(state, input));
        try {
            if (input.NOT() != null) { // case 1: δ (γ, ¬) = (γ', 1)
                ((TState) state).setShouldBeFalse(true); // the formula should be false under the assignment of the
                                                         // state
                this.currentStates = this.transition(
                        state,
                        input.formula(0)).stream().map(s -> (TState) s).collect(Collectors.toSet());
            } else if (input.op != null) { // case 2
                this.currentStates = this.handleOp(state, input); // mutually recursive
            } else if (input.qop != null) { // case 3
                this.currentStates = this.handleQuantifier(state, input); // mutually recursive
            } else if (input.formula().size() == 0) { // case 4, 5, 6
                this.currentStates = this.handleRelation(state, input);
            } else if (input.LPAREN() != null) { // case 7
                this.currentStates = this.transition(state, input.formula(0)).stream().map(s -> (TState) s)
                        .collect(Collectors.toSet());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // printCurrentStates();
        this.currentStates.forEach(s -> {
            if (s.isAccepting()) {
                System.out.println("accepting");
            }
        });
        return this.currentStates.stream().collect(Collectors.toSet());
    }

    private void printCurrentStates() {
        for (TState s : this.currentStates) {
            s.getAssignment().getKvMap().forEach((k, v) -> {
                // System.out.print(k + " -> " + v.getValue() + " ");
                if (v.getValue() != null) {
                    System.out.print(k + " -> " + v.getValue() + " ");
                } else {
                    System.out.print(k + " -> " + "null ");
                }
            });
            System.out.println();
        }
    }

    /**
     * the accept function is defined as Σ* -> {0, 1}
     * 
     * @param visitor the visitor to visit the parse tree
     * @return whether the automaton accepts the input
     * @throws Exception if the visitor fails to visit the parse tree
     */
    public boolean accepts(FOkVisitor<T> visitor) throws Exception {
        // make sure that the visitor has visited the parse tree
        this.visitor = visitor;
        return this.accepts(visitor.getRootFormula());
    }

    /**
     * the accept function is defined as Σ* -> {0, 1}
     * 
     * @param input the input to be checked
     * @return whether the automaton accepts the input
     * @throws Exception if the visitor fails to visit the parse tree
     */
    @Override
    public boolean accepts(FormulaContext input) throws Exception {
        this.currentStates.clear(); // accept() is like a run() of the automaton
        Set<Future<Set<State<Assignment>>>> futures = new HashSet<>();
        this.currentStates.add(this.initialState);
        futures.add(this.executor.submit(new Transition(this, initialState, input)));

        for (Future<Set<State<Assignment>>> future : futures) {
            Set<State<Assignment>> result = future.get();
            for (State<Assignment> state : result) {
                if (state != null) { // don't forget the null case
                    try {
                        this.currentStates.add((FOkATFA<T>.TState) state);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return currentStates.stream().anyMatch(State::isAccepting);
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
    @SuppressWarnings("unchecked")
    public NFA<Assignment, FormulaContext> intersect(NFA<Assignment, FormulaContext> other) {
        assert other instanceof FOkATFA; // assume that the other automaton is also an FOkATFA
        FOkATFA<T> otherAutomaton = (FOkATFA<T>) other;
        FOkATFA<T> result = new FOkATFA<>(new ArrayList<>(), this.structure); // Assume same structure for simplicity

        // 1. Compute the Cartesian product of states
        // TODO: 这里还没看
        for (TState state1 : this.states) {
            for (TState state2 : otherAutomaton.states) {
                Map<String, Structure<?>.Element> kvMap = new ConcurrentHashMap<>();
                kvMap.putAll(state1.getAssignment().getKvMap());
                kvMap.putAll(state2.getAssignment().getKvMap()); // Combine assignments
                TState newState = result.new TState(new Assignment(kvMap));
                newState.setAccepting(state1.isAccepting() && state2.isAccepting()); // Only accepting if both are
                result.addState(newState);
            }
        }

        // 2. Define transitions for the combined states
        for (TState newState : result.states) {
            // Decompose the new state into its components
            Assignment ass1 = ((TState) newState).getAssignment(); // Simplification for demo
            Assignment ass2 = ((TState) newState).getAssignment(); // Simplification for demo

            for (Transition trans1 : this.transitions) {
                for (Transition trans2 : otherAutomaton.transitions) {
                    // Assuming that we can find a way to synchronize or unify the inputs if
                    // necessary

                    // Transition newTransition = result.new Transition(newState,
                    // trans1.getInput());
                    // newTransition.setAutomaton(result);
                    // ((TState) newState).addTransition(newTransition);
                }
            }
        }

        return result;
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
    protected Set<TState> handleOp(
            State<Assignment> currentState,
            FormulaContext input) throws InterruptedException, ExecutionException {
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
                        futures.add(this.executor.submit(() -> {
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
                        Future<Set<TState>> future = this.executor.submit(() -> {
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
     * @param input        the input formula:
     *                     qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     * @param type         the type of the quantifier
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    protected Set<TState> handleQuantifier(State<Assignment> currentState, FormulaContext input)
            throws InterruptedException, ExecutionException {
        String var = input.VARIABLE().getText();

        Set<TState> sameStates = this.states.stream().filter(
                s -> this.currentStates.stream().allMatch(s_ -> s_.equalsExceptVar(s, var)))
                .collect(Collectors.toSet());
        // find all the states that are the same as the current states except for the
        // given variable
        Set<TState> resultStates = new HashSet<>();
        Set<Future<Set<TState>>> futures = new HashSet<>();
        Iterator<TState> iterator = sameStates.iterator(); // get the iterator of the sameStates
        while (iterator.hasNext()) {
            TState state = iterator.next(); // get the next state
            futures.add(this.executor.submit(() -> {
                Set<State<Assignment>> rawStates = transition(state, input.formula(0));
                return rawStates.stream().map(s -> (TState) s).collect(Collectors.toSet());
            }));
        }
        switch (input.qop.getType()) {
            case FOkParser.FORALL:
                for (Future<Set<TState>> future : futures) {
                    resultStates.addAll(future.get());
                }
            case FOkParser.EXISTS: // make use of short-circuiting
                for (Future<Set<TState>> future : futures) {
                    if (!future.get().isEmpty()) {
                        resultStates = future.get();
                        break;
                    }
                }
            default:
                break;
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
     */
    protected Set<TState> handleRelation(State<Assignment> currentState, FormulaContext input) {
        // actually '=' is just a binary relation and 'value' is just a nullary relation
        boolean isTrueUnderAssignment = false;
        TState state = ((TState) currentState);
        Assignment assignment = state.getAssignment();
        if (input.RELATION() != null) {
            String key = input.RELATION().getText();
            if (this.structure.relations.containsKey(key)) {
                isTrueUnderAssignment = this.structure.relations.get(key).holds(
                        input.term().stream()
                                .map(t -> this.structure.new Element(this.visitor.getTermVal(t, assignment)))
                                .collect(Collectors.toList()));
            }
        } else if (input.EQUALS() != null) {
            isTrueUnderAssignment = this.structure.new Element(
                    this.visitor.getTermVal(input.term(0), assignment))
                    .equals(this.structure.new Element(this.visitor.getTermVal(input.term(1), assignment)));
        } else if (input.value() != null) {
            isTrueUnderAssignment = input.value().getText().equals(Data.TRUE);
        }

        // if the formula is what it should be under the assignment of the state,
        if (isTrueUnderAssignment == !state.shouldBeFalse) {
            state.setAccepting(true);
            return Collections.singleton(state);
        }
        return Collections.emptySet();
    }

    /**
     * @return the shortest formula that the automaton accepts
     */
    public String shortestFormula() {
        // use BFS to find the shortest formula
        Queue<TState> queue = new LinkedList<>();
        queue.add(this.initialState);
        while (!queue.isEmpty()) {
            TState state = queue.poll();
            if (state.isAccepting()) {
                return state.getAssignment().toString();
            }
            for (Transition transition : state.getTransitions()) {
                // queue.add((TState) transition.getState());
            }
        }
        return null;
    }

    public FOkATFA<T> copy() {
        FOkATFA<T> copy = new FOkATFA<>(this.structure);
        copy.states.addAll(this.states);
        copy.initialState = this.initialState;
        copy.currentStates.addAll(this.currentStates);
        copy.visitor = this.visitor;
        copy.structure = this.structure;
        copy.transitions.addAll(this.transitions);
        return copy;
    }
}