package structures;

import java.lang.reflect.Array;
import java.util.*;

import FO.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class GraphStructure extends Structure<Integer> {

    // hidden member variables
    // for structure<T>
    // consts: Map<String, T>
    // relations: Map<String, Relation>
    // domain: Set<Element>
    // pos: boolean

    // explicitly invoke the constructor of the superclass if superclass does not have a default constructor
    public GraphStructure(boolean pos) {
        super(pos);
        this.relations.put("E", new E());
    }

    public class Vertex extends Element {
        public Vertex(Integer value) {
            super(value);
        }

        @Override
        public boolean equals(Object obj) {
            Vertex vertex = (Vertex) obj;
            System.out.println(111111);
            System.out.println(this.value + " " + vertex.value);
            return this.value.equals(vertex.value);
        }
    }


    /**
     * represents an directed edge in the graph.
     */
    @AllArgsConstructor
    @NoArgsConstructor 
    public class E extends Relation {
        public final int arity = 2;
        
        @Getter
        @Setter
        private List<Edge> edges = new ArrayList<>();


        public boolean holds(Edge edge) {
            return this.holds(Arrays.asList(edge.v1, edge.v2));
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
                if (edge.v1.equals(args.get(0)) && edge.v2.equals(args.get(1))) {
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
        @Override
        public boolean equals(Object obj) {
            Edge edge = (Edge) obj;
            return this.v1.equals(edge.v1) && this.v2.equals(edge.v2);
        }
    }

    public Edge addEdge(Vertex v1, Vertex v2) {
        Edge dEdge = new Edge(v1, v2);
        ((E)this.relations.get("E")).getEdges().add(dEdge);
        return dEdge;
    }

    public Vertex addVertex(Integer value) {
        Vertex vertex = new Vertex(value);
        this.domain.add(vertex);
        return vertex;
    }
}
