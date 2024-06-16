import interfaces.*;
import visitors.TransitionVisitor;
import java.util.*;
import lombok.*;
import utils.TupleGenerator;

import org.antlr.v4.runtime.tree.*;

@AllArgsConstructor
public class TreeAutomaton extends Automaton<Assignment, ParseTree> {

    private final TransitionVisitor transitionVisitor = new TransitionVisitor();

    @Getter
    private final Set<TreeState> states; // the set of states of the automaton

    @Getter
    private TreeState initialState; // the initial state of the automaton

    @Getter
    private TreeState currentState; // the current state of the automaton


    public TreeAutomaton(List<String> vars, Structure<?> structure) {
        this.states = new HashSet<>();

        // initialize all the states by invoking the generateTuple method
        TupleGenerator.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> {
            HashMap<String, Structure<?>.Element> kvMap = new HashMap<>();
            for (int i = 0; i < vars.size(); i++) {
                kvMap.put(vars.get(i), (Structure<?>.Element)tuple.get(i));
            }
            this.states.add(
                new TreeState(
                    new Assignment(kvMap)
                )
            );
        });
    }

    public TreeAutomaton(ParseTree input, Set<TreeState> states, TreeState initialState) {
        this.states = states;
        this.initialState = initialState;
        this.currentState = initialState;
    }

    @AllArgsConstructor
    public class TreeState implements Automaton.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        private Assignment allVarAsnmnt; // the assignment of all the variables
        public TreeState(Assignment assignment) {
            this.allVarAsnmnt = assignment;
        }

        @Override
        public boolean isAccepting() {
            return this.isAccepting;
        }
    }

    @Override
    public boolean accepts(ParseTree input) {
        return this.run(input).isAccepting();
    }

    public TreeState run(ParseTree input) {
        TreeState currentState = this.initialState;
        this.transitionVisitor.visit(input);
        return currentState;
    }

    public void addState(TreeState state) {
        this.states.add((TreeState) state);
    }

    public void removeState(State<Assignment> state) {
        this.states.remove(state);
    }

    public boolean hasState(State<Assignment> state) {
        return this.states.contains(state);
    }

    
    /**
     * Even though the transition function is defined as Q x Σ -> Q,
     * we can get e \in Σ from class memebers.
     */
    @Override
    public State<Assignment> transition(State<Assignment> state) {
        // This method transitions from one state to another.
        return null;
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
    public Automaton<Assignment, ParseTree> intersect(Automaton<Assignment, ParseTree> automaton) {
        return null;
    }
}
