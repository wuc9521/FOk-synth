package FA;

import java.util.*;


public class UnionATFA<T> {
    List<FOkATFA<T>> automata;

    /**
     * the constructor of the UnionATFA
     * @param automata the list of automata to be unioned
     */
    public UnionATFA(List<FOkATFA<T>> automata) {
        this.automata = automata;
    }

    /**
     * 
     * @param input
     * @return
     */
    public boolean accepts(String input){
        for (FOkATFA<T> automaton : this.automata) {
            if (!automaton.accepts(input)) {
                return false;
            }
        }
        return true;
    }
}
