package AST;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * Helper class for representing and manipulating Abstract Syntax Trees (AST)
 * for first-order logic formulas.
 */
@Getter
@Setter
public class AST {
    public static enum FOk {
        OP, // 逻辑运算符
        QOP, // 量词
        EQUALS, RELATION, // relation: R(x, y), E(x, y)
        VALUE, // value: true, false
        TERM, // term: functions, variables, constants
        FORMULA, // 公式
        NOT,
        LPAREN,
        RPAREN,
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AST) {
            AST ast = (AST) obj;
            return this.toString().equals(ast.toString());
        }
        return false;
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
        Node node = new Node(type, value);
        Node root = new Node(FOk.FORMULA, node.toString());
        switch (type) {
            case FORMULA:
                this.root = node;
                break;
            case VALUE:
            case TERM:
                root.addChild(node);
                this.root = root;
                break;
            default:
                break;

        }
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

    public AST getAST() {
        return this;
    }

    @Getter
    public static class Node {
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

        public String toString() {
            return this.value;
        }
    }

    /**
     * Convert the AST to a string (as a formula)
     * 
     * @return the string representation of the AST
     */
    public String toString() {
        return this.toString(this.root);
    }

    /**
     * Convert the AST to a string (as a formula)
     * 
     * @param node the root of the AST
     * @return the string representation of the AST
     */
    public String toString(Node node) {
        return this.toStringHelper(node).trim();
    }

    /**
     * Helper method to convert the AST to a string
     * 
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
     * 
     * @return the size of the AST
     */
    public int getSize() {// 返回 AST 叶节点的个数
        return root == null ? 0 : getSizeHelper(root);
    }

    /**
     * Helper method to get the size of the AST
     * 
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
