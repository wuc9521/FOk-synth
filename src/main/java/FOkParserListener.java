// Generated from src/main/ag/FOkParser.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FOkParser}.
 */
public interface FOkParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FOkParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(FOkParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(FOkParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#sentence}.
	 * @param ctx the parse tree
	 */
	void enterSentence(FOkParser.SentenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#sentence}.
	 * @param ctx the parse tree
	 */
	void exitSentence(FOkParser.SentenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(FOkParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(FOkParser.FormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(FOkParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(FOkParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(FOkParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(FOkParser.ValueContext ctx);
}