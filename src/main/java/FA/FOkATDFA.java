package FA;

import java.util.HashSet;
import java.util.Set;

import AST.AST.FOk;
import FO.*;
import antlr.FOkParser.FormulaContext;
import lombok.Getter;  

public class FOkATDFA<T> extends DFA<Assignment, FormulaContext> {
    FOkATNFA<T> nfa;

    /**
     * the constructor of the FOkATDFA
     * @param nfa the nfa to be converted to dfa
     */
    public FOkATDFA(FOkATNFA<T> nfa) {
        this.nfa = nfa;
    }

    /**
     * This is an interface that represents a state in an automaton.
     */
    @Getter
    public class TState implements DFA.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        Set<FOkATNFA<T>.TState> value;
        public TState(Set<FOkATNFA<T>.TState> states) {
            this.value = states;
        }
        public TState() {
            this.value = new HashSet<>();
        }
        


        @Override
        public boolean isAccepting() {
            return this.isAccepting;
        }
    }
    /**
     * 
     * @param input
     * @return
     */
    public boolean accepts(FormulaContext input){
        return false;
    }

    /**
     * @param state the state to transition from
     * @param input the input to be checked
     * @return the state that the automaton can transit to
     */
    @Override
    public DFA.State<Assignment> transition(DFA.State<Assignment> state, FormulaContext input) {
        assert state != null && state instanceof TState;
        TState tState = (TState) state;
        return tState;
    }


    /**
     * @return the minimized version of the current automaton
     */
    public FOkATDFA<T> minimize() {
        return this;
    }
}
