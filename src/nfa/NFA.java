package nfa;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a Non-deterministic Finite Automaton (NFA) with epsilon transitions.
 * Provides methods for NFA construction, manipulation, and conversion to DFA.
 */
public class NFA {
    private Alphabet alphabet;
    private Set<State> states;
    private Map<Transition,Set<State>> transitionFunction;
    private State startState;
    private Set<State> finalStates;

    /**
     * Constructs an NFA with the given alphabet, states, start state, transition function, and final states.
     * @param alphabet the alphabet
     * @param states the set of states
     * @param startState the start state
     * @param transitionFunction the transition function
     * @param finalStates the set of final states
     */
    public NFA(Alphabet alphabet, Set<State> states, State startState, Map<Transition,Set<State>> transitionFunction, Set<State> finalStates) {
        this.alphabet = alphabet;
        this.states = states;
        this.transitionFunction = transitionFunction;
        this.startState = startState;
        this.finalStates = finalStates;
    }
    /**
     * Constructs an NFA without specifying the alphabet (used for intermediate operations).
     * @param states the set of states
     * @param startState the start state
     * @param transitionFunction the transition function
     * @param finalState the set of final states
     */
    public NFA(Set<State> states, State startState, Map<Transition,Set<State>> transitionFunction, Set<State> finalState) {
        this.states = states;
        this.transitionFunction = transitionFunction;
        this.startState = startState;
        this.finalStates = finalState;
    }
    /**
     * Returns the transition function of the NFA.
     * @return the transition function
     */
    public Map<Transition,Set<State>> getTransitionFunction() {
        return transitionFunction;
    }
    /**
     * Returns the set of states in the NFA.
     * @return the set of states
     */
    public Set<State> getStates() {
        return states;
    }
    /**
     * Adds a transition from a given transition to a state.
     * @param transition the transition
     * @param state the destination state
     */
    public void setTransition(Transition transition,State state) {
        if (transitionFunction.containsKey(transition)) {
            transitionFunction.get(transition).add(state); // if transition exists in the function
        } else {
            transitionFunction.put(transition,Set.of(state));// if transition doesn't exist in the function
        }
    }
    /**
     * Checks if a state has an outgoing epsilon transition.
     * @param state the state to check
     * @return true if the state has an epsilon transition, false otherwise
     */
    private boolean isEpsilonState(State state) {
        return transitionFunction.keySet().stream().anyMatch(t -> t.isEpsilon() && t.getStartState().equals(state));
    }
    /**
     * Removes epsilon states from a set of states by following epsilon transitions.
     * @param states the set of states
     * @return the set of states with epsilon transitions removed
     */
    private Set<State> removeEpsilonStates(Set<State> states) {
        while (states.stream().anyMatch(s-> isEpsilonState(s))) {
            System.out.println("Removing epsilon" + states);
            Set<State> epsilonStates = states.stream().filter(s-> isEpsilonState(s)).collect(Collectors.toSet());
            states = states.stream().filter(s-> !isEpsilonState(s)).collect(Collectors.toSet());
            for (State state : epsilonStates) {
                states.addAll(transitionFunction.keySet().stream().filter(t -> t.getStartState().equals(state) && t.isEpsilon()).map(transitionFunction::get).flatMap(Set::stream).collect(Collectors.toSet()));
            }
            System.out.println("Epsilon removed" + states);
        }
        return states;
    }
    /**
     * Computes the set of next states for a given set of states and an input symbol.
     * @param states the current set of states
     * @param symbol the input symbol
     * @return the set of next states
     */
    public Set<State> next(Set<State> states, char symbol) {
        states = removeEpsilonStates(states);
        Set<State> nextStates = new HashSet<>();
        System.out.println("Before next for input"+symbol+ states);
        for (State state : states) {
            nextStates.addAll(transitionFunction.keySet().stream()
                    .filter(transition -> transition.getStartState().equals(state) && (transition.getSymbol() == symbol))
                    .map(transitionFunction::get).flatMap(Set::stream)
                    .collect(Collectors.toSet()));
            System.out.println(nextStates);
        }
        System.out.println("After next for input "+ symbol+ nextStates);
        return nextStates;
    }
    /**
     * Checks if the NFA accepts a given input string.
     * @param string the input string
     * @return true if the NFA accepts the string, false otherwise
     */
    public boolean accepts(String string) {
        Set<State> current = Set.of(startState);
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            System.out.println("TESTING FOR: " + ch);
            if (!alphabet.contains(ch)) {
                return false;
            }
            current = next(current, ch);
            System.out.println(current);
        }
        return !current.isEmpty() && current.stream().anyMatch(state -> finalStates.contains(state));
    }
    /**
     * Concatenates two NFAs into a new NFA.
     * @param a the first NFA
     * @param b the second NFA
     * @return the concatenated NFA
     */
    public static NFA concatinateNFA(NFA a, NFA b) {
        NFA result = merge(a,b);
        Set<State> finalStates = new HashSet<>(b.getFinalStates());
        result.setStartState(a.getStartState());
        result.setFinalStates(finalStates);

        for (State state : a.getFinalStates()) {
            result.setTransition(new Transition(state),b.getStartState());
        }
        return result;
    }
    /**
     * Builds an NFA for a single symbol.
     * @param symbol the symbol
     * @return the constructed NFA
     */
    public static NFA buildNFA(Character symbol) {
        State startState = new State(symbol.toString()+"1");
        State finalState = new State(symbol.toString()+"2");
        Map<Transition,Set<State>> delta = new HashMap<>();
        delta.put(new Transition(startState,symbol),Set.of(finalState));
        return new NFA(new Alphabet(Set.of(symbol)), Set.of(startState, finalState), startState,delta,Set.of(finalState));
    }
    /**
     * Applies the Kleene star operation to an NFA.
     * @param a the NFA to star
     * @return the resulting NFA
     */
    public static NFA starOperation(NFA a) {
        NFA result = new NFA(a.getAlphabet(),a.getStates(),a.getStartState(),a.getTransitionFunction(),a.getFinalStates());
        for (State state : a.getFinalStates()) {
            result.setTransition(new Transition(state),a.getStartState());
        }
        return result;
    }
    /**
     * Merges two NFAs into a new NFA (used internally for operations).
     * @param a the first NFA
     * @param b the second NFA
     * @return the merged NFA
     */
    private static NFA merge(NFA a, NFA b) {
        Set<State> states = new HashSet<>(a.getStates());
        states.addAll(b.getStates());
        Alphabet alphabet = Alphabet.merge(a.getAlphabet(), b.getAlphabet());
        a.getTransitionFunction().putAll(b.getTransitionFunction());
        Map<Transition, Set<State>> delta = a.getTransitionFunction();
        return new NFA(alphabet,states,null,delta,null);
    }
    /**
     * Applies the union (or) operation to two NFAs.
     * @param a the first NFA
     * @param b the second NFA
     * @return the resulting NFA
     */
    public static NFA orOperation(NFA a, NFA b) {
        NFA result = merge(a,b);
        State newStart = new State("OR");
        result.addState(newStart);
        Set<State> finalStates = new HashSet<>(a.getFinalStates());
        finalStates.addAll(b.getFinalStates());
        result.setStartState(newStart);
        result.setFinalStates(finalStates);
        result.setTransition(new Transition(newStart),a.getStartState());
        result.setTransition(new Transition(newStart),b.getStartState());
        return result;
    }
    /**
     * Checks if a state is in the destination set for all symbols (used in DFA conversion).
     * @param statesMap the map of states to their destination sets
     * @param state the state to check
     * @return true if in the destination set, false otherwise
     */
    private boolean inDestinationSet(Map<State,Set<State>> statesMap, State state) {
        return this.alphabet.getAlphabet().stream().allMatch(ch -> statesMap.containsValue(next(statesMap.get(state),ch)));
    }
    /**
     * Converts this NFA to a DFA using the subset construction algorithm.
     * @return the equivalent DFA as an NFA object
     */
    public NFA toDFA() {
        Map<State,Set<State>> statesMap = new HashMap<>();
        statesMap.put(startState,Set.of(startState));
        Map<Transition,Set<State>> transitionMap = new HashMap<>();
        NFA DFA = new NFA(alphabet,null,startState,transitionMap,null);
        Set<Character> alphabet = this.alphabet.getAlphabet();
        while (statesMap.keySet().stream().anyMatch(state -> !inDestinationSet(statesMap, state))) {
            Iterator<Character> alphaIterator = alphabet.iterator();
            Set<State> states = statesMap.keySet();
            Iterator<State> statesIterator = states.iterator();
            while (statesIterator.hasNext()) {
                State currentState = statesIterator.next();
                while (alphaIterator.hasNext()) {
                    char ch = alphaIterator.next();
                    if (!statesMap.containsValue(next(statesMap.get(currentState), ch))) {
                        State state = new State("n");
                        statesMap.put(state, next(statesMap.get(currentState), ch));
                    }
                    DFA.setTransition(new Transition(currentState),statesMap.entrySet().stream().filter(e -> e.getValue().equals(next(statesMap.get(currentState),ch))).findAny().get().getKey());
                }
            }
        }
        DFA.setStates(statesMap.keySet());
        Set<State> DFAFinalStates = statesMap.entrySet().stream().filter(e -> e.getValue().stream().anyMatch(s -> finalStates.contains(s))).map(e -> e.getKey()).collect(Collectors.toSet());
        DFA.setFinalStates(DFAFinalStates);
        return DFA;
    }
    /**
     * Adds a state to the NFA.
     * @param state the state to add
     */
    public void addState(State state) {
        states.add(state);
    }
    /**
     * Returns the alphabet of the NFA.
     * @return the alphabet
     */
    public Alphabet getAlphabet() {
        return alphabet;
    }
    /**
     * Returns the set of final (accepting) states.
     * @return the set of final states
     */
    public Set<State> getFinalStates() {
        return finalStates;
    }
    /**
     * Returns the start state of the NFA.
     * @return the start state
     */
    public State getStartState() {
        return startState;
    }
    /**
     * Sets the start state of the NFA.
     * @param startState the new start state
     */
    public void setStartState(State startState) {
        this.startState = startState;
    }
    /**
     * Sets the set of final (accepting) states.
     * @param finalStates the new set of final states
     */
    public void setFinalStates(Set<State> finalStates) {
        this.finalStates = finalStates;
    }
    /**
     * Sets the set of states in the NFA.
     * @param states the new set of states
     */
    public void setStates(Set<State> states) {
        this.states = states;
    }
    /**
     * Returns a string representation of the NFA.
     * @return a string describing the NFA
     */
    @Override
    public String toString() {
        return "( nfa.Alphabet: "+ alphabet.toString() + ", States: " + states.toString() + ", Start nfa.State: " + startState + ", nfa.Transition Function: " + transitionFunction.toString() + ", Final States: " + finalStates.stream().map(State::toString).collect(Collectors.joining(", ","{","}")) + ")";
    }
}
