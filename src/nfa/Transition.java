package nfa;

/**
 * Represents a transition in an NFA or DFA, which can be a symbol transition or an epsilon transition.
 */
public class Transition {
    /**
     * The starting state of the transition.
     */
    private State startState;
    /**
     * The symbol for the transition. Ignored if this is an epsilon transition.
     */
    private char symbol;
    /**
     * Indicates if this transition is an epsilon (empty string) transition.
     */
    private boolean isEpsilon;

    /**
     * Constructs a symbol transition from a start state with a given symbol.
     * @param startState the starting state
     * @param symbol the symbol for the transition
     */
    public Transition(State startState, char symbol) {
        this.startState = startState;
        this.symbol = symbol;
        this.isEpsilon = false;
    }
    /**
     * Constructs an epsilon transition from a start state.
     * @param startState the starting state
     */
    public Transition(State startState) {
        this.startState = startState;
        this.isEpsilon = true;
    }
    /**
     * Returns the starting state of the transition.
     * @return the starting state
     */
    public State getStartState() {return startState;}
    /**
     * Returns the symbol for the transition.
     * @return the symbol
     */
    public char getSymbol() {return symbol;}
    /**
     * Checks if this is an epsilon transition.
     * @return true if epsilon, false otherwise
     */
    public boolean isEpsilon() {return isEpsilon;}
    /**
     * Returns a string representation of the transition.
     * @return a string describing the transition
     */
    @Override
    public String toString() {
        return "("+ startState.toString() + ", " + symbol + ")";
    }

    /**
     * Checks if this transition is equal to another object.
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        return getStartState().equals(((Transition) obj).getStartState()) && getSymbol() == ((Transition) obj).getSymbol();
    }
}
