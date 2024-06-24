package FA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.HashMap;

import AST.ASTBuilder;
import AST.AST;
import FO.*;
import antlr.FOkParser;
import antlr.FOkParser.*;
import lombok.Getter;
import lombok.Setter;
import visitors.FOkVisitor;
import utils.DataUtils;
import utils.ParserUtils;
import utils.SetUtils;

@Getter
public class FOkATDFA<T> extends DFA<Assignment, FormulaContext> {
    private FOkATNFA<T> nfa;
    private Set<TState> states = new HashSet<>();
    private TState initialState = new TState();
    private TState currentState = initialState;
    private FormulaContext currentFormula = null;
    private boolean lookingForTrueAssignment = true;
    private HashMap<TState, HashMap<FormulaContext, TState>> transitions = new HashMap<>();
    private List<FOkATDFA<T>> subAutomata = new ArrayList<>();
    private Structure<T> structure;
    private FOkVisitor<T> visitor;
    private ASTBuilder<T> builder;
    private Set<FormulaContext> AllRelFormulaCtx = new HashSet<>();
    private Stack<FormulaContext> formulaStack = new Stack<>();

    /**
     * constructor for a DFA
     * 
     * @param nfa the nfa to be converted to a dfa
     */
    public FOkATDFA(FOkATNFA<T> nfa) {
        this.nfa = nfa;
        this.structure = nfa.structure;
        this.visitor = nfa.visitor;
        this.builder = new ASTBuilder<>(this.structure, this.nfa.getVars());
        // generate the power set of the nfa's states
        System.out.println(this.nfa.states.size());
        // Set<Set<FOkATNFA<T>.TState>> powerSet = SetUtils.powerSet(this.nfa.states);
        // for (Set<FOkATNFA<T>.TState> set : powerSet) {
        // this.transitions.put(new TState(set), new HashMap<>());
        // } // TODO: 上面的太复杂, 算不了了
        for (AST formulaAST : this.builder.getAllRelationLiterals()) {
            String formula = formulaAST.toString();
            this.visitor.visit(ParserUtils.parse(formula));
            this.AllRelFormulaCtx.add(this.visitor.getRootFormula());
        }
    }

    /**
     * This is an interface that represents a state in an automaton.
     */
    @Getter
    @Setter
    public class TState implements DFA.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        private Set<FOkATNFA<T>.TState> value; // equivalent states in the nfa (with respect to the subset construction
                                               // algorithm)

        // Note: 我们在这里将所有“存在”的情况也转成“任意”的情况.
        // \exists s\in S: s \in AS (:= Set of Accepting States) and xxx
        // \iff \forall s\in AS: s \in AS (!= \emptyset) and xxx
        /**
         * constructor for a DFA state
         * 
         * @param states
         */
        public TState(Set<FOkATNFA<T>.TState> states) {
            this.value = states;
            this.isAccepting = states.stream().allMatch(s -> s.isAccepting());
            // if all the states in the set are accepting, then the state is accepting
        }

        public TState() {
            this.value = Collections.emptySet();
        }

        public TState(DFA.State<Assignment> state) {
            this.value = ((TState) state).value;
            this.isAccepting = ((TState) state).isAccepting;
        }

        @Override
        public boolean isAccepting() {
            return this.isAccepting;
        }

        public boolean equals(TState tState) {
            return this.value.equals(tState.value) && this.isAccepting == tState.isAccepting;
        }
    }

    /**
     * @param input the input formula
     * @return whether the automaton accepts the input
     */
    public boolean accepts(FormulaContext input) {
        TState state = this.initialState;
        this.currentFormula = input;
        this.formulaStack.push(this.currentFormula);
        FormulaContext formula = input;
        while (this.currentFormula != null) {
            if (this.transition(state, formula) == null) {
                break;
            }
            state = (TState) this.transition(state, formula);
            formula = this.currentFormula;
        }
        return state.isAccepting();
    }

    /**
     * @param state the state to transition from
     * @param input the input to be checked
     * @return the state that the automaton can transit to
     */
    @Override
    public DFA.State<Assignment> transition(DFA.State<Assignment> state, FormulaContext input) {
        // TState tState = (TState) state;
        // for (FOkATNFA<T>.TState s : tState.value) {
        // if (nfa.transition(s, input) != null) {
        // return null;
        // }
        // }
        // return null;
        this.currentFormula = input;
        TState tState = (TState) state;
        if (this.currentFormula == null) {
            return null;
        }

        if (this.transitions.get(state) != null) {
            return this.transitions.get(state).get(this.currentFormula);
        }
        try {
            if (this.currentFormula.NOT() != null) {
                this.lookingForTrueAssignment = !this.lookingForTrueAssignment;
                this.currentState = (TState) this.transition(state, this.currentFormula.formula(0));
            } else if (this.currentFormula.op != null) {
                // this.currenState 还是当前的状态, 但是 currentFormula 变成了 op 的左边的那个.
                if (this.lookingForTrueAssignment) {
                    switch (this.currentFormula.op.getType()) {
                        case FOkParser.AND:
                            this.concurrentTransition(input);
                            break;
                        case FOkParser.OR:
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (this.currentFormula.op.getType()) {
                        case FOkParser.AND:
                            break;
                        case FOkParser.OR:
                            break;
                        default:
                            break;
                    }

                }
            } else if (this.currentFormula.qop != null) {
                this.handleQop(input);
            } else if (this.currentFormula.LPAREN() != null) { // formula = (formula)
                return this.transition(state, this.currentFormula.formula(0));
            } else if (this.currentFormula.RELATION() != null || this.currentFormula.EQUALS() != null
                    || this.currentFormula.value() != null) {
                this.handleRelation(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.currentState;
        // 在 DFA 里, 这里不能一路递归到底, 只能递归一点点.
    }

    /**
     * @param input the input formula
     */
    private void handleRelation(FormulaContext input) {
        boolean isTrueUnderAssignment = false;
        this.currentFormula = null;

        // 我们要求当前状态对应的所有nfa状态必须都为真.
        for (FOkATNFA<T>.TState state : this.currentState.value) {
            Assignment assignment = state.getAssignment();
            if (input.RELATION() != null) {
                String relationName = input.RELATION().getText();
                boolean allVarsOfRelationDefined = true;
                for (int i = 0; i < input.term().size(); i++) {
                    if (this.visitor.getTermVal(input.term(i), assignment) == null) {
                        allVarsOfRelationDefined = false; // checked
                        break;
                    }
                }
                if (this.structure.relations.containsKey(relationName)) {
                    isTrueUnderAssignment = this.structure.relations.get(relationName).holds(
                            input.term().stream()
                                    .map(t -> this.structure.new Element(this.visitor.getTermVal(t, assignment)))
                                    .collect(Collectors.toList()));
                }
                if (!allVarsOfRelationDefined) {
                    isTrueUnderAssignment = false;
                }
                if (isTrueUnderAssignment == this.lookingForTrueAssignment) {
                    this.currentState.setAccepting(true);
                } else {
                    ((TState) currentState).setAccepting(false); // not accepting means rejecting
                    // this.currentStates = Collections.emptySet(); // \emptyset means ending the
                    // automato
                }
                return;
            } else if (input.EQUALS() != null) {
                try {
                    T t1 = this.visitor.getTermVal(input.term(0), assignment);
                    T t2 = this.visitor.getTermVal(input.term(1), assignment);
                    Structure<T>.Element e1 = t1 != null ? this.structure.new Element(t1)
                            : this.structure.new Element();
                    Structure<T>.Element e2 = t2 != null ? this.structure.new Element(t2)
                            : this.structure.new Element();
                    isTrueUnderAssignment = e1.equals(e2);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else if (input.value() != null) {
                isTrueUnderAssignment = input.value().getText().equals(DataUtils.TRUE);
            }
            // if the formula is what it should be under the assignment of the state,
            if (isTrueUnderAssignment == this.lookingForTrueAssignment) {
                ((TState) currentState).setAccepting(true);
                // this.currentStates = Collections.singleton((TState) currentState);
                return;
            }
        }
    }

    /**
     * transition for the concurrent automata
     * 
     * @param input the input formula: formula op=(IFF | IMPLIES | AND | OR) formula
     * @return the state that the automaton can transit to
     * @throws Exception if the input formula is not valid
     */
    private void concurrentTransition(FormulaContext input) throws Exception {
        assert this.subAutomata.size() == 2;
        for (int i = 0; i < 2; i++) {
            this.nfa.subAutomata.get(i).currentStates = this.currentState.value;
            this.nfa.subAutomata.get(i).currentFormula = input.formula(i);
            this.nfa.lookingForTrueAssignment = this.lookingForTrueAssignment;
            if (!this.nfa.subAutomata.get(i).accepts(input.formula(i))) {
                this.currentState = null;
                return;
            }
        }
    }

    /**
     * transition for the shortcut automata
     * 
     * @param input the input formula: formula op=(IFF | IMPLIES | AND | OR) formula
     * @throws Exception if the input formula is not valid
     */
    private void shortcutTransition(FormulaContext input) throws Exception {
        assert this.subAutomata.size() == 2;
        // TODO: maybe the responsibility to copy() nfa is here.
        Set<FOkATNFA<T>.TState> states = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            if (this.nfa.subAutomata.get(i).accepts(input.formula(i))) {
                states.addAll(this.nfa.subAutomata.get(i).currentStates);
            }
        }
        this.currentState = new TState(states);
    }

    /**
     * handle the quantifier operator
     * 
     * @param input
     */
    private void handleQop(FormulaContext input) {
    }

    /**
     * @return the minimized version of the current automaton
     */
    public FOkATDFA<T> minimize() {
        return this;
    }

    // 要非常优雅地把这里的 NFA 给用上. 该用 NFA 来做的苦力要让 NFA来做. 比如说并发.
    // 比如说 \exists xxx accept().
}

// TODO: 去了解一下 deterministic pushdown automata.