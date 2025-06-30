package nfa;

import nfa.automaton.DFA;
import nfa.automaton.NFA;
import nfa.builder.NFABuilder;

/**
 * Demonstrates the usage of the automata library.
 */
public class Main {
    public static void main(String[] args) {
        NFABuilder builder = NFABuilder.getInstance();

        // Build an NFA from a regular expression
        String regex = "(a|b)*abb";
        NFA nfa = builder.fromRegex(regex);
        System.out.println("NFA built from regex '" + regex + "':\n" + nfa);

        // Test string acceptance
        String[] testStrings = {"abb", "aabb", "babb", "ab", "bba"};
        for (String s : testStrings) {
            boolean accepted = nfa.accepts(s);
            System.out.println("Does NFA accept '" + s + "'? " + accepted);
        }

        // Demonstrate NFA operations
        NFA nfaA = builder.fromRegex("a");
        NFA nfaB = builder.fromRegex("b");
        
        NFA concat = nfaA.concatenate(nfaB);
        System.out.println("\nNFA for 'ab':\n" + concat);
        DFA dfaconcat = concat.toDFA();
        System.out.println("\nDFA for 'ab':\n" + dfaconcat);



        NFA union = nfaA.union(nfaB);
        System.out.println("\nNFA for 'a|b':\n" + union);
        DFA dfaunion = union.toDFA();
        System.out.println("\nDFA for 'a|b':\n" + dfaunion);
        
        NFA star = nfaA.star();
        System.out.println("\nNFA for 'a*':\n" + star);
        DFA dfastar = star.toDFA();
        System.out.println("\nDFA for 'a*':\n" + dfastar);

        // Convert NFA to DFA
        DFA dfa = nfa.toDFA();
        System.out.println("\nDFA for '" + regex + "':\n" + dfa);

        // Minimize the DFA and test it
        DFA minimizedDFA = dfa.minimize();
        System.out.println("\nMinimized DFA:\n" + minimizedDFA);

        // Verify that the minimized DFA gives the same results
        System.out.println("\n--- Testing Minimized DFA ---");
        for (String s : testStrings) {
            boolean accepted = minimizedDFA.accepts(s);
            System.out.println("Does minimized DFA accept '" + s + "'? " + accepted);
        }
    }
} 