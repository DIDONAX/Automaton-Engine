package nfa.automaton;

import nfa.components.Alphabet;
import nfa.components.State;
import nfa.components.Transition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Deterministic Finite Automaton implementation.
 * A DFA is a special case of an NFA where:
 * 1. There are no epsilon transitions
 * 2. For each state and symbol, there is exactly one next state
 */
public class DFA extends NFA {
    // Flag to control minimization
    private final boolean skipMinimization;

    /**
     * Creates a new, minimized DFA from the given components.
     * This is the official way to construct a DFA.
     */
    public static DFA create(Alphabet alphabet, Set<State> states, State startState,
                             Map<Transition, Set<State>> transitionFunction, Set<State> finalStates) {
        // Create a temporary, non-minimized DFA to run the algorithm on.
        DFA tempDfa = new DFA(alphabet, states, startState, transitionFunction, finalStates, true);
        return tempDfa.minimize();
    }

    /**
     * Private constructor to be used internally for initial creation before minimization.
     * The skipMinimization flag is used to prevent recursion.
     */
    private DFA(Alphabet alphabet, Set<State> states, State startState,
               Map<Transition, Set<State>> transitionFunction, Set<State> finalStates, boolean skipMinimization) {
        super(alphabet, states, startState, transitionFunction, finalStates);
        this.skipMinimization = skipMinimization;
        validateDFA();
    }

    @Override
    public boolean accepts(String input) {
        State currentState = startState;
        for (char symbol : input.toCharArray()) {
            if (!alphabet.contains(symbol)) {
                return false;
            }
            Transition t = new Transition(currentState, symbol);
            Set<State> nextStates = transitionFunction.get(t);
            if (nextStates == null || nextStates.isEmpty()) {
                return false;
            }
            currentState = nextStates.iterator().next();
        }
        return finalStates.contains(currentState);
    }

    @Override
    public DFA union(NFA other) {
        return super.union(other).toDFA();
    }

    @Override
    public DFA concatenate(NFA other) {
        return super.concatenate(other).toDFA();
    }

    @Override
    public DFA star() {
        return super.star().toDFA();
    }

    /**
     * Minimizes the DFA using Hopcroft's algorithm.
     * @return a new DFA that is equivalent to the original but has the minimum number of states.
     */
    public DFA minimize() {
        // Step 1: Remove unreachable states.
        Set<State> reachableStates = findReachableStates();
        if (reachableStates.size() < states.size()) {
            // If there are unreachable states, create an equivalent DFA with only reachable states first.
            return createReachableDFA(reachableStates).minimize();
        }

        // Step 2: Initial partition into final and non-final states.
        List<Set<State>> partitions = new ArrayList<>();
        Set<State> finalStates = new HashSet<>(this.finalStates);
        Set<State> nonFinalStates = new HashSet<>(states);
        nonFinalStates.removeAll(finalStates);

        if (!finalStates.isEmpty()) partitions.add(finalStates);
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);

        // Step 3: Refine partitions until no more changes occur.
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < partitions.size(); i++) {
                Set<State> partition = partitions.get(i);
                if (partition.size() <= 1) continue;

                for (char symbol : alphabet.getSymbols()) {
                    Map<Integer, Set<State>> splits = new HashMap<>();
                    for (State state : partition) {
                        Transition t = new Transition(state, symbol);
                        State nextState = transitionFunction.get(t).iterator().next();
                        int targetPartitionIndex = findPartitionIndex(partitions, nextState);
                        splits.computeIfAbsent(targetPartitionIndex, k -> new HashSet<>()).add(state);
                    }

                    if (splits.size() > 1) {
                        partitions.remove(i);
                        partitions.addAll(splits.values());
                        changed = true;
                        i--; // Re-check the current index as it now contains a new partition
                        break; // Restart with the new partitions
                    }
                }
                if (changed) break;
            }
        }

        return buildMinimizedDFA(partitions);
    }

    private int findPartitionIndex(List<Set<State>> partitions, State state) {
        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i).contains(state)) {
                return i;
            }
        }
        return -1; // Should not happen for a complete DFA
    }

    private Set<State> findReachableStates() {
        Set<State> reachable = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        reachable.add(startState);
        queue.add(startState);

        while (!queue.isEmpty()) {
            State current = queue.poll();
            for (char symbol : alphabet.getSymbols()) {
                transitionFunction.entrySet().stream()
                    .filter(e -> e.getKey().getStartState().equals(current) && e.getKey().getSymbol() == symbol)
                    .flatMap(e -> e.getValue().stream())
                    .filter(reachable::add)
                    .forEach(queue::add);
            }
        }
        return reachable;
    }

    private DFA createReachableDFA(Set<State> reachableStates) {
        Set<State> reachableFinalStates = finalStates.stream().filter(reachableStates::contains).collect(Collectors.toSet());
        Map<Transition, Set<State>> reachableTransitions = transitionFunction.entrySet().stream()
                .filter(e -> reachableStates.contains(e.getKey().getStartState()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // Pass 'true' to the private constructor to avoid minimization here, as it's an intermediate step.
        return new DFA(alphabet, reachableStates, startState, reachableTransitions, reachableFinalStates, true);
    }

    private DFA buildMinimizedDFA(List<Set<State>> partitions) {
        Map<Set<State>, State> partitionMap = new HashMap<>();
        int stateCounter = 0;
        for (Set<State> p : partitions) {
            partitionMap.put(p, new State("q" + stateCounter++));
        }

        Set<State> newStates = new HashSet<>(partitionMap.values());
        State newStart = partitionMap.get(partitions.stream().filter(p -> p.contains(startState)).findFirst().get());
        Set<State> newFinals = partitions.stream()
                .filter(p -> p.stream().anyMatch(finalStates::contains))
                .map(partitionMap::get)
                .collect(Collectors.toSet());

        Map<Transition, Set<State>> newTransitions = new HashMap<>();
        for (Set<State> p : partitions) {
            State from = partitionMap.get(p);
            State representative = p.iterator().next();
            for (char symbol : alphabet.getSymbols()) {
                Transition t = new Transition(representative, symbol);
                State to = partitionMap.get(partitions.stream()
                        .filter(np -> np.contains(transitionFunction.get(t).iterator().next()))
                        .findFirst().get());
                newTransitions.put(new Transition(from, symbol), Set.of(to));
            }
        }
        // Pass 'true' to the private constructor to prevent re-minimizing the already minimal DFA.
        return new DFA(alphabet, newStates, newStart, newTransitions, newFinals, true);
    }

    private void validateDFA() {
        if (alphabet == null || states == null || startState == null || 
            transitionFunction == null || finalStates == null) {
            return; // Allow partial construction
        }

        // Check for epsilon transitions
        if (transitionFunction.keySet().stream().anyMatch(Transition::isEpsilon)) {
            throw new IllegalArgumentException("DFA cannot have epsilon transitions");
        }

        // Check for determinism
        for (State state : states) {
            for (char symbol : alphabet.getSymbols()) {
                Transition t = new Transition(state, symbol);
                Set<State> nextStates = transitionFunction.get(t);
                if (nextStates == null || nextStates.size() != 1) {
                    throw new IllegalArgumentException(
                        String.format("DFA must have exactly one transition for state %s and symbol %c", 
                            state, symbol));
                }
            }
        }
    }
} 