package FA;

import java.util.*;
import lombok.*;
import utils.*;
import visitors.*;
import antlr.*;
import FO.Assignment;
import FO.Structure;
import AST.*;

import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.*;

import antlr.FOkParser.FormulaContext;

/**
 * Alternating Tree Automata for the FO(k) logic
 */
@Getter
public class FOkATNFA<T> extends NFA<Assignment, FormulaContext> {
    private static final int largestFormulaSize = 30;
    protected Set<TState> states = new HashSet<>(); // the set of states of the automaton

    @Setter
    protected TState initialState = new TState(null); // the initial state of the automaton
    protected volatile Set<TState> currentStates = new HashSet<>(); // the current state of the
                                                           // automaton
    protected FOkVisitor<T> visitor; // the visitor to visit the parse tree
    protected Structure<T> structure; // the structure for the automaton to run on
    protected List<FOkATNFA<T>> subAutomata = new LinkedList<>(); // the set of sub-automata
    protected FormulaContext currentFormula; // the current formula to be processed
    protected boolean lookingForTrueAssignment = true; // whether the automaton is looking for a true assignment
    protected List<String> vars = new ArrayList<>(); // the list of variables that can be used to construct the states

    protected enum mode {
        INTERSECT, UNION
    }

    /**
     * the constructor of the FOkTFA class
     * 
     * @param structure the structure for the automaton to run on
     */
    public FOkATNFA(Structure<T> structure) {
        this(new ArrayList<>(), structure);
    }

    /**
     * the constructor of the FOkTFA class
     * 
     * @param vars      a list of variables that can be used to construct the states
     * @param structure the structure for the automaton to run on
     *                  observe that both the type of visitor and type of TFA are
     *                  dependent on the type of the structure
     */
    public FOkATNFA(List<String> vars, Structure<T> structure) {
        this.structure = structure;
        this.vars = vars;
        this.visitor = new FOkVisitor<>(structure);
        this.lookingForTrueAssignment = structure.pos;
        this.generateStates();
        // initialize all the states by invoking the generateTuple method
    }

    private void generateStates() {
        TupleUtils.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> { // tuple:
            // List<Element>
            Map<String, Structure<?>.Element> kvMap = new HashMap<>();
            // HashMap<String, Structure<?>.Element> kvMap = new HashMap<>();
            for (int i = 0; i < vars.size(); i++) {
                if (tuple.get(i) != null) {
                    kvMap.put(vars.get(i), (Structure<T>.Element) tuple.get(i));
                } else {// key = var, value = new Element(), by default a new element without input is
                    // undefined
                    kvMap.put(vars.get(i), structure.new Element());
                }
            }
            TState state = new TState(new Assignment(kvMap));
            if (kvMap.values().stream().allMatch(e -> e.isUndefined())) { // if all the variables are undefined, then it
                // is the initial state
                this.initialState = state;
            }
            this.states.add(state);
        });
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public class TState implements NFA.State<Assignment> {
        private boolean isAccepting = false; // whether the state is accepting
        private Assignment assignment; // the assignment of all the variables

        public TState(Assignment assignment) {
            this.assignment = assignment;
        }

        public TState(Assignment assignment, boolean isAccepting) {
            this.assignment = assignment;
            this.isAccepting = isAccepting;
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
            return this.assignment.getKvMap().get(var);
        }

        /**
         * check if the state is equal to another state except for a variable
         * 
         * @param state the state to be compared with
         * @param var   the variable to be excluded
         * @return whether the two states are equal except for the variable
         */
        public boolean equalsExceptVar(TState state, String var) {
            for (String key : this.assignment.getKvMap().keySet()) {
                if (key.equals(var)) {
                    continue; // skip the variable to be excluded
                }
                if (this.assignment.getKvMap().get(key) == null || state.assignment.getKvMap().get(key) == null) {
                    if (this.assignment.getKvMap().get(key) != state.assignment.getKvMap().get(key)) {
                        return false; // if one of the values is null and the other is not then the two states are not
                                      // equal
                    }
                } else if (!this.assignment.getKvMap().get(key).equals(state.assignment.getKvMap().get(key))) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(TState obj) {
            if (obj == this) {
                return true;
            }
            return this.assignment.equals(obj.assignment);
        }

    }

    public void addState(TState state) {
        Map<String, Structure<?>.Element> kvMap = state.assignment.getKvMap();
        boolean isInitial = kvMap.values().stream().allMatch(e -> e.isUndefined());
        if (isInitial) {
            this.initialState = state;
        }
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
        this.currentFormula = input; // don't forget to update the formula
        // otherwise there would be **STACK OVERFLOW**
        // copy() is just to avoid changing this.currentStates all the time
        if (this.currentFormula == null) {
            return Collections.emptySet(); // TODO: check here
        }
        try {
            if (this.currentFormula.NOT() != null) { // case 1: δ (γ, ¬) = (γ', 1)
                this.lookingForTrueAssignment = !this.lookingForTrueAssignment; // flip the flag
                this.currentStates = this.transition(
                        state,
                        this.currentFormula.formula(0)).stream().map(s -> (TState) s).collect(Collectors.toSet());
            } else if (this.currentFormula.op != null) { // case 2
                this.handleOp(state, this.currentFormula); // mutually recursive
            } else if (this.currentFormula.qop != null) { // case 3
                this.handleQuantifier(state, this.currentFormula); // mutually recursive
            } else if (this.currentFormula.RELATION() != null || this.currentFormula.EQUALS() != null
                    || this.currentFormula.value() != null) { // case 4, 5, 6
                this.handleRelation(state, this.currentFormula);
            } else if (this.currentFormula.LPAREN() != null) { // case 7
                this.currentStates = this.transition(state, this.currentFormula.formula(0)).stream()
                        .map(s -> (TState) s)
                        .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.currentStates.stream().collect(Collectors.toSet());
    }

    /**
     * the accept function is defined as Σ* -> {0, 1}
     * 
     * @param input the input string to be checked
     * @return whether the automaton accepts the input
     */
    public boolean accepts(String input) {
        try {
            FOkVisitor<T> visitor = new FOkVisitor<>(this.structure);
            return this.accepts(visitor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        return this.accepts(visitor.getRootFormula());
    }

    /**
     * the accept function is defined as Σ* -> {0, 1}
     * 
     * @param input the input to be checked
     * @return whether the automaton accepts the input
     * @throws Exception if the visitor fails to visit the parse tree
     */
    @Override
    public boolean accepts(FormulaContext input) throws Exception {
        this.currentFormula = input;
        this.currentStates.clear(); // accept() is like a run() of the automaton
        this.currentStates.add(this.initialState);
        this.run();
        return this.currentStates.stream().anyMatch(s -> s.isAccepting());
    }

    // run() -> transition() -> handler() -> accept() -> run()
    private void run() {
        Set<TState> newStates = new HashSet<>();
        for (TState state : this.currentStates) {
            try {
                newStates.addAll(
                        this.transition(state, this.currentFormula)
                                .stream().map(s -> (TState) s).collect(Collectors.toSet()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.currentStates = newStates;
    }

    /**
     * determinize the automaton
     */
    @Override
    public DFA<Assignment, FormulaContext> determinize() {
        FOkATDFA<T> dfa = new FOkATDFA<>(this);
        return dfa;
    }

    /**
     * handle the operation in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *                     formula
     * @param type         the type of the operation
     * @throws Exception if the operation is not supported
     */
    protected void handleOp(State<Assignment> currentState, FormulaContext input) throws Exception {
        assert input.op != null; // caller should ensure that the input is an operation
        final int opNum = input.formula().size();
        assert opNum == 2; // the operation should have two operands
        this.subAutomata.clear();
        for (int i = 0; i < opNum; i++) {
            FOkATNFA<T> automaton = this.copy();
            automaton.setInitialState((TState) currentState);
            this.subAutomata.add(automaton);
        }
        if (this.lookingForTrueAssignment) {
            switch (input.op.getType()) {
                case FOkParser.AND:
                    this.concurrentTransition(input);
                    break;
                case FOkParser.OR:
                    this.shortCutTransition(input);
                    break;
                case FOkParser.IMPLIES:
                    break;
                default:
                    break;
            }
        } else {
            switch (input.op.getType()) {
                case FOkParser.AND:
                    this.shortCutTransition(input);
                    break;
                case FOkParser.OR:
                    this.concurrentTransition(input);
                    break;
                case FOkParser.IMPLIES:
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * transition for the concurrent automata
     * 根据定义, 读取 and 和 or 的时候不转移状态, 所以只需要传一个参数.
     * 
     * @param input the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *              formula
     */
    private void concurrentTransition(FormulaContext input) throws Exception {
        assert this.subAutomata.size() == 2;
        for (int i = 0; i < 2; i++) {
            if (!this.subAutomata.get(i).accepts(input.formula(i))) {
                this.currentStates = Collections.emptySet();
                return;
            }
        }
    }

    /**
     * transition for the short-cut automata
     * 
     * @param input the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *              formula
     */
    private void shortCutTransition(FormulaContext input) throws Exception {
        for (int i = 0; i < this.subAutomata.size(); i++) {
            if (this.subAutomata.get(i).accepts(input.formula(i))) {
                // Note that this is a strong condition
                // since we even allow the sub-automaton to **ACCEPT** the input
                // 那么当前自动机就要进入到一个singleton中就可以了, 因为我们确保它可以接受.
                this.currentStates = this.subAutomata.get(i).currentStates;
                return;
            }
        }
    }

    /**
     * get the similar states of the current states except for the given variable
     * 
     * @param var the variable to be excluded
     * @return the similar states of the current states except for the given
     *         variable
     */
    public List<TState> getSimilarStates(String var) {
        List<TState> similarStates = this.states.stream().filter(
                s -> this.currentStates.stream().anyMatch(
                        s_ -> s_.equalsExceptVar(s, var)
                                && !s.getAssignment().getKvMap().get(var).isUndefined()))
                .collect(Collectors.toList());
        return similarStates;
    }

    /**
     * handle the quantifier in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula:
     *                     qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     * @param type         the type of the quantifier
     * @throws Exception if the quantifier is not supported
     */
    protected void handleQuantifier(State<Assignment> currentState, FormulaContext input) throws Exception {
        String var = input.VARIABLE().getText();
        List<TState> similarStates = this.getSimilarStates(var);

        // find all the states that are the same as the current states except for the
        // given variable
        this.subAutomata.clear();
        for (TState state : similarStates) {
            FOkATNFA<T> subAutomaton = this.copy();
            subAutomaton.setInitialState(state);
            this.subAutomata.add(subAutomaton);
        }
        if (this.lookingForTrueAssignment) {
            switch (input.qop.getType()) {
                case FOkParser.FORALL:
                    this.concurrentTransition(similarStates, input.formula(0));
                    return;
                case FOkParser.EXISTS: // make use of short-circuiting
                    this.shortCutTransition(similarStates, input.formula(0));
                    return;
                default:
                    return;
            }
        } else { // the automaton is looking for a false assignment
            switch (input.qop.getType()) {
                case FOkParser.FORALL:
                    this.shortCutTransition(similarStates, input.formula(0));
                    return;
                case FOkParser.EXISTS:
                    this.concurrentTransition(similarStates, input.formula(0));
                    return;
                default:
                    return;
            }
        }
    }

    /**
     * transition for the concurrent automata
     * 
     * @param domainStates the domain states of the automaton
     * @param input        the input formula: qop=(FORALL | EXISTS) VARIABLE DOT
     *                     LPAREN formula RPAREN
     */
    private void concurrentTransition(List<TState> domainStates, FormulaContext input) throws Exception {
        assert domainStates.size() == this.subAutomata.size();
        for (int i = 0; i < this.subAutomata.size(); i++) {
            subAutomata.get(i).currentFormula = input;
            if (!this.subAutomata.get(i).accepts(input)) {
                this.currentStates = Collections.emptySet();
                return;
            } // if one of the sub-automata fails to accept the input, then the automaton
              // fails to accept
        }
        this.currentStates = domainStates.stream().collect(Collectors.toSet());
    }

    /**
     * transition for the short-cut automata
     * 
     * @param domainStates the domain states of the automaton
     * @param input        the input formula: qop=(FORALL | EXISTS) VARIABLE DOT
     *                     LPAREN formula RPAREN
     * @throws Exception if the automaton fails to accept the input
     */
    private void shortCutTransition(List<TState> domainStates, FormulaContext input) throws Exception {
        for (int i = 0; i < this.subAutomata.size(); i++) {
            subAutomata.get(i).currentFormula = input;
            if (this.subAutomata.get(i).accepts(input)) {
                this.currentStates = Collections.singleton(domainStates.get(i));
                return;
            }
        }
        this.currentStates = Collections.emptySet(); // if none of the automata accepts the input
    }

    /**
     * handle the relation, equals and values in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula:
     *                     4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *                     5 | term EQUALS term
     *                     6 | value
     */
    protected void handleRelation(State<Assignment> currentState, FormulaContext input) {
        // actually '=' is just a binary relation and 'value' is just a nullary relation
        boolean isTrueUnderAssignment = false;
        Assignment assignment = ((TState) currentState).getAssignment();
        this.currentFormula = null; // this line is ESSNTIAL
        if (input.RELATION() != null) { // as long as the relation is defined, the automaton should stop
            String relationName = input.RELATION().getText();
            boolean allVarsOfRelationDefined = true;
            for (int i = 0; i < input.term().size(); i++) {
                if (this.visitor.getTermVal(input.term(i), assignment) == null) {
                    allVarsOfRelationDefined = false; // checked
                    break;
                }
            } // all the variables should be defined for the automata to accept the input
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
                ((TState) currentState).setAccepting(true);
                this.currentStates = Collections.singleton((TState) currentState);
            } else {
                ((TState) currentState).setAccepting(false); // not accepting means rejecting
                this.currentStates = Collections.emptySet(); // \emptyset means ending the automaton
            }
            return;
        } else if (input.EQUALS() != null) {
            try {
                T t1 = this.visitor.getTermVal(input.term(0), assignment);
                T t2 = this.visitor.getTermVal(input.term(1), assignment);
                Structure<T>.Element e1 = t1 != null ? this.structure.new Element(t1) : this.structure.new Element();
                Structure<T>.Element e2 = t2 != null ? this.structure.new Element(t2) : this.structure.new Element();
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
            this.currentStates = Collections.singleton((TState) currentState);
            return;
        }
        this.currentStates = Collections.emptySet();
    }

    /**
     * @return the shortest formula that the automaton accepts
     */
    public String getShortestFormula(boolean isTrue) {
        return this.getShortestFormula(isTrue, FOkATNFA.largestFormulaSize);
    }

    /**
     * get the shortest formula that the automaton accepts
     * 
     * @param isTrue
     * @param largestSize
     * @return
     */
    public String getShortestFormula(boolean isTrue, int largestSize) {
        Queue<AST> queue = new LinkedList<>();
        FOkATNFA<T> automaton = new FOkATNFA<>(structure);
        ASTBuilder<T> astBuilder = new ASTBuilder<>();
        AST initialAst = new AST(); // Suppose AST has a default initial state that makes sense
        queue.add(initialAst); // Start with a minimal AST

        // the tranversal of the ASTs is actually the Pushdown Automata of the FOk
        // because we are always looking for the minimal AST that satisfies the
        // automaton
        while (!queue.isEmpty()) {
            AST currentAst = queue.poll();
            if (currentAst.getSize() > largestSize) {
                break; // Stop if the AST size exceeds the limit
            }
            Set<AST> grownAsts = new HashSet<>();
            // Set<AST> grownAsts = astBuilder.grow(currentAst);
            // Method to get all possible single-step extensions of the
            // AST
            for (AST ast : grownAsts) {
                String formulaString = ast.toString(); // Assume AST has a method to convert to string formula
                ParseTree tree = ParserUtils.parse(formulaString);
                if (tree != null) {
                    try {
                        if (automaton.accepts((FormulaContext) tree) == isTrue) {
                            return formulaString;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                queue.add(ast); // Add new ASTs to queue for further exploration
            }
        }
        return null;
    }

    /**
     * copy the automaton
     * 
     * @return the copied automaton
     */
    public FOkATNFA<T> copy() {
        FOkATNFA<T> copy = new FOkATNFA<>(this.structure);
        copy.states.addAll(this.states);
        copy.initialState = this.initialState;
        copy.currentStates.addAll(this.currentStates);
        copy.visitor = this.visitor;
        copy.structure = this.structure;
        copy.subAutomata.addAll(this.subAutomata);
        copy.lookingForTrueAssignment = this.lookingForTrueAssignment;
        return copy;
    }

    /**
     * get the possible inputs of the automaton
     * 
     * @return the set of possible inputs
     */
    public Set<FormulaContext> getPossibleInputs() {
        assert this.structure != null;
        assert this.vars != null;
        return new HashSet<>();
    }
}