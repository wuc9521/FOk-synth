package FA;

import java.util.*;


public class UnionATFA<T> {
    List<FOkATNFA<T>> automata;

    /**
     * the constructor of the UnionATFA
     * @param automata the list of automata to be unioned
     */
    public UnionATFA(List<FOkATNFA<T>> automata) {
        this.automata = automata;
    }

    /**
     * 
     * @param input
     * @return
     */
    public boolean accepts(String input){
        for (FOkATNFA<T> automaton : this.automata) {
            if (!automaton.accepts(input)) {
                return false;
            }
        }
        return true;
    }
}
