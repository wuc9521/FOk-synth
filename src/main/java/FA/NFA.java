package FA;
import lombok.*;
import java.util.*;

/**
 * This is an interface that represents an automaton in first-order logic.
 * An automata is formally defined as a 5-tuple (Q, Σ, δ, q0, F)
 */
@Getter @Setter
public abstract class NFA<SType, IType> {
    public interface State<SType> {
        public boolean isAccepting(); // check if the state is accepting
    }

    // 这里第二个范型要有一个类似于 parsetree traverse pointer 的东西, 来记录当前到输入的哪一步了.
    public abstract Set<State<SType>> transition(State<SType> state, IType input); // transition from one state to another

    public abstract void determinize(); // determinize the automaton

    public abstract NFA<SType, IType> intersect(NFA<SType, IType> automaton); // intersect two automata
}