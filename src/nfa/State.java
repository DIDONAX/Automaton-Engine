package nfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a state in an NFA or DFA.
 */
public class State {
    String name;

    /**
     * Constructs a state with the given name.
     * @param name the name of the state
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the state as a string.
     * @return the name of the state
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Creates a new state with the given name.
     * @param name the name of the state
     * @return a new State instance
     */
    public static State create(String name) {
        return new State(name);
    }

    /**
     * Creates a list of states with names as numbers from 0 to numberOfStates-1.
     * @param numberOfStates the number of states to create
     * @return a list of State instances
     */
    public static List<State> create(int numberOfStates) {
        List<State> result = new ArrayList<>();
        for (int i = 0; i < numberOfStates; i++) {
            State s = new State(String.valueOf(i));
            result.add(s);
        }
        return result;
    }
}
