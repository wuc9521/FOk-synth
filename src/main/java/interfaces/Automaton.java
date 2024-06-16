package interfaces;
import lombok.*;
import utils.Pair;

/**
 * This is an interface that represents an automaton in first-order logic.
 * An automata is formally defined as a 5-tuple (Q, Σ, δ, q0, F)
 */
@Getter @Setter
public abstract class Automaton<SType, IType> {
    
    public interface State<SType> {
        public boolean isAccepting(); // check if the state is accepting
    }

    /**
     * @param input
     * @return whether the automaton accepts the input
     */
    public abstract boolean accepts(IType input); // accept or reject an input


    /**
     * @param state
     */
    public abstract void addState(State<SType> state); // add a state to the automaton

    /**
     * remove a state from the automaton
     * @param state
     */
    public abstract void removeState(State<SType> state); 


    /**
     * @param state
     * @return whether the automaton has the state
     */
    public abstract boolean hasState(State<SType> state); // check if the automaton has a state

    // 这里第二个范型要有一个类似于 parsetree traverse pointer 的东西, 来记录当前到输入的哪一步了.
    public abstract Pair<State<SType>, Object> transition(State<SType> state, IType input); // transition from one state to another
}