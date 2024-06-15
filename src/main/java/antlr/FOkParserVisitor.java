// Generated from src/main/ag/FOkParser.g4 by ANTLR 4.9.1
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FOkParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FOkParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FOkParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(FOkParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link FOkParser#sentence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentence(FOkParser.SentenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FOkParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormula(FOkParser.FormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link FOkParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(FOkParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link FOkParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(FOkParser.ValueContext ctx);
}