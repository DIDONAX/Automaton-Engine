package nfa.automaton;

import nfa.components.Alphabet;
import nfa.components.State;
import nfa.components.Transition;
import java.util.Map;
import java.util.Set;

/**
 * Abstract base class for all finite automata.
 */
public abstract class Automaton {
    protected final Alphabet alphabet;
    protected final Set<State> states;
    protected final Map<Transition, Set<State>> transitionFunction;
    protected final State startState;
    protected final Set<State> finalStates;

    protected Automaton(Alphabet alphabet, Set<State> states, 
                       Map<Transition, Set<State>> transitionFunction,
                       State startState, Set<State> finalStates) {
        this.alphabet = alphabet;
        this.states = states;
        this.transitionFunction = transitionFunction;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public Set<State> getStates() {
        return states;
    }

    public Map<Transition, Set<State>> getTransitionFunction() {
        return transitionFunction;
    }

    public State getStartState() {
        return startState;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    /**
     * Checks if the automaton accepts a given input string.
     * This method must be implemented by subclasses.
     *
     * @param input the string to check
     * @return true if the automaton accepts the string, false otherwise
     */
    public abstract boolean accepts(String input);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" {\n");
        sb.append("  Alphabet: ").append(alphabet).append("\n");
        sb.append("  States: ").append(states).append("\n");
        sb.append("  Start State: ").append(startState).append("\n");
        sb.append("  Final States: ").append(finalStates).append("\n");
        sb.append("  Transitions: {\n");
        transitionFunction.forEach((key, value) ->
                sb.append("    ").append(key).append(" -> ").append(value).append("\n"));
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }
} 