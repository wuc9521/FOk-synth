package FA;

public abstract class FA<SType, IType> {
    /**
     * This is an interface that represents a state in an automaton.
     */
    public interface State<SType> {
        public boolean isAccepting(); // check if the state is accepting
    }

    /**
     * @param input the input to be checked
     * @return whether the automaton accepts the input
     */
    public abstract boolean accepts(IType input) throws Exception; // check if the automaton accepts the input
}
