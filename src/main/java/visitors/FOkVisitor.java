package visitors;

import antlr.*;
import antlr.FOkParser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;

import FO.Structure;
import FO.Assignment;
import java.util.*;
import utils.*;
import java.util.stream.Collectors;


import org.antlr.v4.runtime.Token;

public class FOkVisitor<T> extends FOkParserBaseVisitor<Void> {
    private Structure<T> structure;

    private ParseTree tree;

    // the hashmap to store the bound variables in the formula.
    private HashMap<TerminalNode, Pair<Token, TerminalNode>> bdVarTable = new HashMap<>();
    private Set<TerminalNode> allVarSet = new HashSet<>(); // collect all the variables in the formula
    // maybe useful for the reuse of the variables.
    private List<ParserRuleContext> contexts = new ArrayList<>();
    private Assignment assignment;
    private boolean shouldStop = false;

    public FOkVisitor() {
        super();
    }

    public FOkVisitor(Structure<T> structure) {
        this.structure = structure;
    }

    /**
     * This method is used to pause the traversal of the tree.
     */
    private void pause_() {
        this.shouldStop = true;
    }

    /**
     * This method is used to resume the traversal of the tree.
     */
    public void resume_() {
        this.shouldStop = false;
    }

    @Override
    public Void visit(ParseTree tree) {
        this.tree = tree;
        return tree.accept(this);
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof ParserRuleContext) {
            this.contexts.add((ParserRuleContext) node);
        }
        if (node instanceof FormulaContext) {
            this.pause_();
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
     *            1 : NOT formula
     *            2 | formula op=(IFF | IMPLIES | AND | OR) formula
     *            3 | qop=(FORALL | EXISTS) VARIABLE DOT LPAREN formula RPAREN
     *            4 | RELATION (LPAREN term (COMMA term)* RPAREN)?
     *            5 | term EQUALS term
     *            6 | value
     *            7 | LPAREN formula RPAREN
     *            ;
     */
    @Override
    public Void visitFormula(FormulaContext ctx) {
        if (this.shouldStop) {
            return null;
        }
        FormulaContext fCtx = (FormulaContext) ctx;
        if (fCtx.qop != null) { // case 3
            this.allVarSet.add(fCtx.VARIABLE());
        }
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
            // when first tranverse to the variable, we need to find the nearest quantifier
            // and put it in our hashmap.
            RuleContext parent = ctx.getParent();
            // since variable can be reused, we consider the variable in the nearest
            // quantifier.
            while (parent != null && (!(parent instanceof FormulaContext) || ((FormulaContext) parent).qop == null || !((FormulaContext) parent).VARIABLE().getText().equals(ctx.VARIABLE().getText()))) {
                // 向上一直到找到第一个同名的 bounded variable 为止.
                parent = parent.getParent();
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
     * This method is used to calculate the value of the formula (T or F)
     * under given assignment.
     * Note that all the bounded will also be assigned in our FOk model.
     * 
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
        FormulaContext fCtx = (FormulaContext) ctx;
        if (ctx.getChild(0) == fCtx.NOT()) { // case 1: checked
            return !calFormulaVal(fCtx.formula(0));
        } else if (fCtx.op != null) { // case 2: checked
            boolean left = calFormulaVal(fCtx.formula(0));
            boolean right = calFormulaVal(fCtx.formula(1));
            switch (fCtx.op.getType()) {
                case FOkLexer.IFF:
                    return left == right;
                case FOkLexer.IMPLIES:
                    return !left || right;
                case FOkLexer.AND:
                    return left && right; // 这里如果不使用变量的话, Java 编译器会自动进行短路求值.
                case FOkLexer.OR:
                    return left || right;
                default:
                    break;
            }
        } else if (fCtx.qop != null) { // case 3
            switch (fCtx.qop.getType()) {
                case FOkParser.FORALL:
                    if(this.assignment != null){
                        System.out.println("[INFO]: bounded variable: " + fCtx.VARIABLE().getText() + " is assigned to " + this.assignment.getKvMap().get(fCtx.VARIABLE().getText()));
                        // print all children
                        return calFormulaVal(fCtx.formula(0));
                        // TODO: 这里的写法目前应该是不支持变量复用的. 
                    } else{ // if there is no assignment, just consider all the cases.
                        System.out.println("[INFO]: bounded variable: " + fCtx.VARIABLE().getText() + " is not assigned.");
                        boolean res = true;
                        Assignment newAssignment = new Assignment(new HashMap<>(this.assignment.getKvMap()));
                        for (Structure<T>.Element ele : this.structure.domain) {
                            newAssignment.getKvMap().put(fCtx.VARIABLE().getText(), ele);
                            res = res && calFormulaVal(fCtx.formula(0));
                        }
                        return res;
                    }
                case FOkParser.EXISTS:
                    if (this.assignment != null) {
                        System.out.println("[INFO]: bounded variable: " + fCtx.VARIABLE().getText() + " is assigned to " + this.assignment.getKvMap().get(fCtx.VARIABLE().getText()));
                        return calFormulaVal(fCtx.formula(0));
                    } else { // if there is no assignment, just consider all the cases.
                        System.out.println("[INFO]: bounded variable: " + fCtx.VARIABLE().getText() + " is not assigned.");
                        boolean res = false;
                        Assignment newAssignment = new Assignment(new HashMap<>(this.assignment.getKvMap()));
                        for (Structure<T>.Element ele : this.structure.domain) {
                            newAssignment.getKvMap().put(fCtx.VARIABLE().getText(), ele);
                            res = res || calFormulaVal(fCtx.formula(0));
                        }
                        return res;
                    }
                default:
                    break;
            }
        } else if (ctx.getChild(0) == fCtx.RELATION()) { // case 4: checked
            List<Structure<T>.Element> args = fCtx.term().stream().map(t -> {
                return this.structure.new Element(calTermVal(t));
            }).collect(Collectors.toList());
            return this.structure.relations
                    .get(fCtx.RELATION().getText())
                    .holds(args);
        } else if (ctx.getChild(0) == fCtx.term()) { // case 5: checked
            return calTermVal(fCtx.term(0)).equals(calTermVal(fCtx.term(1)));
        } else if (ctx.getChild(0) == fCtx.value()) { // case 6: checked
            return fCtx.value().TRUE() != null;
        } else if (ctx.getChild(0) == fCtx.LPAREN()) { // case 7: checked
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
    @SuppressWarnings("unchecked")
    private T calTermVal(ParserRuleContext ctx) {
        if (ctx.getChild(0) == ((TermContext) ctx).FUNC()) { // case 1: checked
            // 我们假设在这个简化的FOk模型中, 不会出现函数.
            return null;
        } else if (ctx.getChild(0) == ((TermContext) ctx).VARIABLE()) { // case 2
            // get the variable from the hashmap
            TermContext tCtx = (TermContext) ctx;
            Pair<Token, TerminalNode> pair = bdVarTable.get(((TermContext) ctx).VARIABLE());
            if (pair == null) {
                return null;
            }
            TerminalNode var = tCtx.VARIABLE();
            assert this.assignment.getKvMap().containsKey(var.getText());
            return ((T) this.assignment.getKvMap().get(var.getText()).getValue());
        } else if (ctx.getChild(0) == ((TermContext) ctx).CONST()) { // case 3: checked
            // get the value of the const
            TermContext tCtx = (TermContext) ctx;
            return this.structure.constants.get(tCtx.CONST().getText());
        }
        return null;
    }

    /**
     * This method is used to get the value of the formula (T or F)
     * @return The value of the formula (T or F)
     */
    public boolean getFormulaVal() {
        return this.calFormulaVal(this.contexts.get(0));
    }

    /**
     * This method is used to get the value of the formula (T or F)
     * under given assignment.
     * @param assignment The assignment of the variables.
     * @return The value of the formula (T or F)
     */
    public boolean getFormulaVal(Assignment assignment) {
        this.assignment = assignment;
        return this.getFormulaVal();
    }

    /**
     * This method is used to get the value of the formula (T or F)
     * @param ctx The context of the formula.
     * @param assignment The assignment of the variables.
     * @return The value of the formula (T or F)
     */
    public boolean getFormulaVal(FormulaContext ctx, Assignment assignment) {
        this.assignment = assignment;
        return this.calFormulaVal(ctx);
    }

    /**
     * @return The hashmap to store the bound variables in the formula.
     */
    public HashMap<TerminalNode, Pair<Token, TerminalNode>> getBdVarTable() {
        tree.accept(this);
        return bdVarTable;
    }
}
