import org.junit.Test;
import static org.junit.Assert.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import visitors.FOkVisitor;

public class SimpleTest {
    @Test
    public void test() {
        String input = "forall x. (exists y . (R(x) <-> S(y)))";
        CharStream charStream = CharStreams.fromString(input);
        FOkLexer lexer = new FOkLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FOkParser parser = new FOkParser(tokens);
        ParseTree tree = parser.formula();
        System.out.println(tree.toStringTree(parser));

        // print all the variables in the formula
        FOkVisitor visitor = new FOkVisitor();
        visitor.visit(tree);
    }
}
