package visitors;

import org.antlr.v4.runtime.tree.*;
import antlr.*;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is a helper class for the tranverse function of tree automata.
 */
@Getter
@Setter
public class TransitionVisitor extends FOkParserBaseVisitor<Void> {
    @Override
    public Void visit(ParseTree tree) {
        return tree.accept(this);
    }
}
