public class Automaton {
    private enum State {
        START,
        INTEGER,
        DECIMAL,
        EXPONENT,
        EXPONENT_SIGN,
        EXPONENT_INTEGER,
        END
    }
}