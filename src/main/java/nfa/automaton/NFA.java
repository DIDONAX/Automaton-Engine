package nfa.automaton;

import nfa.components.Alphabet;
import nfa.components.State;
import nfa.components.Transition;
import nfa.operations.NFAOperations;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Non-deterministic Finite Automaton implementation.
 */
public class NFA extends Automaton implements NFAOperations {
    public NFA(Alphabet alphabet, Set<State> states, State startState,
               Map<Transition, Set<State>> transitionFunction, Set<State> finalStates) {
        super(alphabet, states, transitionFunction, startState, finalStates);
    }

    @Override
    public boolean accepts(String input) {
        Set<State> currentStates = epsilonClosure(Set.of(startState));
        for (char ch : input.toCharArray()) {
            if (!alphabet.contains(ch)) {
                return false;
            }
            currentStates = move(currentStates, ch);
            if (currentStates.isEmpty()) return false;
            currentStates = epsilonClosure(currentStates);
        }
        return currentStates.stream().anyMatch(finalStates::contains);
    }

    @Override
    public NFA union(NFA other) {
        State newStart = new State("union_start");
        Map<Transition, Set<State>> newTransitions = new HashMap<>(this.transitionFunction);
        newTransitions.putAll(other.transitionFunction);
        newTransitions.put(new Transition(newStart), Set.of(this.startState, other.startState));
        
        Set<State> newStates = new HashSet<>(this.states);
        newStates.addAll(other.states);
        newStates.add(newStart);
        
        Set<State> newFinals = new HashSet<>(this.finalStates);
        newFinals.addAll(other.finalStates);
        
        return new NFA(
            Alphabet.merge(this.alphabet, other.alphabet),
            newStates,
            newStart,
            newTransitions,
            newFinals
        );
    }

    @Override
    public NFA concatenate(NFA other) {
        Map<Transition, Set<State>> newTransitions = new HashMap<>(this.transitionFunction);
        newTransitions.putAll(other.transitionFunction);
        
        for (State finalState : this.finalStates) {
            newTransitions.put(new Transition(finalState), Set.of(other.startState));
        }
        
        Set<State> newStates = new HashSet<>(this.states);
        newStates.addAll(other.states);
        
        return new NFA(
            Alphabet.merge(this.alphabet, other.alphabet),
            newStates,
            this.startState,
            newTransitions,
            other.finalStates
        );
    }

    @Override
    public NFA star() {
        State newStart = new State("star_start");
        Map<Transition, Set<State>> newTransitions = new HashMap<>(this.transitionFunction);
        
        newTransitions.put(new Transition(newStart), Set.of(this.startState));
        
        for (State finalState : this.finalStates) {
            newTransitions.put(new Transition(finalState), Set.of(this.startState));
        }
        
        Set<State> newStates = new HashSet<>(this.states);
        newStates.add(newStart);
        
        Set<State> newFinals = new HashSet<>(this.finalStates);
        newFinals.add(newStart);
        
        return new NFA(
            this.alphabet,
            newStates,
            newStart,
            newTransitions,
            newFinals
        );
    }

    public DFA toDFA() {
        Map<Set<State>, State> dfaStateMap = new HashMap<>();
        Queue<Set<State>> worklist = new LinkedList<>();
        Map<Transition, Set<State>> dfaTransitions = new HashMap<>();
        Set<State> dfaStates = new HashSet<>();
        Set<State> dfaFinals = new HashSet<>();

        // 1. Create a dead state for undefined transitions
        State deadState = new State("dead");
        dfaStates.add(deadState);
        for (char symbol : this.alphabet.getSymbols()) {
            dfaTransitions.put(new Transition(deadState, symbol), Set.of(deadState));
        }

        Set<State> startStateSet = epsilonClosure(Set.of(this.startState));
        State dfaStart = new State(stateSetToName(startStateSet));
        dfaStateMap.put(startStateSet, dfaStart);
        dfaStates.add(dfaStart);
        worklist.add(startStateSet);

        while (!worklist.isEmpty()) {
            Set<State> currentNfaStates = worklist.poll();
            State currentDfaState = dfaStateMap.get(currentNfaStates);

            if (currentNfaStates.stream().anyMatch(finalStates::contains)) {
                dfaFinals.add(currentDfaState);
            }

            for (char symbol : this.alphabet.getSymbols()) {
                Set<State> nextNfaStates = epsilonClosure(move(currentNfaStates, symbol));

                if (nextNfaStates.isEmpty()) {
                    // 2. If transition is undefined, go to dead state
                    dfaTransitions.put(new Transition(currentDfaState, symbol), Set.of(deadState));
                } else {
                    State nextDfaState = dfaStateMap.computeIfAbsent(nextNfaStates, k -> {
                        State newState = new State(stateSetToName(k));
                        dfaStates.add(newState);
                        worklist.add(k);
                        return newState;
                    });
                    dfaTransitions.put(new Transition(currentDfaState, symbol),
                                     Set.of(nextDfaState));
                }
            }
        }

        return DFA.create(this.alphabet, dfaStates, dfaStart, dfaTransitions, dfaFinals);
    }

    private Set<State> epsilonClosure(Set<State> states) {
        Set<State> closure = new HashSet<>(states);
        Stack<State> stack = new Stack<>();
        states.forEach(stack::push);

        while (!stack.isEmpty()) {
            State current = stack.pop();
            transitionFunction.entrySet().stream()
                .filter(e -> e.getKey().getStartState().equals(current) 
                    && e.getKey().isEpsilon())
                .flatMap(e -> e.getValue().stream())
                .filter(closure::add)
                .forEach(stack::push);
        }
        return closure;
    }

    private Set<State> move(Set<State> states, char symbol) {
        return states.stream()
            .flatMap(state -> transitionFunction.entrySet().stream()
                .filter(e -> e.getKey().getStartState().equals(state) 
                    && !e.getKey().isEpsilon() 
                    && e.getKey().getSymbol() == symbol)
                .flatMap(e -> e.getValue().stream()))
            .collect(Collectors.toSet());
    }

    private String stateSetToName(Set<State> states) {
        return states.stream()
            .map(State::toString)
            .sorted()
            .collect(Collectors.joining(",", "{", "}"));
    }
} 