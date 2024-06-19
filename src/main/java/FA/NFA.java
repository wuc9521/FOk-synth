package FA;

import lombok.*;
import java.util.*;

/**
 * This is an interface that represents an automaton in first-order logic.
 * An automata is formally defined as a 5-tuple (Q, Σ, δ, q0, F)
 */
@Getter
@Setter
public abstract class NFA<SType, IType> {
    public interface State<SType> {
        public boolean isAccepting(); // check if the state is accepting
    }

    /**
     * @param input the input to be checked
     * @return whether the automaton accepts the input
     */
    public abstract boolean accept(IType input) throws Exception; // check if the automaton accepts the input

    /**
     * determinize the NFA to a DFA
     */
    public abstract void determinize(); // determinize the automaton

    /**
     * @param state the state to transition from
     * @param input the input to be checked
     * @return the set of states that the automaton can transit to
     */
    public abstract Set<State<SType>> transition(State<SType> state, IType input); // transition from one state to
                                                                                   // another

    /**
     * intersect two automata
     * 
     * @param automaton the automaton to be intersected with
     * @return the intersected automaton
     */
    public abstract NFA<SType, IType> intersect(NFA<SType, IType> automaton); // intersect two automata
}