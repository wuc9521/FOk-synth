// Generated from src/main/ag/FOkLexer.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FOkLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		FORALL=1, EXISTS=2, AND=3, OR=4, NOT=5, IMPLIES=6, IFF=7, LPAREN=8, RPAREN=9, 
		COMMA=10, DOT=11, VARIABLE=12, CONSTANT=13, PREDICATE=14, FUNCTION=15, 
		WS=16, COMMENT=17, LINE_COMMENT=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"FORALL", "EXISTS", "AND", "OR", "NOT", "IMPLIES", "IFF", "LPAREN", "RPAREN", 
			"COMMA", "DOT", "VARIABLE", "CONSTANT", "PREDICATE", "FUNCTION", "WS", 
			"COMMENT", "LINE_COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "'('", "')'", "','", 
			"'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FORALL", "EXISTS", "AND", "OR", "NOT", "IMPLIES", "IFF", "LPAREN", 
			"RPAREN", "COMMA", "DOT", "VARIABLE", "CONSTANT", "PREDICATE", "FUNCTION", 
			"WS", "COMMENT", "LINE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public FOkLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "FOkLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\24\u009e\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2/\n\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\38\n\3\3\4\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\3\5\5\5C\n\5\3\6"+
		"\3\6\3\6\3\6\5\6I\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7S\n\7\3\b\3\b"+
		"\3\b\3\b\5\bY\n\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\7\re\n\r\f"+
		"\r\16\rh\13\r\3\16\3\16\7\16l\n\16\f\16\16\16o\13\16\3\17\3\17\7\17s\n"+
		"\17\f\17\16\17v\13\17\3\20\3\20\7\20z\n\20\f\20\16\20}\13\20\3\21\6\21"+
		"\u0080\n\21\r\21\16\21\u0081\3\21\3\21\3\22\3\22\3\22\3\22\7\22\u008a"+
		"\n\22\f\22\16\22\u008d\13\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3"+
		"\23\7\23\u0098\n\23\f\23\16\23\u009b\13\23\3\23\3\23\3\u008b\2\24\3\3"+
		"\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\3\2\7\3\2c|\6\2\62;C\\aac|\3\2C\\\5\2\13\f\17\17\"\"\4\2"+
		"\f\f\17\17\2\u00ab\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\3.\3\2\2\2\5\67\3\2\2\2\7=\3\2\2\2\tB"+
		"\3\2\2\2\13H\3\2\2\2\rR\3\2\2\2\17X\3\2\2\2\21Z\3\2\2\2\23\\\3\2\2\2\25"+
		"^\3\2\2\2\27`\3\2\2\2\31b\3\2\2\2\33i\3\2\2\2\35p\3\2\2\2\37w\3\2\2\2"+
		"!\177\3\2\2\2#\u0085\3\2\2\2%\u0093\3\2\2\2\'(\7h\2\2()\7q\2\2)*\7t\2"+
		"\2*+\7c\2\2+,\7n\2\2,/\7n\2\2-/\7\u2202\2\2.\'\3\2\2\2.-\3\2\2\2/\4\3"+
		"\2\2\2\60\61\7g\2\2\61\62\7z\2\2\62\63\7k\2\2\63\64\7u\2\2\64\65\7v\2"+
		"\2\658\7u\2\2\668\7\u2205\2\2\67\60\3\2\2\2\67\66\3\2\2\28\6\3\2\2\29"+
		":\7c\2\2:;\7p\2\2;>\7f\2\2<>\7\u2229\2\2=9\3\2\2\2=<\3\2\2\2>\b\3\2\2"+
		"\2?@\7q\2\2@C\7t\2\2AC\7\u222a\2\2B?\3\2\2\2BA\3\2\2\2C\n\3\2\2\2DE\7"+
		"p\2\2EF\7q\2\2FI\7v\2\2GI\7\u00ae\2\2HD\3\2\2\2HG\3\2\2\2I\f\3\2\2\2J"+
		"K\7k\2\2KL\7o\2\2LM\7r\2\2MN\7n\2\2NO\7k\2\2OP\7g\2\2PS\7u\2\2QS\7\u2194"+
		"\2\2RJ\3\2\2\2RQ\3\2\2\2S\16\3\2\2\2TU\7k\2\2UV\7h\2\2VY\7h\2\2WY\7\u2196"+
		"\2\2XT\3\2\2\2XW\3\2\2\2Y\20\3\2\2\2Z[\7*\2\2[\22\3\2\2\2\\]\7+\2\2]\24"+
		"\3\2\2\2^_\7.\2\2_\26\3\2\2\2`a\7\60\2\2a\30\3\2\2\2bf\t\2\2\2ce\t\3\2"+
		"\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2g\32\3\2\2\2hf\3\2\2\2im\t\4"+
		"\2\2jl\t\3\2\2kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2n\34\3\2\2\2om\3"+
		"\2\2\2pt\t\4\2\2qs\t\3\2\2rq\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2u\36"+
		"\3\2\2\2vt\3\2\2\2w{\t\2\2\2xz\t\3\2\2yx\3\2\2\2z}\3\2\2\2{y\3\2\2\2{"+
		"|\3\2\2\2| \3\2\2\2}{\3\2\2\2~\u0080\t\5\2\2\177~\3\2\2\2\u0080\u0081"+
		"\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0083\3\2\2\2\u0083"+
		"\u0084\b\21\2\2\u0084\"\3\2\2\2\u0085\u0086\7\61\2\2\u0086\u0087\7,\2"+
		"\2\u0087\u008b\3\2\2\2\u0088\u008a\13\2\2\2\u0089\u0088\3\2\2\2\u008a"+
		"\u008d\3\2\2\2\u008b\u008c\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u008e\3\2"+
		"\2\2\u008d\u008b\3\2\2\2\u008e\u008f\7,\2\2\u008f\u0090\7\61\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\u0092\b\22\2\2\u0092$\3\2\2\2\u0093\u0094\7\61\2"+
		"\2\u0094\u0095\7\61\2\2\u0095\u0099\3\2\2\2\u0096\u0098\n\6\2\2\u0097"+
		"\u0096\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2"+
		"\2\2\u009a\u009c\3\2\2\2\u009b\u0099\3\2\2\2\u009c\u009d\b\23\2\2\u009d"+
		"&\3\2\2\2\21\2.\67=BHRXfmt{\u0081\u008b\u0099\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}