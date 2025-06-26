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
} 