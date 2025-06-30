package nfa.operations;

import nfa.automaton.NFA;

/**
 * Interface defining operations specific to Non-deterministic Finite Automata (NFA).
 */
public interface NFAOperations {
    /**
     * Creates a new NFA that accepts the union of the languages.
     */
    NFA union(NFA other);

    /**
     * Creates a new NFA that accepts the concatenation of the languages.
     */
    NFA concatenate(NFA other);

    /**
     * Creates a new NFA that accepts the Kleene star of the language.
     */
    NFA star();
} 