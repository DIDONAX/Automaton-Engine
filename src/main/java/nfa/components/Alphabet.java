package nfa.components;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an alphabet (set of symbols) for an automaton.
 */
public class Alphabet {
    private final Set<Character> symbols;

    public Alphabet(Set<Character> symbols) {
        this.symbols = new HashSet<>(symbols);
    }

    public void add(char symbol) {
        symbols.add(symbol);
    }

    public void remove(char symbol) {
        symbols.remove(symbol);
    }

    public boolean contains(char symbol) {
        return symbols.contains(symbol);
    }

    public Set<Character> getSymbols() {
        return new HashSet<>(symbols);
    }

    public static Alphabet merge(Alphabet a, Alphabet b) {
        Set<Character> merged = new HashSet<>(a.symbols);
        merged.addAll(b.symbols);
        return new Alphabet(merged);
    }

    @Override
    public String toString() {
        return symbols.toString();
    }
} 