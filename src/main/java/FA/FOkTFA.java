package FA;

import java.util.*;
import lombok.*;
import utils.TupleGenerator;
import visitors.FOkVisitor;
import FO.Assignment;
import FO.Structure;

import java.util.stream.Collectors;

import antlr.FOkParser;
import antlr.FOkParser.FormulaContext;

public class FOkTFA<T> extends NFA<Assignment, FormulaContext> implements Runnable{

    @Getter
    private final Set<TState> states; // the set of states of the automaton
    private Set<FormulaContext> currentCxts; // the current token of the ast

    @Getter
    private TState initialState; // the initial state of the automaton

    @Getter
    private Set<TState> currentStates; // the current state of the automaton

    @Getter
    private Set<TState> finalStates; // the final states of the automaton

    @Getter
    private FOkVisitor<T> visitor; // the visitor to visit the parse tree

    @Getter
    private Structure<T> structure; // the structure for the automaton to run on

    /**
     * the constructor of the FOkTFA class
     * 
     * @param vars      a list of variables that can be used to construct the states
     * @param structure the structure for the automaton to run on
     *                  observe that both the type of visitor and type of TFA are
     *                  dependent on the type of the structure
     */
    public FOkTFA(List<String> vars, Structure<T> structure) {
        this.states = new HashSet<>();
        this.visitor = new FOkVisitor<>(structure);
        // initialize all the states by invoking the generateTuple method
        TupleGenerator.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> {
            HashMap<String, Structure<?>.Element> kvMap = new HashMap<>();
            for (int i = 0; i < vars.size(); i++) {
                kvMap.put(vars.get(i), (Structure<T>.Element) tuple.get(i));
            }
            this.states.add(new TState(new Assignment(kvMap)));
        });
    }

    @AllArgsConstructor
    public class TState implements NFA.State<Assignment> {
        @Setter
        private boolean isAccepting = false; // whether the state is accepting
        @Getter
        private Assignment allVarAsnmnt; // the assignment of all the variables

        public TState(Assignment assignment) {
            this.allVarAsnmnt = assignment;
        }

        @Override
        public boolean isAccepting() {
            return this.isAccepting;
        }

        /**
         * get the value of a variable in the assignment
         * 
         * @param var the variable to be checked
         * @return the value of the variable in the assignment
         */
        public Structure<?>.Element getValueOfVar(String var) {
            return this.allVarAsnmnt.getKvMap().get(var);
        }
    }

    public void addState(TState state) {
        this.states.add((TState) state);
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
     * 
     * @param state the current state of the automaton
     * @param input the input token
     * @return the set of states that the automaton can transit to
     *         formula
     *         1 : NOT formula
     *         2 | formula op=(IFF | IMPLIES | AND | OR) formula
     *         3 | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     *         4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *         5 | term EQUALS term
     *         6 | value
     *         7 | LPAREN formula RPAREN
     *         ;
     */
    @Override
    public Set<State<Assignment>> transition(State<Assignment> state, FormulaContext input) {
        // TODO: check
        if (input.NOT() != null) { // case 1: δ (γ, ¬) = (γ', 1)
            // return the set of states that is not in currentStates
            this.currentCxts.clear();
            this.currentCxts.add(input.formula(0)); 
            return this.states.stream().filter(s -> !this.currentStates.contains(s)).collect(Collectors.toSet());
        } else if (input.op != null) { // case 2
            this.currentCxts.clear();
            this.currentCxts.add(input.formula(0));
            this.currentCxts.add(input.formula(1));
            // TODO: 考虑Buchi自动机的并发情况. 先看视频完全理解过程.
            // switch (input.op.getType()) {
            //     case FOkParser.AND:
            //         return this.states.stream()
            //                 .filter(s -> this.visitor.getFormulaVal(left, s.getAllVarAsnmnt())
            //                         && this.visitor.getFormulaVal(right, s.getAllVarAsnmnt()))
            //                 .collect(Collectors.toSet());
            //     case FOkParser.OR:
            //         return this.states.stream()
            //                 .filter(s -> this.visitor.getFormulaVal(left, s.getAllVarAsnmnt())
            //                         || this.visitor.getFormulaVal(right, s.getAllVarAsnmnt()))
            //                 .collect(Collectors.toSet());
            //     default:
            //         break;
            // }
        } else if (input.qop != null) { // case 3
            switch (input.qop.getType()) {
                case FOkParser.FORALL:
                    return this.states.stream().filter(s -> this.visitor.getFormulaVal(input, s.getAllVarAsnmnt()))
                            .collect(Collectors.toSet());
                case FOkParser.EXISTS:
                    return null;
                default:
                    break;
            }
        } else if (input.RELATION() != null || input.EQUALS() != null || input.value() != null) { // case 4, 5, 6
            // actually '=' is just a binary relation and 'value' is just a nullary relation
            return this.currentStates.stream()
                    .filter(s -> {
                        // I should use the holds() method of relation, but its just too long....
                        if (this.visitor.getFormulaVal(input, s.getAllVarAsnmnt())) { // aka if the relation holds.
                            this.finalStates.add((TState) s);
                            this.currentCxts = new HashSet<>(); // TODO: check
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toSet());
        } else if (input.LPAREN() != null) { // case 7
            return this.transition(state, input.formula(0)); // recursive call
        }
        return null;
    }

    public boolean accept(FormulaContext input) {
        this.currentCxts = new HashSet<>();
        this.currentCxts.add(input);
        return this.currentStates.stream().anyMatch(s -> s.isAccepting());
    }
   
    @Override
    public void run() {
        this.currentStates = new HashSet<>();
    }

    public void run(FormulaContext input) {
        this.currentStates = new HashSet<>();
        this.currentStates.add(this.initialState);
        for (int i = 0; i < input.getChildCount(); i++) {
            FormulaContext child = (FormulaContext) input.getChild(i);
            Set<TState> nextStates = new HashSet<>();
            for (State<Assignment> state : this.currentStates) {
                // nextStates.addAll(this.transition(state, child));
            }
            this.currentStates = nextStates;
        }
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
    public NFA<Assignment, FormulaContext> intersect(NFA<Assignment, FormulaContext> automaton) {
        return null;
    }
}
