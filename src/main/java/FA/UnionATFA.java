package FA;

import org.antlr.v4.semantics.UseDefAnalyzer;

import FO.Structure;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import utils.TupleGenerator;
import visitors.FOkVisitor;

public class UnionATFA<T> {

    private Set<UState> states;
    private List<String> vars;
    FOkVisitor<T> visitor;
    List<FOkATFA<T>> automata;

    @Getter
    @Setter
    public class UState implements NFA.State<T> {
        private boolean isAccepting;
        List<FOkATFA<T>.TState> states;

        /**
         * the constructor of the UState class
         * @param states a list of states that are combined to form the union state
         */
        public UState(List<FOkATFA<T>.TState> states) {
            this.states = states;
            boolean ans = true;
            for (FOkATFA<T>.TState state : states) {
                ans &= state.isAccepting();
            }
            this.isAccepting = ans;
        }

        public boolean isAccepting() {
            return this.isAccepting;
        }
    }


    public UnionATFA(List<String> vars, FOkVisitor<T> visitor) {    
        this(visitor);
        this.vars = vars;
    }

    public UnionATFA(FOkVisitor<T> visitor) {
        this.visitor = visitor;
    }

    public UnionATFA(List<FOkATFA<T>> automata) {
        this.visitor = new FOkVisitor<>();
        this.automata = automata;
    }

    /**
     * the accept function is defined as Σ* -> {0, 1}
     * 
     * @param visitor the visitor to visit the parse tree
     * @return whether the automaton accepts the input
     * @throws Exception if the visitor fails to visit the parse tree
     */
    public boolean accepts(FOkVisitor<T> visitor) throws Exception {
        // make sure that the visitor has visited the parse tree
        this.visitor = visitor;
        assert visitor.getRootFormula() != null;
        return this.automata.stream().allMatch(automaton -> {
            try { // 必须要声明, visitor 就是 FOk 的 PDFA, allMatch 就可以模拟笛卡尔积.
                return automaton.accepts(visitor);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }


    /**
     * intersect two automata
     * 
     * @param other the other automaton to intersect with
     * @return the intersection of two automata
     */
    public UnionATFA<T> intersect(List<FOkATFA<T>> automata) {

        List<List<FOkATFA<T>.TState>> allStates = new ArrayList<>();
        for (FOkATFA<T> automaton : automata) {
            allStates.add(new ArrayList<>(automaton.getStates()));
        }

        List<List<FOkATFA<T>.TState>> stateCombinations = TupleGenerator.generateCartesian(allStates);
        List<UState> uStates = new ArrayList<>();

        for (List<FOkATFA<T>.TState> combination : stateCombinations) {
            uStates.add(new UState(combination));
        }

        // UnionATFA<T> result = new UnionATFA<>(visitor);
        // result.states = new HashSet<>(uStates);
        // return result;
        return null;
    }
    // TODO: 首先实现给 FOkATFA 套壳之后也能跑
    // TODO: 然后实现多个自动机的交 (直接另所有的都满足就可以了)
    // TODO: 生成最小 formula 这件事也可以用list模拟, 细品一下. 毕竟笛卡尔积确实也还要一个个比较.
}
