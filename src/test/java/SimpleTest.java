import org.junit.Test;

import TreeAutomaton.TreeState;
import static org.junit.Assert.*;
import java.util.Scanner;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import visitors.FOkVisitor;
import visitors.TransitionVisitor;

public class SimpleTest {
    @Test
    public void simpleTest() {
        String input = "forall x. (exists y . (R(x) <-> S(y))) & $T";
        CharStream charStream = CharStreams.fromString(input);
        FOkLexer lexer = new FOkLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FOkParser parser = new FOkParser(tokens);
        ParseTree tree = parser.formula();
        System.out.println(tree.toStringTree(parser));

        // print all the variables in the formula
        FOkParserBaseVisitor<Void> visitor = new TransitionVisitor();
        visitor.visit(tree);
    }

    @Test
    public void automataTest() {
        String input = "forall x. (exists y . (P(x) <-> Q(y))) & $T";
        CharStream charStream = CharStreams.fromString(input);
        FOkLexer lexer = new FOkLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FOkParser parser = new FOkParser(tokens);
        ParseTree tree = parser.formula();
        System.out.println(tree.toStringTree(parser));

        TreeAutomaton automaton = new TreeAutomaton(
            null,
            null,
            null,
            null
        );
    }
}
