import org.junit.Test;

import static org.junit.Assert.*;

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
import antlr.FOkParser.FormulaContext;
import utils.*;
import AST.*;

public class FunctionTest {
    // @Test
    // public void unionTest() {
    //     GraphStructure gs1 = new GraphStructure(true);
    //     GraphStructure gs2 = new GraphStructure(false);
    //     for (int i = 0; i < 8; i++) {
    //         gs1.addVertex(i);
    //         gs2.addVertex(i);
    //     }
    //     gs1.constants.put("#1", 7);
    //     gs1.constants.put("#2", 0);
    //     gs1.constants.put("#3", 1);
    //     gs1.constants.put("#4", 6);

    //     gs2.constants.put("#1", 7);
    //     gs2.constants.put("#2", 0);
    //     gs2.constants.put("#3", 1);
    //     gs2.constants.put("#4", 6);
    //     int[][] edges1 = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 1, 4 }, { 1, 5 }, { 4, 1 },
    //             { 5, 1 }, { 4, 7 }, { 5, 7 }, { 7, 4 }, { 7, 5 } };
    //     int[][] edges2 = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 1, 4 }, { 1, 5 }, { 4, 1 },
    //             { 5, 1 }, { 4, 7 }, { 5, 7 }, { 7, 4 }, { 7, 5 }, { 7, 6 }, { 6, 7 } };
    //     for (int[] edge : edges1) {
    //         Vertex v1 = gs1.new Vertex(edge[0]);
    //         Vertex v2 = gs1.new Vertex(edge[1]);
    //         GraphStructure.Edge dEdge = gs1.new Edge(v1, v2);
    //         ((GraphStructure.E) gs1.relations.get("E")).addEdge(dEdge);
    //     }
    //     for (int[] edge : edges2) {
    //         Vertex v1 = gs2.new Vertex(edge[0]);
    //         Vertex v2 = gs2.new Vertex(edge[1]);
    //         GraphStructure.Edge dEdge = gs2.new Edge(v1, v2);
    //         ((GraphStructure.E) gs2.relations.get("E")).addEdge(dEdge);
    //     }
    //     ArrayList vars = new ArrayList<String>() {
    //         {
    //             add("x");
    //             add("y");
    //         }
    //     }; // the list of variables
    //     FOkVisitor visitor1 = new FOkVisitor(gs1);
    //     FOkVisitor visitor2 = new FOkVisitor(gs2);
    //     ArrayList AFTAList = new ArrayList<FOkATNFA<GraphStructure>>() {
    //         {
    //             add(new FOkATNFA(vars, gs1));
    //             add(new FOkATNFA(vars, gs2));
    //         }
    //     };
    //     UnionATFA union = new UnionATFA(AFTAList);
    //     ASTBuilder builder = new ASTBuilder(gs1, vars);
    //     Set<AST> asts = new HashSet<>();
    //     asts = builder.grow(asts);
    //     asts = builder.grow(asts);
    //     asts = builder.grow(asts);
    //     for (AST ast : asts) {
    //         try {
    //             String input = ast.toString();
    //             System.out.println(
    //                     "Union ATFA" + (union.accepts(input) ? " accepts" : " rejects") + " the formula: " + input);
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

     @Test
    public void automataAcceptsTest() {
        GraphStructure gs1 = new GraphStructure(true);
        GraphStructure gs2 = new GraphStructure(false);
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
                // automaton2 = new FOkATNFA(vars, gs2);
                visitor1.visit(ParserUtils.parse(input[i]));
                visitor2.visit(ParserUtils.parse(input[i]));
                assertEquals(new FOkATNFA(vars, gs1).accepts(visitor1), expected[0][i]);
                System.out.println("ATFA 1" + (new FOkATNFA(vars, gs1).accepts(visitor1) ? " accepts" : " rejects")
                        + " the formula: " + input[i]);
                assertEquals(new FOkATNFA(vars, gs2).accepts(visitor2), expected[1][i]);
                System.out.println("ATFA 2" + (new FOkATNFA(vars, gs2).accepts(visitor2) ? " accepts" : " rejects")
                        + " the formula: " + input[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
