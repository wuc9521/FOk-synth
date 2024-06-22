package FA;

import java.util.Set;

import lombok.*;

/**
 * This is an interface that represents an automaton in first-order logic.
 * An automata is formally defined as a 5-tuple (Q, Σ, δ, q0, F)
 */
@Getter
@Setter
public abstract class NFA<SType, IType> extends FA<SType, IType> {
    /**
     * determinize the NFA to a DFA
     */
    public abstract DFA<SType, IType> determinize(); // determinize the automaton

    /**
     * @param state the state to transition from
     * @param input the input to be checked
     * @return the set of states that the automaton can transit to
     */
    public abstract Set<State<SType>> transition(State<SType> state, IType input); // transition from one state to
                                                                                   // another
}