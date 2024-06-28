package FA;

import java.util.*;

import org.antlr.v4.runtime.ParserRuleContext;

import antlr.FOkParser;
import antlr.FOkParser.FormulaContext;
import lombok.Getter;
import lombok.Setter;
import structures.GraphStructure.E;
import AST.AST;

import java.util.stream.*;

// extended from FOkATNFA
public class FOkEATA<T> {

    private final FOkATNFA<T> nfa;
    private final Set<EState> states = new HashSet<>();
    private final Map<EState, Map<InputTokens, Set<EState>>> transitions = new HashMap<>();
    private boolean lookingForTrueAssignment = true;
    private EState initialState;

    private static enum EStateType {
        UNIVERSAL,
        EXTISTENTIAL
    }

    private static enum InputTokenType {
        AND,
        OR,
        FORALL,
        EXISTS,
        VAR,
        RELATION,
        NOT
    }

    public class InputTokens {
        List<InputTokenType> tokens;
        String vars; // the variables in tokens

        public InputTokens(InputTokenType... tokens) {
            this.tokens = Arrays.asList(tokens);
        }

        public InputTokens(InputTokenType token, String var) {
            this.tokens = Arrays.asList(token);
            this.vars = var;
        }
    }

    /**
     * constructor of the FOkEATA
     * 
     * @param nfa the nfa to be determinized
     */
    public FOkEATA(FOkATNFA<T> nfa) {
        this.nfa = nfa;
        this.initStates();
    }

    @Getter
    @Setter
    public class EState {
        private List<FOkATNFA<T>.TState> tStates;
        private EStateType type;
        private boolean isAccepting = false;

        /**
         * constructor of the EState
         * 
         * @param tStates the TStates of the state
         * @param type    the type of the state
         */
        public EState(List<FOkATNFA<T>.TState> tStates, EStateType type) {
            this.tStates = tStates;
            this.type = type;
        }

        public EState(List<FOkATNFA<T>.TState> tStates, EStateType type, boolean isAccepting) {
            this.tStates = tStates;
            this.type = type;
            this.isAccepting = isAccepting;
        }

        /**
         * constructor of the EState
         * 
         * @param tStates the TStates of the state
         */
        public EState(List<FOkATNFA<T>.TState> tStates) {
            this.tStates = tStates;
            this.type = EStateType.EXTISTENTIAL; // default type is existential
        }

        /**
         * override the equals function
         * 
         * @param state the state to be compared
         * @return whether the two states are equal
         */
        public boolean equals(EState state) {
            return this.tStates.equals(state.tStates) && this.type == state.type
                    && this.isAccepting == state.isAccepting;
        }
    }

    public void initTransitions() {
        this.transitions.clear();
        for (EState state : this.transitions.keySet()) {
            switch (state.type) {
                case EXTISTENTIAL:
                    if (state.tStates.size() == 1) { // raw state
                        this.initRawTransitions(state);
                        break;
                    } 
                case UNIVERSAL: // all the raw states should be existential
                    assert state.tStates.size() > 1;
                    this.initComplexTransitions(state);
                default:
                    break;
            }
        }
    }

    /**
     * helper function to initialize the raw transitions of the EATA
     * 
     * @param state the state to be initialized
     */
    private void initRawTransitions(EState state) {
        assert state.tStates.size() == 1;
        FOkATNFA<T>.TState tState = state.tStates.get(0);
        InputTokens input;
        Set<EState> nextStates;
        // ================= and =================
        input = new InputTokens(InputTokenType.AND); // read an AND token
        nextStates = new HashSet<>(Arrays.asList(new EState(Arrays.asList(tState, tState), EStateType.UNIVERSAL)));
        this.transitions.get(state).put(input, nextStates);

        // ================= or =================
        input = new InputTokens(InputTokenType.OR);
        nextStates = new HashSet<>(Arrays.asList(new EState(Arrays.asList(tState, tState), EStateType.EXTISTENTIAL)));
        this.transitions.get(state).put(input, nextStates);

        // =============== forall ================
        for (String var : this.nfa.getVars()) {
            input = new InputTokens(InputTokenType.FORALL, var);
            nextStates = new HashSet<>(Arrays.asList(new EState(nfa.getSimilarStates(var), EStateType.UNIVERSAL)));
            this.transitions.get(state).put(input, nextStates);
        }

        // =============== exists ================
        for (String var : this.nfa.getVars()) {
            input = new InputTokens(InputTokenType.EXISTS, var);
            nextStates = new HashSet<>(Arrays.asList(new EState(nfa.getSimilarStates(var), EStateType.EXTISTENTIAL)));
            this.transitions.get(state).put(input, nextStates);
        }

        // ============== relation ===============
        // TODO: handle R(x, y, z) and t1 = t2 cases
        for (String relationName : this.nfa.getStructure().relations.keySet()) {
            input = new InputTokens(InputTokenType.RELATION, relationName);
            // boolean relationIsTrue = this.nfa.getStructure().relations.get(relationName).holds(
            //         state.tStates.stream().map(tState -> new E(tState, this.nfa.getStructure())).collect(Collectors.toList()));
            // if(!relationIsTrue){
            //     break; // if the relation is false, then the state is not reachable
            // }
            for (EState nextState : this.transitions.keySet()) {
                if (nextState.tStates.size() == 1 && nextState.tStates.get(0).isAccepting()) {
                    nextStates.add(nextState);
                    break;
                }
            }
            this.transitions.get(state).put(input, nextStates);
        }
        // not
        return;
    }

    private void initComplexTransitions(EState state) {
        assert state.tStates.size() > 1;
        // TODO.
    }

    public FOkEATA<T> determinize(FOkATNFA<T> nfa) {
        return new FOkEATA<T>(nfa).UDeterminize().EDeterminize();
    }

    /**
     * helper function: universal determinize
     * 
     * @return the determinized EATA
     */
    private FOkEATA<T> UDeterminize() {
        // 修改 this.transition 函数, 和 this.states
        return null;
    }

    /**
     * helper function: existential determinize
     * 
     * @return the determinized EATA
     */
    private FOkEATA<T> EDeterminize() {
        // 修改 this.transition 函数, 和 this.states
        return null;
    }

    /**
     * helper function to initialize the states of the EATA
     */
    private void initStates() {
        List<String> vars = nfa.getVars();

        Set<FOkATNFA<T>.TState> nfaStates = nfa.getStates();
        nfaStates.forEach(state -> {
            this.states.add(
                    new EState(Arrays.asList(state), EStateType.EXTISTENTIAL));
            this.states.add(
                    new EState(Arrays.asList(state), EStateType.EXTISTENTIAL, true));
            this.states.add(
                    new EState(Arrays.asList(state, state), EStateType.UNIVERSAL));
            this.states.add(
                    new EState(Arrays.asList(state, state), EStateType.EXTISTENTIAL));
        });
        for (String var : vars) {
            List<FOkATNFA<T>.TState> similarStates = new ArrayList<>(nfa.getSimilarStates(var));
            this.states.add(new EState(similarStates, EStateType.UNIVERSAL));
            this.states.add(new EState(similarStates, EStateType.EXTISTENTIAL));
        }
        // set initial state
        this.initialState = new EState(Arrays.asList(nfa.getInitialState()), EStateType.UNIVERSAL);
    }
}
