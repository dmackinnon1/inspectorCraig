# inspectorCraig
Generator for logic puzzles in the style of "The Case Files of Inspector Craig," from "What is the Name of this Book" by Raymond M. Smullyan.

## Generating the Puzzle Data

This project will generate a set of logic problems based on some of the problems found in chapter 6 of "What is the Name of this Book" by Raymond M. Smullyan.
In these problems, readers are invited to solve a crime based on a list of suspects and a set of clues about who is involved in the crime. The suspects may ultimately be guilty, innocent, or have undetermined guilt or innocence based on the evidence provided.

The puzzles are generated and saved in **.json** files in the **data** directory. These are generated by a Java program that is built and executed by running the **build.sh** script. 

```
> ./build.sh
...starting build
...cleanup
...compiling sources from src/inspectorCraigJava to target directory
...creating generator.jar in target directory
...executing jar to generate puzzle data in data directory
generating 3 phrase problems...
generating 4 phrase problems...
inconsistent: 10
problems considered too large: 46
problems generated: 400
...complete
```
The "3 phrase" problems, that involve *A*, *B*, and *C*, are deterministically generated based on problems found in Smullyan's book. The "4 phrase" problems, that add an additional character who goes by *D* have a certain amount of randomness in their generation, so each run gives a different output for these problems. Because of the randomness in the "4 phrase" problems, some generated problems might get rejected, either for being inconsistent, or having too many clues.

The generated puzzles are written out like the one shown below.

```
{
  "propositions": ["A", "B", "C"], 
  "premises": [
    "C", "(B -> -C)", 
    "(-B -> A)", 
    "[A, B, C]"], 
   "solution": ["A", "-B", "C"], 
   "description": "1204"
}	
```
The *propositions* are the statements that will be used in the puzzle. The *premises* are the clues, and the *solution* are the true statements that can be deduced from the clues.

## Running Unit Tests

Unit tests can be run by executing the **build.sh** script with the **test** argument.
```
> ./build.sh test
...starting build
...cleanup
...compiling sources from src/inspectorCraigJava to target directory
...creating generator.jar in target directory
...executing jar to generate puzzle data in data directory
>> Running TestSet1 - basic operations <<
running test: implication satisfaction, positive case
running test: simple contrapositive
running test: simple union
running test: deMorgan's law
running test: negate implication
running test: double negation
ran 6 tests with no errors
>> Running TestSet2 - simple deductions <<
running test: solver, basic union case
running test: solver, basic implication case
running test: solver, basic contrapositive case
running test: solver, basic union + implication case
running test: solver, union and contradiction
running test: solver, composed implication
ran 6 tests with no errors
...complete
```

## Solving Puzzles
Although you can read the raw puzzle data, the intention is that you will interact with the puzzle interface provided by **index.html**. This page loads the puzzle data and presents the puzzles one at a time in an interactive format.

A live version of the page can be found at (https://dmackinnon1.github.io/inspectorCraig/)
