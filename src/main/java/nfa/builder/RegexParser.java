package nfa.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Utility class for parsing regular expressions.
 */
public class RegexParser {
    private static final Map<Character, Integer> PRECEDENCE;
    static {
        PRECEDENCE = new HashMap<>();
        PRECEDENCE.put('*', 3);
        PRECEDENCE.put('.', 2);
        PRECEDENCE.put('|', 1);
    }

    /**
     * Converts an infix regular expression to postfix notation.
     */
    public static String toPostfix(String regex) {
        if (regex == null) {
            throw new IllegalArgumentException("Regex cannot be null");
        }
        
        String withConcatenation = addConcatenation(regex);
        Stack<Character> operators = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (char ch : withConcatenation.toCharArray()) {
            if (isOperand(ch)) {
                postfix.append(ch);
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // Remove '('
                }
            } else {
                while (!operators.isEmpty() && operators.peek() != '(' && 
                       PRECEDENCE.get(ch) <= PRECEDENCE.get(operators.peek())) {
                    postfix.append(operators.pop());
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            postfix.append(operators.pop());
        }

        return postfix.toString();
    }

    /**
     * Adds explicit concatenation operators.
     */
    private static String addConcatenation(String regex) {
        if (regex.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        result.append(regex.charAt(0));
        
        for (int i = 1; i < regex.length(); i++) {
            char ch = regex.charAt(i);
            char prev = regex.charAt(i-1);
            if ((isOperand(ch) || ch == '(') && 
                (isOperand(prev) || prev == '*' || prev == ')')) {
                result.append('.').append(ch);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Checks if a symbol is an operand.
     */
    public static boolean isOperand(char symbol) {
        return symbol != '(' && symbol != '*' && 
               symbol != '|' && symbol != ')' && 
               symbol != '.';
    }

    private RegexParser() {} // Prevent instantiation
} 