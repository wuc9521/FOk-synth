
import java.util.*;
import lombok.*;
import utils.TupleGenerator;
import org.antlr.runtime.Token;

import FA.Automaton;
import FO.Assignment;
import FO.Structure;

import java.util.stream.Collectors;

import antlr.FOkParser;

@AllArgsConstructor
public class TreeAutomaton extends Automaton<Assignment, Token> {

    @Getter
    private final Set<TreeState> states; // the set of states of the automaton

    @Getter
    private TreeState initialState; // the initial state of the automaton

    @Getter
    private TreeState currentState; // the current state of the automaton

    public TreeAutomaton(List<String> vars, Structure<?> structure) {
        this.states = new HashSet<>();

        // initialize all the states by invoking the generateTuple method
        TupleGenerator.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> {
            HashMap<String, Structure<?>.Element> kvMap = new HashMap<>();
            for (int i = 0; i < vars.size(); i++) {
                kvMap.put(vars.get(i), (Structure<?>.Element)tuple.get(i));
            }
            this.states.add(
                new TreeState(
                    new Assignment(kvMap)
                )
            );
        });
    }

    @AllArgsConstructor
    public class TreeState implements Automaton.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        @Getter
        private Assignment allVarAsnmnt; // the assignment of all the variables
        public TreeState(Assignment assignment) {
            this.allVarAsnmnt = assignment;
        }

        @Override
        public boolean isAccepting() {
            return this.isAccepting;
        }

        public Structure<?>.Element getValueOfVar(String var) {
            return this.allVarAsnmnt.getKvMap().get(var);
        }
    }

    // @Override
    // public boolean accepts(Token input) {
    //     return this.run(input).isAccepting();
    // }

    // public TreeState run(ParseTree input) {
    //     TreeState currentState = this.initialState;
    //     this.transitionVisitor.visit(input);
    //     return currentState;
    // }

    public void addState(TreeState state) {
        this.states.add((TreeState) state);
    }

    public void removeState(State<Assignment> state) {
        this.states.remove(state);
    }

    public boolean hasState(State<Assignment> state) {
        return this.states.contains(state);
    }

    
    /**
     * the transition function is defined as Q x Σ -> 2^Q, 
     * since the alternating automaton is non-deterministic
     */
    @Override
    public Set<State<Assignment>> transition(State<Assignment> state, Token input) {
        switch (input.getType()) {
            case FOkParser.AND:
                return this.states.stream()
                .filter(s -> s.isAccepting())
                .collect(Collectors.toSet());
            case FOkParser.OR:
                break;
            case FOkParser.FORALL:
                break;
            case FOkParser.EXISTS:
                break;
            case FOkParser.RELATION:
                break;
            case FOkParser.NOT:
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * determinize the automaton
     */
    @Override
    public void determinize() {

    }

    /**
     * intersect two automata
     * 
     * @param automaton
     * @return the intersection of two automata
     */
    @Override
    public Automaton<Assignment, Token> intersect(Automaton<Assignment, Token> automaton) {
        return null;
    }
}
