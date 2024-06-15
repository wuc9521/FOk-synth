import antlr.*;
import antlr.FOkParser.*;
import org.antlr.v4.runtime.tree.TerminalNode; 

public class FOkVisitor extends FOkParserBaseVisitor<Void> {
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
        //if constains a variable
        TerminalNode variable = ctx.VARIABLE();
        if (variable != null) {
            System.out.println(variable);
        }
        TerminalNode and = ctx.AND();
        return visitChildren(ctx);
    }

    @Override
    public Void visitTerm(TermContext ctx) {
        TerminalNode variable = ctx.VARIABLE();
        if (variable != null) {
            System.out.println(variable);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitValue(ValueContext ctx) {
        return visitChildren(ctx);
    }
    
}
