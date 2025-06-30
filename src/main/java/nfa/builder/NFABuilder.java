package nfa.builder;

import nfa.automaton.NFA;
import nfa.components.Alphabet;
import nfa.components.State;
import nfa.components.Transition;
import java.util.*;

/**
 * Builder for creating NFAs from regular expressions and basic components.
 */
public class NFABuilder implements AutomatonBuilder<NFA> {
    private static final NFABuilder INSTANCE = new NFABuilder();

    /**
     * Gets the singleton instance.
     */
    public static NFABuilder getInstance() {
        return INSTANCE;
    }

    /**
     * Creates an NFA from a regular expression.
     */
    public NFA fromRegex(String regex) {
        if (regex == null) {
            throw new IllegalArgumentException("Regex cannot be null");
        }
        if (regex.isEmpty()) {
            return epsilon();
        }
        if (regex.length() == 1 && RegexParser.isOperand(regex.charAt(0))) {
            return fromSymbol(regex.charAt(0));
        }

        String postfix = RegexParser.toPostfix(regex);
        Stack<NFA> stack = new Stack<>();
        
        for (char ch : postfix.toCharArray()) {
            if (RegexParser.isOperand(ch)) {
                stack.push(fromSymbol(ch));
            } else if (ch == '|' || ch == '.') {
                NFA nfa1 = stack.pop();
                NFA nfa2 = stack.pop();
                stack.push(ch == '|' ? nfa2.union(nfa1) : nfa2.concatenate(nfa1));
            } else if (ch == '*') {
                stack.push(stack.pop().star());
            }
        }
        
        return stack.peek();
    }

    @Override
    public NFA fromSymbol(Character symbol) {
        State startState = new State(symbol.toString() + "1");
        State finalState = new State(symbol.toString() + "2");
        Map<Transition, Set<State>> delta = new HashMap<>();
        delta.put(new Transition(startState, symbol), Set.of(finalState));
        return new NFA(new Alphabet(Set.of(symbol)), Set.of(startState, finalState), 
                      startState, delta, Set.of(finalState));
    }

    @Override
    public NFA epsilon() {
        State state = new State("eps");
        return new NFA(new Alphabet(Set.of()), Set.of(state), state, 
                      new HashMap<>(), Set.of(state));
    }

    @Override
    public NFA empty() {
        State state = new State("empty");
        return new NFA(new Alphabet(Set.of()), Set.of(state), state, 
                      new HashMap<>(), Set.of());
    }

    private NFABuilder() {} // Singleton
} 