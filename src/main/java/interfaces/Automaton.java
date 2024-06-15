package interfaces;
import utils.Pair;

/**
 * This is an interface that represents an automaton in first-order logic.
 * An automata is formally defined as a 5-tuple (Q, Σ, δ, q0, F)
 */
public interface Automaton<SType, IType> {
    
    /**
     * This is an interface that represents a state in an automaton.
     */
    public interface State<SType> {
        public boolean isAccepting(); // check if the state is accepting
    }

    /**
     * @return the initial state of the automaton
     */
    public State<SType> getInitialState(); // get the initial state

    /**
     * @param input
     * @return whether the automaton accepts the input
     */
    public boolean accepts(IType input); // accept or reject an input

    /**
     * @param state
     */
    public void setInitialState(State<SType> state); // set the initial state

    /**
     * @param state
     */
    public void addState(State<SType> state); // add a state to the automaton

    /**
     * @param state
     */
    public void removeState(State<SType> state); // remove a state from the automaton

    /**
     * @param state
     * @return
     */
    public boolean hasState(State<SType> state); // check if the automaton has a state

    // 这里第二个范型要有一个类似于 parsetree traverse pointer 的东西, 来记录当前到输入的哪一步了.
    public Pair<State<SType>, Object> transition(State<SType> state, IType input); // transition from one state to another
}