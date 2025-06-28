package nfa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Demonstrates the construction and usage of NFAs and their conversion from regular expressions.
 */
public class Main {
    /**
     * The main entry point for the NFA demonstration.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        //TODO: MAKE DFA CONVERSION CLEANER
        //TODO: ADD GUI

        //Test 1: Build an NFA from a regular expression and print it
        String regex = "(a|b)*abb";
        NFA nfaFromRegex = RegexToNFA.build(regex);
        System.out.println("NFA built from regex '(a|b)*abb':\n" + nfaFromRegex);

        //Test 2: Check if the NFA accepts certain strings
        String[] testStrings = {"abb", "aabb", "babb", "ab", "bba"};
        for (String s : testStrings) {
            boolean accepted = nfaFromRegex.accepts(s);
            System.out.println("Does the NFA accept '" + s + "'? " + accepted);
        }
        
        //Test 4: Demonstrate NFA operations: star, or, and concatenation
        NFA nfaA = RegexToNFA.build("a");
        NFA nfaB = RegexToNFA.build("b");
        NFA concatNFA = NFA.concatinateNFA(nfaA, nfaB);
        System.out.println("NFA for concatenation 'ab':\n" + concatNFA);
        NFA orNFA = NFA.orOperation(nfaA, nfaB);
        System.out.println("NFA for union 'a|b':\n" + orNFA);
        NFA starNFA = NFA.starOperation(nfaA);
        System.out.println("NFA for star 'a*':\n" + starNFA);

        //Test 5: Convert an NFA to a DFA and print it
        NFA dfa = nfaFromRegex.toDFA();
        System.out.println("DFA converted from NFA for '(a|b)*abb':\n" + dfa);
    }
}