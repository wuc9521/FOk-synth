// Generated from src/main/ag/FOkParser.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FOkParser}.
 */
public interface FOkParserListener extends ParseTreeListener {
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
	 * Enter a parse tree produced by {@link FOkParser#implication}.
	 * @param ctx the parse tree
	 */
	void enterImplication(FOkParser.ImplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#implication}.
	 * @param ctx the parse tree
	 */
	void exitImplication(FOkParser.ImplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(FOkParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(FOkParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(FOkParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(FOkParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(FOkParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(FOkParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(FOkParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(FOkParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(FOkParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(FOkParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link FOkParser#termList}.
	 * @param ctx the parse tree
	 */
	void enterTermList(FOkParser.TermListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#termList}.
	 * @param ctx the parse tree
	 */
	void exitTermList(FOkParser.TermListContext ctx);
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
	 * Enter a parse tree produced by {@link FOkParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void enterQuantifier(FOkParser.QuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FOkParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void exitQuantifier(FOkParser.QuantifierContext ctx);
}