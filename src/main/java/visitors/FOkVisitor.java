package visitors;

import antlr.*;
import antlr.FOkParser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;
import interfaces.*;
import java.util.*;
import utils.*;
import org.antlr.v4.runtime.Token;

public class FOkVisitor extends FOkParserBaseVisitor<Void> {
    private boolean value = false;
    private Structure<?> structure;

    // the hashmap to store the bound variables in the formula.
    private HashMap<TerminalNode, Pair<Token, TerminalNode>> bdVarTable = new HashMap<>();

    public boolean getFormulaVal(ParseTree tree) {
        this.visit(tree);
        return value;
    }

    @Override
    public Void visit(ParseTree tree) {
        return tree.accept(this);
    }

    public Void visit(ParseTree tree, Structure<?> structure) {
        this.structure = structure;
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
     *            3 | CONST
     *            ;
     */
    @Override
    public Void visitTerm(TermContext ctx) {
        if (ctx.getChild(0) == ctx.VARIABLE()) { // case 2
            // when first tranverse to the variable, we need to find the nearest quantifier and put it in our hashmap.
            RuleContext parent = ctx.getParent();
            // since variable can be reused, we consider the variable in the nearest quantifier.
            while (parent != null) {
                while (!(parent instanceof FormulaContext)) {
                    parent = parent.getParent();
                }
                if (((FormulaContext) parent).qop != null) {
                    break;
                }
            }
            FormulaContext fCtx = (FormulaContext) parent;
            bdVarTable.put(
                ctx.VARIABLE(), 
                new Pair<>(fCtx.qop, fCtx.VARIABLE())
            );
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
    public boolean calFormulaVal(ParserRuleContext ctx) {
        FormulaContext fCtx = (FormulaContext) ctx;
        ValueContext vCtx = (ValueContext) ctx;
        if (ctx.getChild(0) == fCtx.NOT()) { // case 1
            return !calFormulaVal(fCtx.formula(0));
        } else if (fCtx.op != null) { // case 2
        } else if (fCtx.qop != null) { // case 3
            switch (fCtx.qop.getType()) {
                case FOkParser.FORALL:
                    // this.structure.domain.forEach(e -> {
                    break;
                case FOkParser.EXISTS:
                    break;
                default:
                    break;
            }
        } else if (ctx.getChild(0) == fCtx.RELATION()) { // case 4
            for (int i = 0; i < ctx.getChildCount(); i++) { // discuss about the arity of the relation
                System.out.println(ctx.getChild(i));
            }
        } else if (ctx.getChild(0) == fCtx.term()) { // case 5
        } else if (ctx.getChild(0) == fCtx.value()) { // case 6
            return vCtx.getRuleIndex() == FOkParser.TRUE;
        } else if (ctx.getChild(0) == fCtx.LPAREN()) { // case 7
            return calFormulaVal(fCtx.formula(0));
        } 
        return false;
    }

    /**
     * @param ctx The context of the term.
     *            term
     *            1 : FUNC LPAREN term (COMMA term)* RPAREN
     *            2 | VARIABLE
     *            3 | CONST
     *            ;
     */
    private Structure<?>.Const calTermVal(ParserRuleContext ctx) {
        if (ctx.getChild(0) == ((TermContext) ctx).FUNC()) { // case 1
            // 我们假设在这个简化的FOk模型中, 不会出现函数.
            return null;
        } else if (ctx.getChild(0) == ((TermContext) ctx).VARIABLE()) { // case 2
            // get the variable from the hashmap
            Pair<Token, TerminalNode> pair = bdVarTable.get(((TermContext) ctx).VARIABLE());
            if (pair == null) {
                return null;
            }
        } else if (ctx.getChild(0) == ((TermContext) ctx).CONST()) { // case 3
            // get the value of the const
            TermContext tCtx = (TermContext) ctx;
            return this.structure.constants.stream()
                    .filter(c -> c.getName().equals(tCtx.CONST().getText()))
                    .findFirst().get();
        }
        return null;
    }
}