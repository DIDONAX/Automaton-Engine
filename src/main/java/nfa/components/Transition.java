package nfa.components;

import java.util.Objects;

/**
 * Represents a transition in an automaton.
 */
public class Transition {
    private final State startState;
    private final char symbol;
    private final boolean isEpsilon;

    public Transition(State startState, char symbol) {
        this.startState = startState;
        this.symbol = symbol;
        this.isEpsilon = false;
    }

    public Transition(State startState) {
        this.startState = startState;
        this.symbol = '\0';
        this.isEpsilon = true;
    }

    public State getStartState() {
        return startState;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isEpsilon() {
        return isEpsilon;
    }

    @Override
    public String toString() {
        return "(" + startState + ", " + (isEpsilon ? "ε" : symbol) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return symbol == that.symbol && 
               isEpsilon == that.isEpsilon && 
               Objects.equals(startState, that.startState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startState, symbol, isEpsilon);
    }
} 