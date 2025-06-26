package nfa;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an alphabet for an NFA, which is a set of allowed characters (symbols).
 */
public class Alphabet {
    /**
     * The set of characters that make up the alphabet.
     */
    public Set<Character> alphabet;

    /**
     * Constructs an Alphabet with the given set of characters.
     * @param alphabet the set of characters for this alphabet
     */
    public Alphabet(Set<Character> alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Adds a character to the alphabet.
     * @param c the character to add
     */
    public void add(char c) {
        alphabet.add(c);
    }

    /**
     * Removes a character from the alphabet.
     * @param c the character to remove
     */
    public void remove(char c) {
        alphabet.remove(c);
    }

    /**
     * Checks if the alphabet contains a given character.
     * @param c the character to check
     * @return true if the character is in the alphabet, false otherwise
     */
    public boolean contains(char c) {
        return alphabet.contains(c);
    }

    /**
     * Merges two alphabets into a new Alphabet containing all unique characters from both.
     * @param a the first alphabet
     * @param b the second alphabet
     * @return a new Alphabet containing all characters from both a and b
     */
    public static Alphabet merge(Alphabet a, Alphabet b) {
        Set<Character> merged = new HashSet<>();
        merged.addAll(b.alphabet);
        merged.addAll(a.alphabet);
        return new Alphabet(merged);
    }

    /**
     * Returns the set of characters in this alphabet.
     * @return the set of characters
     */
    public Set<Character> getAlphabet() {
        return alphabet;
    }

    /**
     * Returns a string representation of the alphabet.
     * @return a string listing all characters in the alphabet
     */
    @Override
    public String toString() {
        return alphabet.stream().map(String::valueOf).collect(Collectors.joining(",","{","}"));
    }
}
