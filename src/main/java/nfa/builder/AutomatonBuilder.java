package nfa.builder;

import nfa.automaton.Automaton;

/**
 * Interface for building automata from basic components.
 */
public interface AutomatonBuilder<T extends Automaton> {
    /**
     * Creates an automaton that accepts a single symbol.
     */
    T fromSymbol(Character symbol);

    /**
     * Creates an automaton that accepts the empty string (epsilon).
     */
    T epsilon();

    /**
     * Creates an automaton that accepts nothing (empty language).
     */
    T empty();
} 