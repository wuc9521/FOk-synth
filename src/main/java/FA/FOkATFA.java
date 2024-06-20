package FA;

import java.util.*;

import lombok.*;
import utils.TupleGenerator;
import visitors.FOkVisitor;
import FO.Assignment;
import FO.Structure;
import utils.Data;

import java.util.stream.Collectors;

import antlr.FOkParser;
import antlr.FOkParser.FormulaContext;

/**
 * Alternating Tree Automata for the FO(k) logic
 */
@Getter
public class FOkATFA<T> extends NFA<Assignment, FormulaContext> {

    protected Set<TState> states = new HashSet<>(); // the set of states of the automaton

    @Setter
    protected TState initialState = new TState(null); // the initial state of the automaton
    protected Set<TState> currentStates = new HashSet<>(); // the current state of the
                                                           // automaton
    protected FOkVisitor<T> visitor; // the visitor to visit the parse tree
    protected Structure<T> structure; // the structure for the automaton to run on
    protected List<FOkATFA<T>> subAutomata = new LinkedList<>(); // the set of sub-automata
    protected FormulaContext currentFormula; // the current formula to be processed
    protected boolean lookingForTrueAssignment = true; // whether the automaton is looking for a true assignment

    /**
     * the constructor of the FOkTFA class
     * 
     * @param structure the structure for the automaton to run on
     */
    public FOkATFA(Structure<T> structure) {
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
    public FOkATFA(List<String> vars, Structure<T> structure) {
        this.structure = structure;
        this.visitor = new FOkVisitor<>(structure);
        // initialize all the states by invoking the generateTuple method
        TupleGenerator.generateKTuples(vars.size(), new ArrayList<>(structure.domain)).forEach(tuple -> { // tuple:
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
                        return false; // if one of the values is null, then the two states are not equal
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
        this.currentFormula = input; // 这里注意一定要更新当前的formula, 不然就会爆栈
        try {
            // copy() 主要就是为了避免递归的过程中不停改变 this.currentStates
            if (this.currentFormula.NOT() != null) { // case 1: δ (γ, ¬) = (γ', 1)
                this.lookingForTrueAssignment = !this.lookingForTrueAssignment; // flip the flag
                this.currentStates = this.transition(
                        state,
                        this.currentFormula.formula(0)).stream().map(s -> (TState) s).collect(Collectors.toSet());
            } else if (this.currentFormula.op != null) { // case 2
                this.currentStates = this.handleOp(state, this.currentFormula); // mutually recursive
            } else if (this.currentFormula.qop != null) { // case 3
                this.currentStates = this.handleQuantifier(state, this.currentFormula); // mutually recursive
            } else if (this.currentFormula.RELATION() != null || this.currentFormula.EQUALS() != null
                    || this.currentFormula.value() != null) { // case 4, 5, 6
                this.currentStates = this.handleRelation(state, this.currentFormula);
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

    private void printCurrentStates() {
        for (TState s : this.currentStates) {
            s.getAssignment().getKvMap().forEach((k, v) -> {
                // System.out.print(k + " -> " + v.getValue() + " ");
                if (v.getValue() != null) {
                    System.out.print(k + " -> " + v.getValue() + " ");
                } else {
                    System.out.print(k + " -> " + "null ");
                }
            });
            System.out.println();
        }
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

    private void printCurrentAcceptingStates() {
        // 假设已经运行完毕
        this.currentStates.forEach(s -> {
            if (s.isAccepting()) {
                s.getAssignment().getKvMap().forEach((k, v) -> {
                    System.out.print(k + " -> " + v.getValue() + " ");
                });
                System.out.println();
            }
        });
    }

    /**
     * run the automaton
     * run 是必不可少的, 必须要很好地定义转移函数, 不能使用check来蒙混过关.
     */
    private void run() {
        while (this.currentFormula != null && !this.currentStates.isEmpty()) {
            System.out.println(this.currentFormula.getText() + " " + this.currentStates.size());
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
    public NFA<Assignment, FormulaContext> intersect(NFA<Assignment, FormulaContext> other) {
        return null;
    }

    /**
     * handle the operation in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *                     formula
     * @param type         the type of the operation
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    protected Set<TState> handleOp(State<Assignment> currentState, FormulaContext input) {
        assert input.op != null; // caller should ensure that the input is an operation
        final int opNum = input.formula().size();
        Set<TState> resultStates = new HashSet<>();
        assert opNum == 2; // the operation should have two operands
        resultStates = this.currentStates.stream().collect(Collectors.toSet());
        this.subAutomata.clear(); // TODO ?
        for (int i = 0; i < opNum; i++) {
            FOkATFA<T> automaton = this.copy();
            automaton.setInitialState((TState) currentState);
            this.subAutomata.add(automaton);
        }
        if (this.lookingForTrueAssignment) {
            switch (input.op.getType()) {
                case FOkParser.AND:
                    resultStates = this.concurrent2wayTransition((TState) currentState, input);
                    break;
                case FOkParser.OR:
                    resultStates = this.shortCut2wayTransition((TState) currentState, input);
                    break;
                case FOkParser.IMPLIES: // TODO: finish the implementation
                    break;
                default:
                    break;
            }
        } else {
            switch (input.op.getType()) {
                case FOkParser.AND:
                    resultStates = this.shortCut2wayTransition((TState) currentState, input);
                    break;
                case FOkParser.OR:
                    resultStates = this.concurrent2wayTransition((TState) currentState, input);
                    break;
                case FOkParser.IMPLIES:
                    break;
                default:
                    break;
            }
        }
        return resultStates;
    }

    /**
     * transition for the concurrent automata
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *                     formula
     * @return the set of states that the automaton can transit to
     */
    private Set<TState> concurrent2wayTransition(TState currentState, FormulaContext input) {
        Set<TState> resultStates = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            Set<TState> updatedStates = this.subAutomata.get(i).transition(currentState, input.formula(i))
                    .stream()
                    .map(s -> (TState) s).collect(Collectors.toSet());
            resultStates.addAll(updatedStates);
            this.merge(this.subAutomata.get(i), true);
        }
        return resultStates;
    }

    /**
     * transition for the short-cut automata
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula: formula op=(IFF | IMPLIES | AND | OR)
     *                     formula
     * @return the set of states that the automaton can transit to
     */
    private Set<TState> shortCut2wayTransition(TState currentState, FormulaContext input) {
        Set<TState> resultStates = new HashSet<>();
        for (int i = 0; i < this.subAutomata.size(); i++) {
            Set<TState> updatedStates = subAutomata.get(i).transition(currentState, input.formula(i)).stream()
                    .map(s -> (TState) s).collect(Collectors.toSet());
            if (!updatedStates.isEmpty()) {
                resultStates = updatedStates;
                this.merge(this.subAutomata.get(i), false);
                break;
            }
        }
        return resultStates;
    }

    /**
     * handle the quantifier in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula:
     *                     qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     * @param type         the type of the quantifier
     * @return the set of states that the automaton can transit to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    protected Set<TState> handleQuantifier(State<Assignment> currentState, FormulaContext input) {
        String var = input.VARIABLE().getText();

        List<TState> similarStates = this.states.stream().filter(
                s -> this.currentStates.stream().anyMatch(s_ -> s_.equalsExceptVar(s, var)))
                .collect(Collectors.toList());

        // find all the states that are the same as the current states except for the
        // given variable
        Set<TState> resultStates = new HashSet<>();
        this.subAutomata.clear();
        for (TState state : similarStates) {
            FOkATFA<T> subAutomaton = this.copy();
            subAutomaton.setInitialState(state);
            this.subAutomata.add(subAutomaton);
        }
        if (this.lookingForTrueAssignment) {
            switch (input.qop.getType()) {
                case FOkParser.FORALL:
                    resultStates = this.concurrentTransition(similarStates, input);
                case FOkParser.EXISTS: // make use of short-circuiting
                    resultStates = this.shortCutTransition(similarStates, input);
                default:
                    break;
            }
        } else { // the automaton is looking for a false assignment
            switch (input.qop.getType()) {
                case FOkParser.FORALL:
                    resultStates = this.shortCutTransition(similarStates, input);
                case FOkParser.EXISTS:
                    resultStates = this.concurrentTransition(similarStates, input);
                default:
                    break;
            }
        }
        return resultStates;
    }

    /**
     * transition for the concurrent automata
     * 
     * @param domainStates
     * @param input
     * @return
     */
    private Set<TState> concurrentTransition(List<TState> domainStates, FormulaContext input) {
        Set<TState> resultStates = new HashSet<>();
        for (int i = 0; i < this.subAutomata.size(); i++) {
            Set<TState> updatedStates = this.subAutomata.get(i).transition(domainStates.get(i), input.formula(0))
                    .stream()
                    .map(s -> (TState) s).collect(Collectors.toSet());
            resultStates.addAll(updatedStates.stream().map(s -> (TState) s).collect(Collectors.toSet()));
            this.merge(this.subAutomata.get(i), true);
        }
        // change the isAccepting flag of the states
        return this.updateIsAccepting(resultStates);
    }

    // TODO: 目前为止的设计都是没有问题的, 但是由于太困了实现可能都不对
    // TODO: 看一下和ChatGPT对话的最后一部分, 关于Java的Set中加入已有的对象会发生什么
    private Set<TState> updateIsAccepting(Set<TState> resultStates) {
        for (TState state : resultStates) {
            if (state.isAccepting()) {
                if (!this.currentStates.contains(state)) {
                    this.currentStates.add(state);
                }
            } else {
                state.setAccepting(false);
                this.currentStates.remove(state);
            }
        }
        return resultStates;
    }

    /**
     * transition for the short-cut automata
     * 
     * @param domainStates
     * @param input
     * @return
     */
    private Set<TState> shortCutTransition(List<TState> domainStates, FormulaContext input) {
        Set<TState> resultStates = new HashSet<>();
        for (int i = 0; i < this.subAutomata.size(); i++) {
            Set<TState> updatedStates = subAutomata.get(i).transition(domainStates.get(i), input.formula(0))
                    .stream()
                    .map(s -> (TState) s).collect(Collectors.toSet());
            if (!updatedStates.isEmpty()) {
                resultStates = updatedStates;
                this.merge(this.subAutomata.get(i), false);
                break;
            }
        }
        return resultStates;
    }

    /**
     * handle the relation, equals and values in the formula
     * 
     * @param currentState the current state of the automaton
     * @param input        the input formula:
     *                     4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *                     5 | term EQUALS term
     *                     6 | value
     * @return the set of states that the automaton can transit to
     */
    protected Set<TState> handleRelation(State<Assignment> currentState, FormulaContext input) {
        // actually '=' is just a binary relation and 'value' is just a nullary relation
        boolean isTrueUnderAssignment = false;
        Assignment assignment = ((TState) currentState).getAssignment();
        this.currentFormula = null; // this line is ESSNTIAL
        if (input.RELATION() != null) {
            String key = input.RELATION().getText();
            if (this.structure.relations.containsKey(key)) {
                isTrueUnderAssignment = this.structure.relations.get(key).holds(
                        input.term().stream()
                                .map(t -> this.structure.new Element(this.visitor.getTermVal(t, assignment)))
                                .collect(Collectors.toList()));
            }
            // if the formula is what it should be under the assignment of the state,
            if (isTrueUnderAssignment == this.lookingForTrueAssignment) {
                boolean allVarsOfRelationDefined = true;
                for (int i = 0; i < input.term().size(); i++) {
                    // 如果给关系的某一个参数赋值为null, 那么这个关系的变量就是 undefined.
                    if (this.visitor.getTermVal(input.term(i), assignment) == null) {
                        allVarsOfRelationDefined = false; // checked
                        break;
                    }
                }
                if (allVarsOfRelationDefined && (isTrueUnderAssignment == this.lookingForTrueAssignment)) {
                    ((TState) currentState).setAccepting(true);
                } else {
                    ((TState) currentState).setAccepting(false);
                }
                return Collections.singleton((TState) currentState);
            }
        } else if (input.EQUALS() != null) {
            isTrueUnderAssignment = this.structure.new Element(
                    this.visitor.getTermVal(input.term(0), assignment))
                    .equals(this.structure.new Element(this.visitor.getTermVal(input.term(1), assignment)));
        } else if (input.value() != null) {
            isTrueUnderAssignment = input.value().getText().equals(Data.TRUE);
        }
        // if the formula is what it should be under the assignment of the state,
        if (isTrueUnderAssignment == this.lookingForTrueAssignment) {
            ((TState) currentState).setAccepting(true);
            return Collections.singleton((TState) currentState);
        }
        return Collections.emptySet();
    }

    /**
     * @return the shortest formula that the automaton accepts
     */
    public String shortestFormula() {
        return null;
    }

    /**
     * copy the automaton
     * 
     * @return the copied automaton
     */
    public FOkATFA<T> copy() {
        FOkATFA<T> copy = new FOkATFA<>(this.structure);
        copy.states.addAll(this.states);
        copy.initialState = this.initialState; // TODO?
        copy.currentStates.addAll(this.currentStates);
        copy.visitor = this.visitor;
        copy.structure = this.structure;
        copy.subAutomata.addAll(this.subAutomata);
        copy.lookingForTrueAssignment = this.lookingForTrueAssignment;
        return copy;
    }

    /**
     * copy the automaton
     * 
     * @param isLookingForTrueAssignment whether the automaton is looking for a true
     *                                   assignment
     * @return the copied automaton
     */
    public FOkATFA<T> copy(boolean isLookingForTrueAssignment) {
        FOkATFA<T> copy = this.copy();
        copy.lookingForTrueAssignment = isLookingForTrueAssignment;
        return copy;
    }

    /**
     * merge the information of the sub-automata
     * 
     * @param automaton the automaton to be merged with
     * @param intersect if set to true, then should intersect all the accepting
     *                  states
     *                  if set to false, then should union all the accepting states
     */
    public void merge(FOkATFA<T> automaton, boolean intersect) {
        this.currentFormula = automaton.currentFormula;
        // this.lookingForTrueAssignment = automaton.lookingForTrueAssignment;
        Set<TState> tmpStates = this.currentStates;
        if (intersect) {   
            tmpStates.retainAll(automaton.currentStates);
        } else {
            tmpStates.addAll(automaton.currentStates);
        }
        for (TState state : tmpStates) {
            if (state.isAccepting()) {
                if (!this.currentStates.contains(state)) {
                    this.currentStates.add(state);
                }
            } else {
                state.setAccepting(false);
                this.currentStates.remove(state);
            }
        }
    }
}