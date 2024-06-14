// Generated from src/main/ag/FOkParser.g4 by ANTLR 4.9.1
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
		FORALL=1, EXISTS=2, AND=3, OR=4, NOT=5, IMPLIES=6, IFF=7, LPAREN=8, RPAREN=9, 
		COMMA=10, DOT=11, VARIABLE=12, CONSTANT=13, PREDICATE=14, FUNCTION=15, 
		WS=16, COMMENT=17, LINE_COMMENT=18;
	public static final int
		RULE_formula = 0, RULE_implication = 1, RULE_disjunction = 2, RULE_conjunction = 3, 
		RULE_negation = 4, RULE_atom = 5, RULE_predicate = 6, RULE_termList = 7, 
		RULE_term = 8, RULE_quantifier = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"formula", "implication", "disjunction", "conjunction", "negation", "atom", 
			"predicate", "termList", "term", "quantifier"
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

	public static class FormulaContext extends ParserRuleContext {
		public ImplicationContext implication() {
			return getRuleContext(ImplicationContext.class,0);
		}
		public TerminalNode EOF() { return getToken(FOkParser.EOF, 0); }
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitFormula(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_formula);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			implication();
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

	public static class ImplicationContext extends ParserRuleContext {
		public List<DisjunctionContext> disjunction() {
			return getRuleContexts(DisjunctionContext.class);
		}
		public DisjunctionContext disjunction(int i) {
			return getRuleContext(DisjunctionContext.class,i);
		}
		public List<TerminalNode> IMPLIES() { return getTokens(FOkParser.IMPLIES); }
		public TerminalNode IMPLIES(int i) {
			return getToken(FOkParser.IMPLIES, i);
		}
		public ImplicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implication; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterImplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitImplication(this);
		}
	}

	public final ImplicationContext implication() throws RecognitionException {
		ImplicationContext _localctx = new ImplicationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_implication);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			disjunction();
			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPLIES) {
				{
				{
				setState(24);
				match(IMPLIES);
				setState(25);
				disjunction();
				}
				}
				setState(30);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class DisjunctionContext extends ParserRuleContext {
		public List<ConjunctionContext> conjunction() {
			return getRuleContexts(ConjunctionContext.class);
		}
		public ConjunctionContext conjunction(int i) {
			return getRuleContext(ConjunctionContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(FOkParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(FOkParser.OR, i);
		}
		public DisjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterDisjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitDisjunction(this);
		}
	}

	public final DisjunctionContext disjunction() throws RecognitionException {
		DisjunctionContext _localctx = new DisjunctionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			conjunction();
			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(32);
				match(OR);
				setState(33);
				conjunction();
				}
				}
				setState(38);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class ConjunctionContext extends ParserRuleContext {
		public List<NegationContext> negation() {
			return getRuleContexts(NegationContext.class);
		}
		public NegationContext negation(int i) {
			return getRuleContext(NegationContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(FOkParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(FOkParser.AND, i);
		}
		public ConjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterConjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitConjunction(this);
		}
	}

	public final ConjunctionContext conjunction() throws RecognitionException {
		ConjunctionContext _localctx = new ConjunctionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_conjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			negation();
			setState(44);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(40);
				match(AND);
				setState(41);
				negation();
				}
				}
				setState(46);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class NegationContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(FOkParser.NOT, 0); }
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public NegationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitNegation(this);
		}
	}

	public final NegationContext negation() throws RecognitionException {
		NegationContext _localctx = new NegationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_negation);
		try {
			setState(50);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(47);
				match(NOT);
				setState(48);
				negation();
				}
				break;
			case FORALL:
			case EXISTS:
			case LPAREN:
			case PREDICATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				atom();
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

	public static class AtomContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(FOkParser.LPAREN, 0); }
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(FOkParser.RPAREN, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public QuantifierContext quantifier() {
			return getRuleContext(QuantifierContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atom);
		try {
			setState(58);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				match(LPAREN);
				setState(53);
				formula();
				setState(54);
				match(RPAREN);
				}
				break;
			case PREDICATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(56);
				predicate();
				}
				break;
			case FORALL:
			case EXISTS:
				enterOuterAlt(_localctx, 3);
				{
				setState(57);
				quantifier();
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

	public static class PredicateContext extends ParserRuleContext {
		public TerminalNode PREDICATE() { return getToken(FOkParser.PREDICATE, 0); }
		public TerminalNode LPAREN() { return getToken(FOkParser.LPAREN, 0); }
		public TermListContext termList() {
			return getRuleContext(TermListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(FOkParser.RPAREN, 0); }
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitPredicate(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(PREDICATE);
			setState(61);
			match(LPAREN);
			setState(62);
			termList();
			setState(63);
			match(RPAREN);
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

	public static class TermListContext extends ParserRuleContext {
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
		public TermListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterTermList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitTermList(this);
		}
	}

	public final TermListContext termList() throws RecognitionException {
		TermListContext _localctx = new TermListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_termList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			term();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(66);
				match(COMMA);
				setState(67);
				term();
				}
				}
				setState(72);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class TermContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(FOkParser.VARIABLE, 0); }
		public TerminalNode CONSTANT() { return getToken(FOkParser.CONSTANT, 0); }
		public TerminalNode FUNCTION() { return getToken(FOkParser.FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(FOkParser.LPAREN, 0); }
		public TermListContext termList() {
			return getRuleContext(TermListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(FOkParser.RPAREN, 0); }
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_term);
		try {
			setState(80);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VARIABLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(73);
				match(VARIABLE);
				}
				break;
			case CONSTANT:
				enterOuterAlt(_localctx, 2);
				{
				setState(74);
				match(CONSTANT);
				}
				break;
			case FUNCTION:
				enterOuterAlt(_localctx, 3);
				{
				setState(75);
				match(FUNCTION);
				setState(76);
				match(LPAREN);
				setState(77);
				termList();
				setState(78);
				match(RPAREN);
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

	public static class QuantifierContext extends ParserRuleContext {
		public TerminalNode FORALL() { return getToken(FOkParser.FORALL, 0); }
		public TerminalNode VARIABLE() { return getToken(FOkParser.VARIABLE, 0); }
		public TerminalNode DOT() { return getToken(FOkParser.DOT, 0); }
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode EXISTS() { return getToken(FOkParser.EXISTS, 0); }
		public QuantifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).enterQuantifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FOkParserListener ) ((FOkParserListener)listener).exitQuantifier(this);
		}
	}

	public final QuantifierContext quantifier() throws RecognitionException {
		QuantifierContext _localctx = new QuantifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_quantifier);
		try {
			setState(90);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORALL:
				enterOuterAlt(_localctx, 1);
				{
				setState(82);
				match(FORALL);
				setState(83);
				match(VARIABLE);
				setState(84);
				match(DOT);
				setState(85);
				formula();
				}
				break;
			case EXISTS:
				enterOuterAlt(_localctx, 2);
				{
				setState(86);
				match(EXISTS);
				setState(87);
				match(VARIABLE);
				setState(88);
				match(DOT);
				setState(89);
				formula();
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\24_\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\7\3\35\n\3\f\3\16\3 \13\3\3\4\3\4\3\4\7\4%\n\4"+
		"\f\4\16\4(\13\4\3\5\3\5\3\5\7\5-\n\5\f\5\16\5\60\13\5\3\6\3\6\3\6\5\6"+
		"\65\n\6\3\7\3\7\3\7\3\7\3\7\3\7\5\7=\n\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\7\tG\n\t\f\t\16\tJ\13\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\nS\n\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13]\n\13\3\13\2\2\f\2\4\6\b\n\f\16"+
		"\20\22\24\2\2\2^\2\26\3\2\2\2\4\31\3\2\2\2\6!\3\2\2\2\b)\3\2\2\2\n\64"+
		"\3\2\2\2\f<\3\2\2\2\16>\3\2\2\2\20C\3\2\2\2\22R\3\2\2\2\24\\\3\2\2\2\26"+
		"\27\5\4\3\2\27\30\7\2\2\3\30\3\3\2\2\2\31\36\5\6\4\2\32\33\7\b\2\2\33"+
		"\35\5\6\4\2\34\32\3\2\2\2\35 \3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37\5"+
		"\3\2\2\2 \36\3\2\2\2!&\5\b\5\2\"#\7\6\2\2#%\5\b\5\2$\"\3\2\2\2%(\3\2\2"+
		"\2&$\3\2\2\2&\'\3\2\2\2\'\7\3\2\2\2(&\3\2\2\2).\5\n\6\2*+\7\5\2\2+-\5"+
		"\n\6\2,*\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\t\3\2\2\2\60.\3\2\2"+
		"\2\61\62\7\7\2\2\62\65\5\n\6\2\63\65\5\f\7\2\64\61\3\2\2\2\64\63\3\2\2"+
		"\2\65\13\3\2\2\2\66\67\7\n\2\2\678\5\2\2\289\7\13\2\29=\3\2\2\2:=\5\16"+
		"\b\2;=\5\24\13\2<\66\3\2\2\2<:\3\2\2\2<;\3\2\2\2=\r\3\2\2\2>?\7\20\2\2"+
		"?@\7\n\2\2@A\5\20\t\2AB\7\13\2\2B\17\3\2\2\2CH\5\22\n\2DE\7\f\2\2EG\5"+
		"\22\n\2FD\3\2\2\2GJ\3\2\2\2HF\3\2\2\2HI\3\2\2\2I\21\3\2\2\2JH\3\2\2\2"+
		"KS\7\16\2\2LS\7\17\2\2MN\7\21\2\2NO\7\n\2\2OP\5\20\t\2PQ\7\13\2\2QS\3"+
		"\2\2\2RK\3\2\2\2RL\3\2\2\2RM\3\2\2\2S\23\3\2\2\2TU\7\3\2\2UV\7\16\2\2"+
		"VW\7\r\2\2W]\5\2\2\2XY\7\4\2\2YZ\7\16\2\2Z[\7\r\2\2[]\5\2\2\2\\T\3\2\2"+
		"\2\\X\3\2\2\2]\25\3\2\2\2\n\36&.\64<HR\\";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}