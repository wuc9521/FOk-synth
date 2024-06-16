package structures;

import interfaces.*;
import java.util.*;

import javax.annotation.processing.Generated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class GraphStructure extends Structure<Integer> {

    // hidden member variables
    // consts: Set<Const>
    // domain: Set<Element>
    // relations: Set<Relation>
    // pos: boolean

    // explicitly invoke the constructor of the superclass if superclass does not have a default constructor
    public GraphStructure(boolean pos) {
        super(pos);
        this.relations.add(new E("E")); // in graph, there is at least a relation E, default name is "E"
    }

    public class Vertex extends Element {
        public String name = "V"; // default name is "V"
        public Vertex(Integer value) {
            super(value);
        }
    }

    @Getter
    @Setter
    public class VConst extends Const {
        private Integer value;
        private String name;
        public VConst(Integer value) {
            super(value, "#" + value);
            this.value = value;
            this.name = "#" + value;
        }
    }

    /**
     * represents an directed edge in the graph.
     */
    @AllArgsConstructor
    @NoArgsConstructor 
    public class E extends Relation {
        public final int arity = 2;
        public List<Edge> edges = new ArrayList<>();

        @Setter
        public String name;
        
        E(String name) {
            this.name = name;
        }

        public boolean holds(Edge edge) {
            return this.edges.contains(edge);
        }

        public boolean holds(Vertex... vertexs) {
            if (vertexs.length != 2) {
                return false;
            }
            return this.holds(Arrays.asList(vertexs));
        }

        @Override
        public boolean holds(List<Element> args) {
            for (Edge edge : this.edges) {
                if (edge.v1 == args.get(0) && edge.v2 == args.get(1)) {
                    return true;
                }
            }
            return false;
        }
    }

    public class Edge {
        // v1 -> v2
        Vertex v1;
        Vertex v2;
        public Edge(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    public Edge addEdge(Vertex v1, Vertex v2) {
        Edge dEdge = new Edge(v1, v2);
        this.relations.stream().filter(r -> r instanceof E).forEach(r -> ((E) r).edges.add(dEdge)); // add the edge to the relation E
        return dEdge;
    }

    public Vertex addVertex(Integer value) {
        Vertex vertex = new Vertex(value);
        this.domain.add(vertex);
        return vertex;
    }
}
