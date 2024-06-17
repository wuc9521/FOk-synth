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
        String input = "forall x . ( E(#1, x) -> (exists y . ( E(x, y) & E(y, #2))))";
        CharStream charStream = CharStreams.fromString(input);
        FOkLexer lexer = new FOkLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FOkParser parser = new FOkParser(tokens);
        ParseTree tree = parser.formula();
        
        GraphStructure graphSturcture = new GraphStructure(true);
        for (int i = 0; i < 8; i++) {
            graphSturcture.addVertex(i);
        }
        graphSturcture.constants.put("#1", 7);
        graphSturcture.constants.put("#2", 0);
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
            graphSturcture
        );
        FOkVisitor visitor = new FOkVisitor(graphSturcture);
        visitor.visit(tree);
        int idx = 0;
        int tCnt = 0;
        int fCnt = 0;
        for (TreeAutomaton.TreeState state : automaton.getStates()) {
            idx++;
            if (visitor.getFormulaVal(state.getAllVarAsnmnt())) {
                tCnt++;
            } else {
                fCnt++;
            }
        }
        assertEquals(idx, 64);
        assertEquals(tCnt, 50);
        assertEquals(fCnt + tCnt, idx);
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
        assertFalse(visitor.getFormulaVal());
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
