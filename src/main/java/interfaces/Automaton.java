package interfaces;
import lombok.*;

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

    // 这里第二个范型要有一个类似于 parsetree traverse pointer 的东西, 来记录当前到输入的哪一步了.
    public abstract State<SType> transition(State<SType> state); // transition from one state to another

    public abstract void determinize(); // determinize the automaton

    public abstract Automaton<SType, IType> intersect(Automaton<SType, IType> automaton); // intersect two automata
}