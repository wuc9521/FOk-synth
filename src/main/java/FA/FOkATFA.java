package FA;

import java.util.*;
import java.util.concurrent.Callable;
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
    private TState initialState; // the initial state of the automaton
    private Set<TState> currentStates; // the current state of the automaton
    private Set<TState> finalStates; // the final states of the automaton
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
        this.executor = Executors.newFixedThreadPool(8); // the thread pool for the transitions
        // initialize all the states by invoking the generateTuple method
        TupleGenerator.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> {
            HashMap<String, Structure<?>.Element> kvMap = new HashMap<>();
            for (int i = 0; i < vars.size(); i++) {
                kvMap.put(vars.get(i), (Structure<T>.Element) tuple.get(i));
            }
            this.states.add(new TState(new Assignment(kvMap)));
        });
    }

    @AllArgsConstructor
    @Getter
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
    }

    public void addState(TState state) {
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
        if (input.NOT() != null) { // case 1: δ (γ, ¬) = (γ', 1)
            // return the set of states that is not in currentStates
            nextStates = this.states.stream().filter(s -> !this.currentStates.contains(s)).collect(Collectors.toSet());
        } else if (input.op != null) { // case 2
            try {
                this.handleOp(state, input, input.op.getType());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else if (input.qop != null) { // case 3
            try {
                this.handleQuantifier(state, input, input.qop.getType());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else if (input.RELATION() != null || input.EQUALS() != null || input.value() != null) { // case 4, 5, 6
            // actually '=' is just a binary relation and 'value' is just a nullary relation
            return this.currentStates.stream()
                    .filter(s -> {
                        // I should use the holds() method of relation, but its just too long....
                        if (this.visitor.getFormulaVal(input, s.getAssignment())) { // aka if the relation holds.
                            this.finalStates.add((TState) s);
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toSet());
        } else if (input.LPAREN() != null) { // case 7
            return this.transition(state, input.formula(0)); // recursive call
        }
        return nextStates.stream().collect(Collectors.toSet());
    }

    public boolean accept(FormulaContext input) {
        return this.currentStates.stream().anyMatch(s -> s.isAccepting());
    }

    /**
     * the run method of the automaton
     * 
     * @param input the input string to be checked
     * @return whether the input string is accepted by the automaton
     */
    @Override
    public boolean run(FormulaContext input) throws Exception {
        Set<Future<Set<State<Assignment>>>> futures = new HashSet<>();
        futures.add(executor.submit(new Transition(this, initialState, input)));

        Set<State<Assignment>> currentStates = new HashSet<>();
        for (Future<Set<State<Assignment>>> future : futures) {
            currentStates.addAll(future.get());
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
     * @param state the current state of the automaton
     * @param input the input formula: formula op=(IFF | IMPLIES | AND | OR) formula
     * @param type  the type of the operation
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Set<State<Assignment>> handleOp(
            State<Assignment> state,
            FormulaContext input,
            int type) throws InterruptedException, ExecutionException {
        assert input.op != null; // caller should ensure that the input is an operation
        final int opNum = input.formula().size();
        Set<State<Assignment>> resultStates = new HashSet<>();
        assert opNum == 2; // the operation should have two operands
        switch (input.op.getType()) {
            case FOkParser.AND:
                resultStates = this.currentStates.stream().collect(Collectors.toSet());
                for (int i = 0; i < opNum; i++) {
                    final int finalI = i;
                    Set<Future<Set<State<Assignment>>>> futures = new HashSet<>();
                    for (State<Assignment> intermediateState : resultStates) {
                        // Submit each transition to the executor
                        futures.add(executor.submit(() -> transition(intermediateState, input.formula(finalI))));
                    }
                    Set<State<Assignment>> intermediateStates = new HashSet<>();
                    for (Future<Set<State<Assignment>>> future : futures) {
                        intermediateStates.addAll(future.get());
                    }
                    resultStates = intermediateStates;
                }
                return resultStates;
            case FOkParser.OR:
                Set<Future<Set<State<Assignment>>>> futures = new HashSet<>();
                for (int i = 0; i < opNum; i++) {
                    final int finalI = i;
                    futures.add(executor.submit(() -> transition(state, input.formula(finalI))));
                }
                resultStates = new HashSet<>();
                for (Future<Set<State<Assignment>>> future : futures) {
                    resultStates.addAll(future.get());
                    if (!resultStates.isEmpty()) { // For OR, we can short-circuit if any child returns a non-empty set
                        break;
                    }
                }
                return resultStates;
            default:
                break;
        }
        return resultStates;
    }


    /**
     * handle the quantifier in the formula
     * @param state the current state of the automaton
     * @param input the input formula: qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     * @param type the type of the quantifier
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Set<State<Assignment>> handleQuantifier(State<Assignment> state, FormulaContext input, int type) throws InterruptedException, ExecutionException {
        switch (input.qop.getType()) {
            case FOkParser.FORALL:
                return null;
            case FOkParser.EXISTS:
                return null;
            default:
                break;
        }
        return null;
    }
}
