package nfa.components;

import java.util.Objects;

/**
 * Represents a state in an automaton.
 */
public class State {
    private final String name;

    public State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
} 