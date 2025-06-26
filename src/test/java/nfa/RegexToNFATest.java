package nfa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains JUnit tests for the RegexToNFA and NFA classes.
 */
public class RegexToNFATest {

    @Test
    void testSimpleRegexAcceptance() {
        NFA nfa = RegexToNFA.build("a");
        assertTrue(nfa.accepts("a"), "Should accept 'a'");
        assertFalse(nfa.accepts("b"), "Should not accept 'b'");
        assertFalse(nfa.accepts(""), "Should not accept empty string");
        assertFalse(nfa.accepts("aa"), "Should not accept 'aa'");
    }

    @Test
    void testStarOperator() {
        NFA nfa = RegexToNFA.build("a*");
        assertTrue(nfa.accepts(""), "Should accept empty string for 'a*'");
        assertTrue(nfa.accepts("a"), "Should accept 'a' for 'a*'");
        assertTrue(nfa.accepts("aaaa"), "Should accept 'aaaa' for 'a*'");
        assertFalse(nfa.accepts("b"), "Should not accept 'b' for 'a*'");
    }

    @Test
    void testOrOperator() {
        NFA nfa = RegexToNFA.build("a|b");
        assertTrue(nfa.accepts("a"), "Should accept 'a' for 'a|b'");
        assertTrue(nfa.accepts("b"), "Should accept 'b' for 'a|b'");
        assertFalse(nfa.accepts(""), "Should not accept empty string for 'a|b'");
        assertFalse(nfa.accepts("ab"), "Should not accept 'ab' for 'a|b'");
    }

    @Test
    void testConcatenationAndComplexRegex() {
        NFA nfa = RegexToNFA.build("(a|b)*abb");
        assertTrue(nfa.accepts("abb"), "Should accept 'abb'");
        assertTrue(nfa.accepts("aabb"), "Should accept 'aabb'");
        assertTrue(nfa.accepts("babb"), "Should accept 'babb'");
        assertTrue(nfa.accepts("ababb"), "Should accept 'ababb'");
        assertFalse(nfa.accepts("ab"), "Should not accept 'ab'");
        assertFalse(nfa.accepts("bba"), "Should not accept 'bba'");
    }

    @Test
    void testRejectionOfInvalidChars() {
        NFA nfa = RegexToNFA.build("a*b");
        assertFalse(nfa.accepts("acb"), "Should reject string with char 'c' not in alphabet");
    }

    @Test
    void testEmptyRegex() {
        NFA nfa = RegexToNFA.build("");
        assertNotNull(nfa, "Building an empty regex should not return null");
        assertTrue(nfa.accepts(""), "NFA from empty regex should accept empty string");
        assertFalse(nfa.accepts("a"), "NFA from empty regex should not accept 'a'");
    }

    @Test
    void testToDFAConversion() {
        // NFA for the regex (a|b)*abb
        NFA nfa = RegexToNFA.build("(a|b)*abb");
        assertNotNull(nfa, "NFA should not be null");

        // Convert to DFA
        NFA dfa = nfa.toDFA();
        assertNotNull(dfa, "DFA should not be null");

        // The resulting DFA should accept the same language
        assertTrue(dfa.accepts("abb"), "DFA should accept 'abb'");
        assertTrue(dfa.accepts("aabb"), "DFA should accept 'aabb'");
        assertTrue(dfa.accepts("babb"), "DFA should accept 'babb'");
        assertTrue(dfa.accepts("ababb"), "DFA should accept 'ababb'");

        // The resulting DFA should reject strings not in the language
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
        assertFalse(dfa.accepts("bba"), "DFA should not accept 'bba'");
        assertFalse(dfa.accepts("abca"), "DFA should not accept string with invalid characters");
    }

    @Test
    void testNestedStarAndUnion() {
        NFA nfa = RegexToNFA.build("(a(b|c))*");
        assertTrue(nfa.accepts(""), "Should accept empty string");
        assertTrue(nfa.accepts("ab"), "Should accept 'ab'");
        assertTrue(nfa.accepts("ac"), "Should accept 'ac'");
        assertTrue(nfa.accepts("abac"), "Should accept 'abac'");
        assertTrue(nfa.accepts("acab"), "Should accept 'acab'");
        assertFalse(nfa.accepts("a"), "Should not accept 'a'");
        assertFalse(nfa.accepts("b"), "Should not accept 'b'");
        assertFalse(nfa.accepts("abc"), "Should not accept 'abc'");
    }

    @Test
    void testConcatenatedUnions() {
        NFA nfa = RegexToNFA.build("(a|b)(c|d)");
        assertTrue(nfa.accepts("ac"), "Should accept 'ac'");
        assertTrue(nfa.accepts("ad"), "Should accept 'ad'");
        assertTrue(nfa.accepts("bc"), "Should accept 'bc'");
        assertTrue(nfa.accepts("bd"), "Should accept 'bd'");
        assertFalse(nfa.accepts("a"), "Should not accept 'a'");
        assertFalse(nfa.accepts("cd"), "Should not accept 'cd'");
        assertFalse(nfa.accepts("abcd"), "Should not accept 'abcd'");
    }

    @Test
    void testLongerCombinedPattern() {
        NFA nfa = RegexToNFA.build("(ab|c)*de");
        assertTrue(nfa.accepts("de"), "Should accept 'de'");
        assertTrue(nfa.accepts("abde"), "Should accept 'abde'");
        assertTrue(nfa.accepts("cde"), "Should accept 'cde'");
        assertTrue(nfa.accepts("abccde"), "Should accept 'abccde'");
        assertTrue(nfa.accepts("cabcde"), "Should accept 'cabcde'");
        assertFalse(nfa.accepts(""), "Should not accept empty string");
        assertFalse(nfa.accepts("d"), "Should not accept 'd'");
        assertFalse(nfa.accepts("e"), "Should not accept 'e'");
        assertFalse(nfa.accepts("abe"), "Should not accept 'abe'");
    }
} 