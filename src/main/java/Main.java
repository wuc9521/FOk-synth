import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import visitors.*;

public class Main {
    public static void main(String[] args) {
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