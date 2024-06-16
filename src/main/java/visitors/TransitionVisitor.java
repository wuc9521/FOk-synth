package visitors;

import java.util.ArrayList;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import lombok.Getter;
import lombok.Setter;
import antlr.FOkParser.*;


/**
 * This class is a helper class for the tranverse function of tree automata.
 */
@Getter @Setter
public class TransitionVisitor extends FOkParserBaseVisitor<Void> {
    private int depth = -1;
    public ParserRuleContext current; // subtype of RuleNode
    public ArrayList<ParserRuleContext> next;
    private boolean shouldStop = false;

    @Override
    public Void visit(ParseTree tree) {
        return tree.accept(this);
    }

    @Override
    public Void visitChildren(RuleNode node) {
        System.out.println("node: " + node);
        if (shouldStop) {
            return null;
        }
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

    /**
     * @param ctx The context of the formula.
     *            formula
     * 1          : NOT formula 
     * 2          | formula op=(IFF | IMPLIES | AND | OR) formula
     * 3          | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     * 4          | RELATION (LPAREN term (COMMA term)* RPAREN)?
     * 5          | term EQUALS term
     * 6          | value
     * 7          | LPAREN formula RPAREN
     *            ;
     */
    @Override
    public Void visitFormula(FormulaContext ctx) {
        System.out.println(ctx);
        if (ctx.getChild(0) == ctx.NOT()) { // case 1
        } else if (ctx.getChild(0) == ctx.LPAREN()) { // case 7
        } else if (ctx.qop != null) { // case 3
            switch (ctx.qop.getType()) {
                case FOkParser.FORALL:
                    break;
                case FOkParser.EXISTS:
                    break;
                default: break;
            }
        } else if (ctx.getChild(0) == ctx.RELATION()) { // case 4
            System.out.println("RELATION");
            for (int i = 0; i < ctx.getChildCount(); i++) { // discuss about the arity of the relation
                System.out.println(ctx.getChild(i));
            }
        } else if (ctx.getChild(0) == ctx.term()) { // case 5
        } else if (ctx.getChild(0) == ctx.value()) { // case 6
        } else if (ctx.op != null) { // case 2
        }
        return visitChildren(ctx);
    }

    /**
     * @param ctx The context of the term.
     *            term
     * 1          : FUNC LPAREN term (COMMA term)* RPAREN
     * 2          | VARIABLE
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
}
