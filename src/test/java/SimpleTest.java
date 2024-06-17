import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Scanner;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import java.util.*;
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
        // System.out.println(tree.toStringTree(parser));

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
        GraphStructure graphSturcture = new GraphStructure(true);
        for (int i = 0; i < 8; i++) {
            graphSturcture.addVertex(i);
        }
        graphSturcture.constants.put("s", 7);
        graphSturcture.constants.put("t", 0);
        int[][] edges = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 1, 4 }, { 1, 5 }, { 4, 1 },
                { 5, 1 }, { 4, 7 }, { 5, 7 }, { 7, 4 }, { 7, 5 } };
        for (int[] edge : edges) {
            Vertex v1 = graphSturcture.new Vertex(edge[0]);
            Vertex v2 = graphSturcture.new Vertex(edge[1]);
            GraphStructure.Edge dEdge = graphSturcture.new Edge(v1, v2);
            ((GraphStructure.E) graphSturcture.relations.get("E")).getEdges().add(dEdge);
        }
        TreeAutomaton automaton = new TreeAutomaton(
                new ArrayList<String>() {
                    {
                        add("x");
                        add("y");
                    }
                }, // the list of variables
                graphSturcture);
        for (TreeAutomaton.TreeState state : automaton.getStates()) {
            // System.out.println(
            // state.getAllVarAsnmnt().getKvMap().get("x").getValue()
            // + " "
            // + state.getAllVarAsnmnt().getKvMap().get("y").getValue()
            // );
        }
        // System.out.println(automaton.getStates().size());
    }

    @Test
    public void calValTest() {
        String input = "(($F -> $T)->$T) <-> $T & $F";
        CharStream charStream = CharStreams.fromString(input);
        FOkLexer lexer = new FOkLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FOkParser parser = new FOkParser(tokens);
        ParseTree tree = parser.formula();

        // Print all the variables in the formula
        FOkVisitor visitor = new FOkVisitor();
        visitor.visit(tree);
        System.out.println("====================");
        System.out.println(visitor.getFormulaVal());
        System.out.println("====================");
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
