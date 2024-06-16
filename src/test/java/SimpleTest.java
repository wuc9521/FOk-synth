import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Scanner;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import structures.GraphStructure;
import structures.GraphStructure.Vertex;
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
        GraphStructure graph = new GraphStructure(true);
        for(int i = 0; i < 8; i++) {
            graph.addVertex(i);
        }
        graph.constants.put("s", 7);
        graph.constants.put("t", 0);
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {2, 0}, {3, 0}, {1, 4}, {1, 5}, {4, 1}, {5, 1}, {4, 7}, {5, 7}, {7, 4}, {7, 5}};
        for(int[] edge : edges) {
            Vertex v1 = graph.new Vertex(edge[0]);
            Vertex v2 = graph.new Vertex(edge[1]);
            GraphStructure.Edge dEdge = graph.new Edge(v1, v2);
            ((GraphStructure.E)graph.relations.get("E")).getEdges().add(dEdge);
        }
        TreeAutomaton automaton = new TreeAutomaton(tree, new HashSet<>(), null);
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
