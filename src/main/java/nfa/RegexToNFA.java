package nfa;

import java.util.*;

/**
 * Utility class for converting regular expressions to NFAs using the Shunting Yard algorithm and expression trees.
 */
public class RegexToNFA {

    /**
     * Converts an infix regular expression to postfix notation using the Shunting Yard algorithm.
     * @param regex the infix regular expression
     * @return the postfix notation of the regular expression
     */
    public static String toPostFix(String regex) {
        Stack<Character> op = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        Map<Character,Integer> precedence = new HashMap<>();
        precedence.put('*',3);
        precedence.put('.',2);
        precedence.put('|',1);
        for (int i = 0; i < regex.length(); i++) {
            char ch = regex.charAt(i);
            if (isOperand(ch)) {
                postfix.append(ch);
            } else if (ch == '(') {
                op.push(ch);
            } else if (ch == ')') {
                while(op.peek() != '(') {
                    postfix.append(op.pop());
                }
                op.pop();
            } else {
                while (!op.isEmpty() && op.peek() != '(' && precedence.get(ch) <= precedence.get(op.peek())) {
                    postfix.append(op.pop());
                }
                op.push(ch);
            }
        }

        while (!op.isEmpty()) {
            postfix.append(op.pop());
        }

        return postfix.toString();
    }

    /**
     * Checks if a symbol is an operand (not an operator or parenthesis).
     * @param symbol the character to check
     * @return true if the symbol is an operand, false otherwise
     */
    private static boolean isOperand(char symbol) {
        return symbol != '(' && symbol != '*' && symbol != '|' && symbol != ')' && symbol != '.';
    }

    /**
     * Checks if a symbol is an operator (concatenation, union, or star).
     * @param symbol the character to check
     * @return true if the symbol is an operator, false otherwise
     */
    private static boolean isOperator(char symbol) {
        return symbol == '.' || symbol == '|' || symbol == '*';
    }

    /**
     * Adds explicit concatenation operators ('.') to a regular expression string.
     * @param string the input regular expression
     * @return the regular expression with explicit concatenation
     */
    public static String addConcatination(String string) {
        if (string.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        result.append(string.charAt(0));
        for (int i = 1; i < string.length(); i++) {
            char ch = string.charAt(i);
            if ((isOperand(ch) || ch == '(') && string.charAt(i-1) != '|' && string.charAt(i-1) != '(' && string.charAt(i-1) != '.') {
                result.append(".").append(ch);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Builds an NFA from a regular expression string.
     * @param input the regular expression
     * @return the constructed NFA
     */
    public static NFA build(String input) {
       String postfix = toPostFix(addConcatination(input));
       Stack<NFA> stack = new Stack<>();
       if (postfix.isEmpty()) {
           // Return an NFA that accepts only the empty string
           State startState = new State("eps_start");
           return new NFA(new Alphabet(new HashSet<>()), Set.of(startState), startState, new HashMap<>(), Set.of(startState));
       }

       for (Character ch : postfix.toCharArray()) {
           if (isOperand(ch)) {
               stack.push(NFA.buildNFA(ch));
           } else if (ch == '|' || ch == '.') {
               NFA nfa1 = stack.pop();
               NFA nfa2 = stack.pop();
               NFA nfa3 = ch == '|' ? NFA.orOperation(nfa1,nfa2) : NFA.concatinateNFA(nfa2,nfa1);
               stack.push(nfa3);
           } else if (ch == '*') {
               NFA nfa3 = NFA.starOperation(stack.pop());
               stack.push(nfa3);
           }
       }
       return stack.peek();
    }

}
