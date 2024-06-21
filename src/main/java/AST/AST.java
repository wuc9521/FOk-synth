package AST;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import antlr.*;

/**
 * Helper class for representing and manipulating Abstract Syntax Trees (AST)
 * for first-order logic formulas.
 */
@Getter
public class AST {
    public enum FOk {
        OP, // 逻辑运算符
        QOP, // 量词
        EQUALS, RELATION, // relation: R(x, y), E(x, y)
        VALUE, // value: true, false
        TERM, // term: functions, variables, constants
        FORMULA, // 公式
        NOT,
        LPAREN,
        RPAREN,
        COMMA,
    }

    private int size; // The size of the AST
    private Node root; // The root of the AST

    public AST() {
        this.root = new Node(FOk.FORMULA, null);
    }

    public AST(Node root) {
        this.root = root;
    }

    public AST(FOk type, String value) {
        this.root = new Node(type, value);
    }

    public AST(AST ast) {
        this.root = ast.getRoot();
        this.size = ast.getSize();
    }

    public Node getRoot() {
        return root;
    }

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

    }

    /**
     * Convert the AST to a string (as a formula)
     */
    public String toString() {
        if (root == null) {
            return "";
        }
        return toStringHelper(root).trim();
    }

    /**
     * Helper method to convert the AST to a string
     * @param node the current node
     * @return the string representation of the AST with the given node as the root
     */
    private String toStringHelper(Node node) {
        StringBuilder sb = new StringBuilder();
        switch (node.getType()) {
            case OP:
                if (node.getChildren().size() == 2) {
                    sb.append("(");
                    sb.append(toStringHelper(node.getChildren().get(0)));
                    sb.append(" ").append(node.getValue()).append(" ");
                    sb.append(toStringHelper(node.getChildren().get(1)));
                    sb.append(")");
                }
                break;
            case NOT:
                sb.append(node.getValue());
                if (!node.getChildren().isEmpty()) {
                    sb.append(toStringHelper(node.getChildren().get(0)));
                }
                break;
            case QOP:
                sb.append(node.getValue()).append(" ");
                sb.append(node.getChildren().get(0).getValue()).append(".");
                sb.append("(");
                sb.append(toStringHelper(node.getChildren().get(1)));
                sb.append(")");
                break;
            case RELATION:
            case EQUALS:
                if (node.getChildren().size() >= 1) {
                    sb.append(node.getValue()).append("(");
                    List<String> childFormulas = new LinkedList<>();
                    for (Node child : node.getChildren()) {
                        childFormulas.add(toStringHelper(child));
                    }
                    sb.append(String.join(", ", childFormulas));
                    sb.append(")");
                }
                break;
            case VALUE:
            case TERM:
                sb.append(node.getValue());
                break;
            default:
                sb.append(node.getValue());
                break;
        }
        return sb.toString();
    }

    /**
     * Get the size of the AST
     * @return the size of the AST
     */
    public int getSize() {// 返回 AST 叶节点的个数
        return root == null ? 0 : getSizeHelper(root);
    }

    /**
     * Helper method to get the size of the AST
     * @param node the current node
     * @return the size of the AST
     */
    private int getSizeHelper(Node node) {
        assert node != null;
        int size = 0;
        if (node.getChildren().isEmpty()) {
            return 1;
        }
        for (Node child : node.getChildren()) {
            size += getSizeHelper(child);
        }
        return size;
    }
}
