// Generated from src/main/g4/FOkParser.g4 by ANTLR 4.9.1
package antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FOkParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LPAREN=1, RPAREN=2, EQUALS=3, TRUE=4, FALSE=5, FORALL=6, EXISTS=7, AND=8, 
		OR=9, IMPLIES=10, IFF=11, NOT=12, CONST=13, FUNC=14, RELATION=15, VARIABLE=16, 
		DOT=17, COMMA=18, ENDLINE=19, WS=20, COMMENT=21;
	public static final int
		RULE_prog = 0, RULE_sentence = 1, RULE_formula = 2, RULE_term = 3, RULE_value = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "sentence", "formula", "term", "value"
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

	@Override
	public String getGrammarFileName() { return "FOkParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FOkParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(FOkParser.EOF, 0); }
		public TerminalNode COMMENT() { return getToken(FOkParser.COMMENT, 0); }
		public List<SentenceContext> sentence() {
			return getRuleContexts(SentenceContext.class);
		}
		public SentenceContext sentence(int i) {
			return getRuleContext(SentenceContext.class,i);
		}
		public TerminalNode ENDLINE() { return getToken(FOkParser.ENDLINE, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FOkParserVisitor ) return ((FOkParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(10);
				match(COMMENT);
				}
			}

			setState(14); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(13);
				sentence();
				}
				}
				setState(16); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << TRUE) | (1L << FALSE) | (1L << FORALL) | (1L << EXISTS) | (1L << NOT) | (1L << CONST) | (1L << FUNC) | (1L << RELATION) | (1L << VARIABLE))) != 0) );
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ENDLINE) {
				{
				setState(18);
				match(ENDLINE);
				}
			}

			setState(21);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenceContext extends ParserRuleContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public SentenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentence; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FOkParserVisitor ) return ((FOkParserVisitor<? extends T>)visitor).visitSentence(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenceContext sentence() throws RecognitionException {
		SentenceContext _localctx = new SentenceContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sentence);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			formula(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormulaContext extends ParserRuleContext {
		public Token qop;
		public Token op;
		public TerminalNode NOT() { return getToken(FOkParser.NOT, 0); }
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public TerminalNode VARIABLE() { return getToken(FOkParser.VARIABLE, 0); }
		public TerminalNode DOT() { return getToken(FOkParser.DOT, 0); }
		public TerminalNode LPAREN() { return getToken(FOkParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(FOkParser.RPAREN, 0); }
		public TerminalNode FORALL() { return getToken(FOkParser.FORALL, 0); }
		public TerminalNode EXISTS() { return getToken(FOkParser.EXISTS, 0); }
		public TerminalNode RELATION() { return getToken(FOkParser.RELATION, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(FOkParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(FOkParser.COMMA, i);
		}
		public TerminalNode EQUALS() { return getToken(FOkParser.EQUALS, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode IFF() { return getToken(FOkParser.IFF, 0); }
		public TerminalNode IMPLIES() { return getToken(FOkParser.IMPLIES, 0); }
		public TerminalNode AND() { return getToken(FOkParser.AND, 0); }
		public TerminalNode OR() { return getToken(FOkParser.OR, 0); }
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FOkParserVisitor ) return ((FOkParserVisitor<? extends T>)visitor).visitFormula(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		return formula(0);
	}

	private FormulaContext formula(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FormulaContext _localctx = new FormulaContext(_ctx, _parentState);
		FormulaContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_formula, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NOT:
				{
				setState(26);
				match(NOT);
				setState(27);
				formula(7);
				}
				break;
			case FORALL:
			case EXISTS:
				{
				setState(28);
				((FormulaContext)_localctx).qop = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==FORALL || _la==EXISTS) ) {
					((FormulaContext)_localctx).qop = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(29);
				match(VARIABLE);
				setState(30);
				match(DOT);
				setState(31);
				match(LPAREN);
				setState(32);
				formula(0);
				setState(33);
				match(RPAREN);
				}
				break;
			case RELATION:
				{
				setState(35);
				match(RELATION);
				setState(47);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(36);
					match(LPAREN);
					setState(37);
					term();
					setState(42);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(38);
						match(COMMA);
						setState(39);
						term();
						}
						}
						setState(44);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(45);
					match(RPAREN);
					}
					break;
				}
				}
				break;
			case CONST:
			case FUNC:
			case VARIABLE:
				{
				setState(49);
				term();
				setState(50);
				match(EQUALS);
				setState(51);
				term();
				}
				break;
			case TRUE:
			case FALSE:
				{
				setState(53);
				value();
				}
				break;
			case LPAREN:
				{
				setState(54);
				match(LPAREN);
				setState(55);
				formula(0);
				setState(56);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(65);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new FormulaContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_formula);
					setState(60);
					if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
					setState(61);
					((FormulaContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AND) | (1L << OR) | (1L << IMPLIES) | (1L << IFF))) != 0)) ) {
						((FormulaContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(62);
					formula(7);
					}
					} 
				}
				setState(67);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TerminalNode FUNC() { return getToken(FOkParser.FUNC, 0); }
		public TerminalNode LPAREN() { return getToken(FOkParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(FOkParser.RPAREN, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(FOkParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(FOkParser.COMMA, i);
		}
		public TerminalNode CONST() { return getToken(FOkParser.CONST, 0); }
		public TerminalNode VARIABLE() { return getToken(FOkParser.VARIABLE, 0); }
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FOkParserVisitor ) return ((FOkParserVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_term);
		int _la;
		try {
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FUNC:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				match(FUNC);
				setState(69);
				match(LPAREN);
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONST) | (1L << FUNC) | (1L << VARIABLE))) != 0)) {
					{
					setState(70);
					term();
					setState(75);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(71);
						match(COMMA);
						setState(72);
						term();
						}
						}
						setState(77);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(80);
				match(RPAREN);
				}
				break;
			case CONST:
				enterOuterAlt(_localctx, 2);
				{
				setState(81);
				match(CONST);
				}
				break;
			case VARIABLE:
				enterOuterAlt(_localctx, 3);
				{
				setState(82);
				match(VARIABLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(FOkParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(FOkParser.FALSE, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FOkParserVisitor ) return ((FOkParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return formula_sempred((FormulaContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean formula_sempred(FormulaContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27Z\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\5\2\16\n\2\3\2\6\2\21\n\2\r\2\16\2\22\3"+
		"\2\5\2\26\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\7\4+\n\4\f\4\16\4.\13\4\3\4\3\4\5\4\62\n\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4=\n\4\3\4\3\4\3\4\7\4B\n\4\f\4\16\4"+
		"E\13\4\3\5\3\5\3\5\3\5\3\5\7\5L\n\5\f\5\16\5O\13\5\5\5Q\n\5\3\5\3\5\3"+
		"\5\5\5V\n\5\3\6\3\6\3\6\2\3\6\7\2\4\6\b\n\2\5\3\2\b\t\3\2\n\r\3\2\6\7"+
		"\2c\2\r\3\2\2\2\4\31\3\2\2\2\6<\3\2\2\2\bU\3\2\2\2\nW\3\2\2\2\f\16\7\27"+
		"\2\2\r\f\3\2\2\2\r\16\3\2\2\2\16\20\3\2\2\2\17\21\5\4\3\2\20\17\3\2\2"+
		"\2\21\22\3\2\2\2\22\20\3\2\2\2\22\23\3\2\2\2\23\25\3\2\2\2\24\26\7\25"+
		"\2\2\25\24\3\2\2\2\25\26\3\2\2\2\26\27\3\2\2\2\27\30\7\2\2\3\30\3\3\2"+
		"\2\2\31\32\5\6\4\2\32\5\3\2\2\2\33\34\b\4\1\2\34\35\7\16\2\2\35=\5\6\4"+
		"\t\36\37\t\2\2\2\37 \7\22\2\2 !\7\23\2\2!\"\7\3\2\2\"#\5\6\4\2#$\7\4\2"+
		"\2$=\3\2\2\2%\61\7\21\2\2&\'\7\3\2\2\',\5\b\5\2()\7\24\2\2)+\5\b\5\2*"+
		"(\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2\2-/\3\2\2\2.,\3\2\2\2/\60\7\4\2"+
		"\2\60\62\3\2\2\2\61&\3\2\2\2\61\62\3\2\2\2\62=\3\2\2\2\63\64\5\b\5\2\64"+
		"\65\7\5\2\2\65\66\5\b\5\2\66=\3\2\2\2\67=\5\n\6\289\7\3\2\29:\5\6\4\2"+
		":;\7\4\2\2;=\3\2\2\2<\33\3\2\2\2<\36\3\2\2\2<%\3\2\2\2<\63\3\2\2\2<\67"+
		"\3\2\2\2<8\3\2\2\2=C\3\2\2\2>?\f\b\2\2?@\t\3\2\2@B\5\6\4\tA>\3\2\2\2B"+
		"E\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\7\3\2\2\2EC\3\2\2\2FG\7\20\2\2GP\7\3\2"+
		"\2HM\5\b\5\2IJ\7\24\2\2JL\5\b\5\2KI\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2"+
		"\2\2NQ\3\2\2\2OM\3\2\2\2PH\3\2\2\2PQ\3\2\2\2QR\3\2\2\2RV\7\4\2\2SV\7\17"+
		"\2\2TV\7\22\2\2UF\3\2\2\2US\3\2\2\2UT\3\2\2\2V\t\3\2\2\2WX\t\4\2\2X\13"+
		"\3\2\2\2\f\r\22\25,\61<CMPU";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}