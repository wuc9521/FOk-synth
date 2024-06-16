package visitors;
import antlr.*;
import antlr.FOkParser.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*; 

public class FOkVisitor extends FOkParserBaseVisitor<Void> {
    private boolean value = false;
    @Override
    public Void visit(ParseTree tree) {
        return tree.accept(this);
    }

    @Override
    public Void visitChildren(RuleNode node) {
        return super.visitChildren(node); // continue the traversal
    }

    @Override
    public Void visitProg(ProgContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitSentence(SentenceContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitFormula(FormulaContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * @param ctx The context of the term.
     *            term
     *            1 : FUNC LPAREN term (COMMA term)* RPAREN
     *            2 | VARIABLE
     *            ;
     */
    @Override
    public Void visitTerm(TermContext ctx) {
        if (ctx.getChild(0) == ctx.FUNC()) { // case 1
        } else if (ctx.getChild(0) == ctx.VARIABLE()) { // case 2
        }
        return visitChildren(ctx);
    }

    /**
     * @param ctx The context of the value.
     */
    @Override
    public Void visitValue(ValueContext ctx) {
        assert ctx.getRuleIndex() == FOkParser.TRUE || ctx.getRuleIndex() == FOkParser.FALSE;
        return visitChildren(ctx);
    }

    /**
     * @param ctx The context of the formula.
     *            formula
     *            1 : NOT formula
     *            2 | formula op=(IFF | IMPLIES | AND | OR) formula
     *            3 | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     *            4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *            5 | term EQUALS term
     *            6 | value
     *            7 | LPAREN formula RPAREN
     *            ;
     */
    private boolean calFormulaVal(ParserRuleContext ctx) {
        if (ctx.getChild(0) == ((FormulaContext)ctx).NOT()) { // case 1
            return !calFormulaVal(((FormulaContext)ctx).formula(0));
        } else if (ctx.getChild(0) == ((FormulaContext)ctx).LPAREN()) { // case 7
            return calFormulaVal(((FormulaContext)ctx).formula(0));
        } else if (((FormulaContext)ctx).qop != null) { // case 3
            switch (((FormulaContext)ctx).qop.getType()) {
                case FOkParser.FORALL:
                    // 这里不得不涉及到 structure 了
                    break;
                case FOkParser.EXISTS:
                    break;
                default:
                    break;
            }
        } else if (ctx.getChild(0) == ((FormulaContext)ctx).RELATION()) { // case 4
            for (int i = 0; i < ctx.getChildCount(); i++) { // discuss about the arity of the relation
                System.out.println(ctx.getChild(i));
            }
        } else if (ctx.getChild(0) == ((FormulaContext)ctx).term()) { // case 5
        } else if (ctx.getChild(0) == ((FormulaContext)ctx).value()) { // case 6
            return ((ValueContext)ctx).getRuleIndex() == FOkParser.TRUE;
        } else if (((FormulaContext)ctx).op != null) { // case 2
        }
        return false;
    }
}
