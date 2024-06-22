package FA;

public abstract class DFA<SType, IType> extends FA<SType, IType> {
    
    /**
     * @param state the state to transition from
     * @param input the input to be checked
     * @return the set of states that the automaton can transit to
     */
    public abstract State<SType> transition(State<SType> state, IType input); // transition from one state to
                                                                                   // another
}
