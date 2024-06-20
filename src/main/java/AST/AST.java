package AST;


import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * Helper class for representing and manipulating Abstract Syntax Trees (AST)
 * for first-order logic formulas.
 */
@Getter
public class AST {
    public enum FOk {
        PROG, SENTENCE, FORMULA, TERM, VALUE, RELATION, EQUALS, NOT, IFF, IMPLIES, AND, OR, FORALL, EXISTS, VARIABLE,
        DOT, LPAREN, RPAREN, COMMA
    }

    private int size; // The size of the AST
    private Node root; // The root of the AST

    public AST() {
        this.root = null; // Initially, there's no root
    }

    public Node getRoot() {
        return root;
    }

    // Method to add a node as root or as a child of an existing node
    public void addNode(FOk type, String value, Node parent) {
        Node newNode = new Node(type, value);
        if (parent == null) {
            this.root = newNode;
        } else {
            parent.addChild(newNode);
            newNode.setParent(parent);
        }
    }

    @Getter
    public class Node {
        private FOk type;
        private String value;
        private Node parent;
        private List<Node> children = new LinkedList<>();

        public Node(FOk type, String value) {
            this.type = type;
            this.value = value;
        }

        public void addChild(Node child) {
            this.children.add(child);
            child.setParent(this);
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        // Method to print tree from this node down
        public void printTree(String prefix) {
            System.out.println(prefix + type + ": " + value);
            children.forEach(child -> child.printTree(prefix + "  "));
        }
    }

    public String toString(){
        return null;
    }
}
