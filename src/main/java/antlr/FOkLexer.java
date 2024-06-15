// Generated from src/main/ag/FOkLexer.g4 by ANTLR 4.9.1
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
			null, "'('", "')'", "'='", null, null, null, null, "'&'", "'|'", "'->'", 
			"'<->'", "'~'", null, null, null, null, "'.'", "','"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u0093\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\6\16\\\n\16\r\16\16\16]\3\17\3\17\6\17b\n\17"+
		"\r\17\16\17c\3\20\6\20g\n\20\r\20\16\20h\3\21\3\21\7\21m\n\21\f\21\16"+
		"\21p\13\21\3\22\3\22\3\23\3\23\3\24\6\24w\n\24\r\24\16\24x\3\24\3\24\3"+
		"\25\6\25~\n\25\r\25\16\25\177\3\25\3\25\3\26\3\26\3\26\3\26\7\26\u0088"+
		"\n\26\f\26\16\26\u008b\13\26\3\26\5\26\u008e\n\26\3\26\3\26\3\26\3\26"+
		"\2\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\26+\27\3\2\23\4\2VVvv\4\2TTtt\4\2WWww\4"+
		"\2GGgg\4\2HHhh\4\2CCcc\4\2NNnn\4\2UUuu\4\2QQqq\4\2ZZzz\4\2KKkk\5\2\62"+
		";C\\c|\4\2\62;C\\\4\2C\\c|\4\2\f\f\17\17\4\2\13\13\"\"\4\2\13\f\17\17"+
		"\2\u009a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\3-\3\2\2\2"+
		"\5/\3\2\2\2\7\61\3\2\2\2\t\63\3\2\2\2\138\3\2\2\2\r>\3\2\2\2\17E\3\2\2"+
		"\2\21L\3\2\2\2\23N\3\2\2\2\25P\3\2\2\2\27S\3\2\2\2\31W\3\2\2\2\33Y\3\2"+
		"\2\2\35_\3\2\2\2\37f\3\2\2\2!j\3\2\2\2#q\3\2\2\2%s\3\2\2\2\'v\3\2\2\2"+
		")}\3\2\2\2+\u0083\3\2\2\2-.\7*\2\2.\4\3\2\2\2/\60\7+\2\2\60\6\3\2\2\2"+
		"\61\62\7?\2\2\62\b\3\2\2\2\63\64\t\2\2\2\64\65\t\3\2\2\65\66\t\4\2\2\66"+
		"\67\t\5\2\2\67\n\3\2\2\289\t\6\2\29:\t\7\2\2:;\t\b\2\2;<\t\t\2\2<=\t\5"+
		"\2\2=\f\3\2\2\2>?\t\6\2\2?@\t\n\2\2@A\t\3\2\2AB\t\7\2\2BC\t\b\2\2CD\t"+
		"\b\2\2D\16\3\2\2\2EF\t\5\2\2FG\t\13\2\2GH\t\f\2\2HI\t\t\2\2IJ\t\2\2\2"+
		"JK\t\t\2\2K\20\3\2\2\2LM\7(\2\2M\22\3\2\2\2NO\7~\2\2O\24\3\2\2\2PQ\7/"+
		"\2\2QR\7@\2\2R\26\3\2\2\2ST\7>\2\2TU\7/\2\2UV\7@\2\2V\30\3\2\2\2WX\7\u0080"+
		"\2\2X\32\3\2\2\2Y[\7%\2\2Z\\\t\r\2\2[Z\3\2\2\2\\]\3\2\2\2][\3\2\2\2]^"+
		"\3\2\2\2^\34\3\2\2\2_a\7a\2\2`b\t\r\2\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2"+
		"cd\3\2\2\2d\36\3\2\2\2eg\t\16\2\2fe\3\2\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2"+
		"\2\2i \3\2\2\2jn\t\17\2\2km\t\r\2\2lk\3\2\2\2mp\3\2\2\2nl\3\2\2\2no\3"+
		"\2\2\2o\"\3\2\2\2pn\3\2\2\2qr\7\60\2\2r$\3\2\2\2st\7.\2\2t&\3\2\2\2uw"+
		"\t\20\2\2vu\3\2\2\2wx\3\2\2\2xv\3\2\2\2xy\3\2\2\2yz\3\2\2\2z{\b\24\2\2"+
		"{(\3\2\2\2|~\t\21\2\2}|\3\2\2\2~\177\3\2\2\2\177}\3\2\2\2\177\u0080\3"+
		"\2\2\2\u0080\u0081\3\2\2\2\u0081\u0082\b\25\2\2\u0082*\3\2\2\2\u0083\u0084"+
		"\7\61\2\2\u0084\u0085\7\61\2\2\u0085\u0089\3\2\2\2\u0086\u0088\n\22\2"+
		"\2\u0087\u0086\3\2\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a"+
		"\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u008e\7\17\2\2"+
		"\u008d\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0090"+
		"\7\f\2\2\u0090\u0091\3\2\2\2\u0091\u0092\b\26\2\2\u0092,\3\2\2\2\13\2"+
		"]chnx\177\u0089\u008d\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}