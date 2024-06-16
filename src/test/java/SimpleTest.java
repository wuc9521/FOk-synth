import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Scanner;
import org.antlr.v4.misc.Graph;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import structures.GraphStructure;
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
        // TreeAutomaton automaton = new TreeAutomaton(
        //         null,
        //         null,
        //         null,
        //         null);
    }

    @Test
    public void calValTest() {
        String input = "forall x. (exists y . (P(x) <-> Q(y))) & $T";
        CharStream charStream = CharStreams.fromString(input);
        FOkLexer lexer = new FOkLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FOkParser parser = new FOkParser(tokens);
        ParseTree tree = parser.formula();

        // print all the variables in the formula
        FOkParserBaseVisitor<Void> visitor = new FOkVisitor();
        visitor.visit(tree);
    }

    @Test
    public void SeparateTest() {
        // test for section 2.1 Fig 1
        GraphStructure[] gs = new GraphStructure[4];
        gs[0] = gs[1] = new GraphStructure(true);
        gs[2] = gs[3] = new GraphStructure(false);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                gs[i].addVertex(j);
            }
        }
    }
}
