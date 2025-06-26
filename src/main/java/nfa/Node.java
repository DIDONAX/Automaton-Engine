package nfa;

/**
 * Represents a node in an expression tree for regular expressions.
 * Each node can have a left and right child and holds a character value (operator or operand).
 */
public class Node {
    /**
     * The left child of this node.
     */
    public Node left;
    /**
     * The right child of this node.
     */
    public Node right;
    /**
     * The value of this node (operator or operand).
     */
    public char value;

    /**
     * Constructs a node with the given value.
     * @param value the character value for this node
     */
    public Node(char value) {
        this.value = value;
        left = right = null;
    }
}
