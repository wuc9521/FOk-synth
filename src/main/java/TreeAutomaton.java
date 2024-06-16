import interfaces.*;
import utils.*;
import java.util.*;
import lombok.*;
import lombok.experimental.PackagePrivate;

import org.antlr.v4.runtime.tree.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class TreeAutomaton extends Automaton<Assignment, ParseTree> {


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

    @Override
    public boolean accepts(ParseTree input) {
        return this.run(input).isAccepting();
    }

    public TreeState run(ParseTree input) {
        // This method runs the automaton on the input.
        return null;
    }


    @Override
    public void addState(State<Assignment> state) {
        this.states.add((TreeState)state);
    }

    @Override
    public void removeState(State<Assignment> state) {
        this.states.remove(state);
    }

    @Override
    public boolean hasState(State<Assignment> state) {
        return this.states.contains(state);
    }

    @Override
    public Pair<State<Assignment>, Object> transition(State<Assignment> state, ParseTree input) {
        // This method transitions from one state to another.
        return null;
    }

}
