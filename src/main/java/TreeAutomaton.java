import interfaces.*;
import utils.*;
import java.util.*;
import lombok.*;
import org.antlr.v4.runtime.tree.*;


public class TreeAutomaton implements Automaton<Assignment, ParseTree> {


    @AllArgsConstructor @NoArgsConstructor @Getter @Setter
    public class TreeState implements Automaton.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        private Assignment assignment; // the assignment of the state

        @Override
        public boolean isAccepting() {
            return isAccepting;
        }
    }

    private Set<TreeState> states; // the set of states of the automaton
    private TreeState initialState; // the initial state of the automaton

    public boolean accepts(ParseTree input) {
        // This method accepts or rejects a parse tree.
        return false;
    }

    public State<Assignment> getInitialState() {
        // This method returns the initial state of the automaton.
        return null;
    }

    public void setInitialState(State<Assignment> state) {
        // This method sets the initial state of the automaton.
    }

    public void addState(State<Assignment> state) {
        // This method adds a state to the automaton.
    }

    public void removeState(State<Assignment> state) {
        // This method removes a state from the automaton.
    }

    public boolean hasState(State<Assignment> state) {
        // This method checks if the automaton has a state.
        return false;
    }

    public Pair<State<Assignment>, Object> transition(State<Assignment> state, ParseTree input) {
        // This method transitions from one state to another.
        return null;
    }

}
