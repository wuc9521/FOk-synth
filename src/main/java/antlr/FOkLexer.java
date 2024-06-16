// Generated from src/main/g4/FOkLexer.g4 by ANTLR 4.9.1
package antlr;
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
		LPAREN=1, RPAREN=2, EQUALS=3, TRUE=4, FALSE=5, FORALL=6, EXISTS=7, AND=8, 
		OR=9, IMPLIES=10, IFF=11, NOT=12, CONST=13, FUNC=14, RELATION=15, VARIABLE=16, 
		DOT=17, COMMA=18, ENDLINE=19, WS=20, COMMENT=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LPAREN", "RPAREN", "EQUALS", "TRUE", "FALSE", "FORALL", "EXISTS", "AND", 
			"OR", "IMPLIES", "IFF", "NOT", "CONST", "FUNC", "RELATION", "VARIABLE", 
			"DOT", "COMMA", "ENDLINE", "WS", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'='", "'$T'", "'$F'", null, null, "'&'", "'|'", 
			"'->'", "'<->'", "'~'", null, null, null, null, "'.'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LPAREN", "RPAREN", "EQUALS", "TRUE", "FALSE", "FORALL", "EXISTS", 
			"AND", "OR", "IMPLIES", "IFF", "NOT", "CONST", "FUNC", "RELATION", "VARIABLE", 
			"DOT", "COMMA", "ENDLINE", "WS", "COMMENT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u008e\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\6\16W\n\16\r\16\16\16X\3\17\3\17\6\17]\n\17\r\17\16\17^\3\20\6\20"+
		"b\n\20\r\20\16\20c\3\21\3\21\7\21h\n\21\f\21\16\21k\13\21\3\22\3\22\3"+
		"\23\3\23\3\24\6\24r\n\24\r\24\16\24s\3\24\3\24\3\25\6\25y\n\25\r\25\16"+
		"\25z\3\25\3\25\3\26\3\26\3\26\3\26\7\26\u0083\n\26\f\26\16\26\u0086\13"+
		"\26\3\26\5\26\u0089\n\26\3\26\3\26\3\26\3\26\2\2\27\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'"+
		"\25)\26+\27\3\2\22\4\2HHhh\4\2QQqq\4\2TTtt\4\2CCcc\4\2NNnn\4\2GGgg\4\2"+
		"ZZzz\4\2KKkk\4\2UUuu\4\2VVvv\5\2\62;C\\c|\4\2\62;C\\\4\2C\\c|\4\2\f\f"+
		"\17\17\4\2\13\13\"\"\4\2\13\f\17\17\2\u0095\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2"+
		"\2\2)\3\2\2\2\2+\3\2\2\2\3-\3\2\2\2\5/\3\2\2\2\7\61\3\2\2\2\t\63\3\2\2"+
		"\2\13\66\3\2\2\2\r9\3\2\2\2\17@\3\2\2\2\21G\3\2\2\2\23I\3\2\2\2\25K\3"+
		"\2\2\2\27N\3\2\2\2\31R\3\2\2\2\33T\3\2\2\2\35Z\3\2\2\2\37a\3\2\2\2!e\3"+
		"\2\2\2#l\3\2\2\2%n\3\2\2\2\'q\3\2\2\2)x\3\2\2\2+~\3\2\2\2-.\7*\2\2.\4"+
		"\3\2\2\2/\60\7+\2\2\60\6\3\2\2\2\61\62\7?\2\2\62\b\3\2\2\2\63\64\7&\2"+
		"\2\64\65\7V\2\2\65\n\3\2\2\2\66\67\7&\2\2\678\7H\2\28\f\3\2\2\29:\t\2"+
		"\2\2:;\t\3\2\2;<\t\4\2\2<=\t\5\2\2=>\t\6\2\2>?\t\6\2\2?\16\3\2\2\2@A\t"+
		"\7\2\2AB\t\b\2\2BC\t\t\2\2CD\t\n\2\2DE\t\13\2\2EF\t\n\2\2F\20\3\2\2\2"+
		"GH\7(\2\2H\22\3\2\2\2IJ\7~\2\2J\24\3\2\2\2KL\7/\2\2LM\7@\2\2M\26\3\2\2"+
		"\2NO\7>\2\2OP\7/\2\2PQ\7@\2\2Q\30\3\2\2\2RS\7\u0080\2\2S\32\3\2\2\2TV"+
		"\7%\2\2UW\t\f\2\2VU\3\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\34\3\2\2\2"+
		"Z\\\7a\2\2[]\t\f\2\2\\[\3\2\2\2]^\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_\36\3\2"+
		"\2\2`b\t\r\2\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2d \3\2\2\2ei\t\16"+
		"\2\2fh\t\f\2\2gf\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2j\"\3\2\2\2ki\3"+
		"\2\2\2lm\7\60\2\2m$\3\2\2\2no\7.\2\2o&\3\2\2\2pr\t\17\2\2qp\3\2\2\2rs"+
		"\3\2\2\2sq\3\2\2\2st\3\2\2\2tu\3\2\2\2uv\b\24\2\2v(\3\2\2\2wy\t\20\2\2"+
		"xw\3\2\2\2yz\3\2\2\2zx\3\2\2\2z{\3\2\2\2{|\3\2\2\2|}\b\25\2\2}*\3\2\2"+
		"\2~\177\7\61\2\2\177\u0080\7\61\2\2\u0080\u0084\3\2\2\2\u0081\u0083\n"+
		"\21\2\2\u0082\u0081\3\2\2\2\u0083\u0086\3\2\2\2\u0084\u0082\3\2\2\2\u0084"+
		"\u0085\3\2\2\2\u0085\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0087\u0089\7\17"+
		"\2\2\u0088\u0087\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"\u008b\7\f\2\2\u008b\u008c\3\2\2\2\u008c\u008d\b\26\2\2\u008d,\3\2\2\2"+
		"\13\2X^cisz\u0084\u0088\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}