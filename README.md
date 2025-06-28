# NFA and Regex to NFA Converter

This project provides a Java implementation for:
- Converting regular expressions to Non-deterministic Finite Automata (NFA)
- Manipulating and operating on NFAs (concatenation, union, star)
- Converting NFAs to DFAs
- Testing string acceptance by NFAs/DFAs

## Features
- **Regex to NFA**: Uses the Shunting Yard algorithm to parse regular expressions and build corresponding NFAs.
- **NFA Operations**: Supports concatenation, union (|), and Kleene star (*) operations.
- **NFA to DFA**: Converts an NFA to a DFA for efficient string acceptance checking.
- **JUnit Tests**: Comprehensive test suite for regex parsing, NFA construction, and acceptance.

## Project Structure
- `src/main/java/nfa/` — Source code for NFA, regex parsing, and conversion logic
- `src/test/java/nfa/` — JUnit tests for the implementation


## Example Usage
The `Main.java` demonstrates how to:
- Build an NFA from a regex
- Print the NFA
- Test string acceptance
- Perform NFA operations (concatenation, union, star)
- Convert an NFA to a DFA

```java
String regex = "(a|b)*abb";
NFA nfaFromRegex = RegexToNFA.build(regex);
System.out.println("NFA built from regex '(a|b)*abb':\n" + nfaFromRegex);

String[] testStrings = {"abb", "aabb", "babb", "ab", "bba"};
for (String s : testStrings) {
    boolean accepted = nfaFromRegex.accepts(s);
    System.out.println("Does the NFA accept '" + s + "'? " + accepted);
}
```

## Running Tests
JUnit tests are provided in `src/test/java/nfa/RegexToNFATest.java`.
To run the tests, use your IDE's test runner or a build tool like Gradle or Maven (if configured).

## .gitignore
A `.gitignore` file is included to exclude build outputs, IDE files, and other non-source files from your repository.

## License
This project is provided for educational purposes. Feel free to use and modify it as needed. 