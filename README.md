# Automaton Engine

A lightweight Java library for converting regular expressions into Non-deterministic Finite Automata (NFAs) and simulating their behavior. This project provides a simple, dependency-free implementation of core automata theory concepts, making it ideal for educational purposes and small-scale applications.

## Features

- **Regex to NFA Conversion**: Convert standard regular expressions into an equivalent NFA using the Shunting Yard and Thompson's construction algorithms.
- **NFA Simulation**: Test whether an NFA accepts or rejects a given input string.
- **Core Automata Operations**: Includes implementations for concatenation, union (or), and the Kleene star operations.
- **DFA Conversion**: Supports converting an NFA into an equivalent Deterministic Finite Automaton (DFA).
- **JUnit Tests**: Comes with a suite of JUnit tests to verify correctness and demonstrate usage.

## Usage

Using the Automaton Engine is straightforward. Here's a quick example of how to build an NFA from a regex and test a string against it:

```java
// Import the necessary classes
import nfa.NFA;
import nfa.RegexToNFA;

public class Example {
    public static void main(String[] args) {
        // 1. Define the regular expression
        String regex = "(a|b)*abb";

        // 2. Build the NFA from the regex
        NFA nfa = RegexToNFA.build(regex);

        // 3. Check if the NFA accepts a string
        boolean isAccepted = nfa.accepts("aabb");

        System.out.println("NFA from regex: " + regex);
        System.out.println("Accepts 'aabb'? " + isAccepted); // Outputs: true
    }
}
```

## Running Tests

This project uses JUnit 5 for testing. To run the tests, you can use your IDE (like IntelliJ or Eclipse) or run them from the command line with Maven or Gradle.

From your IDE:
1.  Make sure you have the JUnit 5 library added to your project's dependencies.
2.  Navigate to the `test/nfa/RegexToNFATest.java` file.
3.  Right-click on the file and select "Run 'RegexToNFATest'".

## Future Improvements

- **Robust Error Handling**: Implement custom exceptions for invalid regex syntax.
- **Visualization**: Add functionality to export an automaton's structure to a format like Graphviz DOT for visualization. 