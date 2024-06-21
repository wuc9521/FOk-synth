import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import FA.*;
import java.util.*;
import structures.GraphStructure;
import structures.GraphStructure.Edge;
import structures.GraphStructure.Vertex;
import visitors.FOkVisitor;
import visitors.TransitionVisitor;
import utils.Colors;
import antlr.FOkParser.FormulaContext;
import utils.*;

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
    public void automataInitTest() {
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
            ((GraphStructure.E) graphSturcture.relations.get("E")).addEdge(dEdge);
        }
        FOkATFA automaton = new FOkATFA(
                new ArrayList<String>() {
                    {
                        add("x");
                        add("y");
                    }
                }, // the list of variables
                graphSturcture);
        FOkVisitor visitor = new FOkVisitor(graphSturcture);
        visitor.visit(tree);
        // x = 4, y = 2 should reject
        System.out.println("");
        automaton.getStates().stream().forEach(state -> {
            boolean isAccepting = visitor.getFormulaVal(((FOkATFA.TState) state).getAssignment());
            String color = isAccepting ? Colors.GREEN : Colors.RED; // 设置颜色
            System.out.print(color); // 设置颜色
            System.out.print("{ ");
            ((FOkATFA.TState) state).getAssignment().getKvMap().forEach((k, v) -> {
                System.out.print(k + " -> " + v.getValue() + " "); // 打印键值对
            });
            System.out.println((isAccepting ? "} is accepting" : "} is rejecting") + Colors.RESET); // 结束颜色并重置
        });
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

    @Test
    public void automataAcceptsTest() {
        String[] input = {
                "~(forall x . (~(E(#1, x)) | (exists y . ( E(x, y) & E(y, #2)))))",
                "forall x . ( ~(E(#1, x)) | (exists y . ( E(x, y) & E(y, #2))))",
                "E(#1, #4)", // E(7, 6)
                "~(exists x . (E(x, #4)))", // ~E(x, 6)
                "exists x . (E(x, #4))", // E(x, 6)
                "forall x . (E(x, #4))", // E(x, 6)
                "$F"
        };
        boolean[][] expected = {
                { false, true, false, true, false, false, false },
                { false, true, false, true, false, true, true }
        };

        GraphStructure gs1 = new GraphStructure(true);
        GraphStructure gs2 = new GraphStructure(false);
        for (int i = 0; i < 8; i++) {
            gs1.addVertex(i);
            gs2.addVertex(i);
        }
        gs1.constants.put("#1", 7);
        gs1.constants.put("#2", 0);
        gs1.constants.put("#3", 1);
        gs1.constants.put("#4", 6);

        gs2.constants.put("#1", 7);
        gs2.constants.put("#2", 0);
        gs2.constants.put("#3", 1);
        gs2.constants.put("#4", 6);
        int[][] edges1 = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 1, 4 }, { 1, 5 }, { 4, 1 },
                { 5, 1 }, { 4, 7 }, { 5, 7 }, { 7, 4 }, { 7, 5 } };
        int[][] edges2 = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 1, 4 }, { 1, 5 }, { 4, 1 },
                { 5, 1 }, { 4, 7 }, { 5, 7 }, { 7, 4 }, { 7, 5 }, { 7, 6 }, { 6, 7 } };
        for (int[] edge : edges1) {
            Vertex v1 = gs1.new Vertex(edge[0]);
            Vertex v2 = gs1.new Vertex(edge[1]);
            GraphStructure.Edge dEdge = gs1.new Edge(v1, v2);
            ((GraphStructure.E) gs1.relations.get("E")).addEdge(dEdge);
        }
        for (int[] edge : edges2) {
            Vertex v1 = gs2.new Vertex(edge[0]);
            Vertex v2 = gs2.new Vertex(edge[1]);
            GraphStructure.Edge dEdge = gs2.new Edge(v1, v2);
            ((GraphStructure.E) gs2.relations.get("E")).addEdge(dEdge);
        }
        ArrayList vars = new ArrayList<String>() {
            {
                add("x");
                add("y");
            }
        }; // the list of variables
        FOkVisitor visitor1 = new FOkVisitor(gs1);
        FOkVisitor visitor2 = new FOkVisitor(gs2);
        for (int i = 0; i < input.length; i++) {
            try {
                // automaton2 = new FOkATFA(vars, gs2);
                visitor1.visit(ParserUtils.parse(input[i]));
                visitor2.visit(ParserUtils.parse(input[i]));
                assertEquals(new FOkATFA(vars, gs1).accepts(visitor1), expected[0][i]);
                assertEquals(new FOkATFA(vars, gs2).accepts(visitor2), expected[1][i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
