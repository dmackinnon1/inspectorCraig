# inspectorCraig
This project provides a generator for logic puzzles in the style of "The Case Files of Inspector Craig" and others from "What is the Name of this Book?" and "The Lady or The Tiger?" by Raymond M. Smullyan.

## Generating the Puzzle Data
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

## The Case Files of Inspector Craig
In these problems, readers are invited to solve a crime based on a list of suspects and a set of clues about who is involved in the crime. The suspects may ultimately be guilty, innocent, or have undetermined guilt or innocence based on the evidence provided.

The "3 phrase" problems, that involve *A*, *B*, and *C*, are deterministically generated based on problems found in Smullyan's book. The "4 phrase" problems, which add an additional character who goes by *D*, have a certain amount of randomness in their generation. Because of the random element in generating the "4 phrase" problems, some generated problems may be rejected, either for being inconsistent or for having too many clues. Every build will generate the same "3 phrase" problem set, but may generate a different "4 phrase" problem set on subsequent runs.

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

### Solving the Puzzles
Although you can read the raw puzzle data, the intention is that you will interact with the puzzle interface provided by **index.html**. This page loads the puzzle data and presents the puzzles one at a time in an interactive format.

A live version of the page can be found at https://dmackinnon1.github.io/inspectorCraig/


## Tiger or Treasure?
These puzzles are inspired by puzzles in "The Lady or the Tiger?" in which a king tries prisoners by asking them to select from a set of doors that either lead to a lady or a tiger. If they choose the tiger, they perish, and if they choose the lady they are freed and allowed to marry the lady. 

The puzzles here have modified the story somewhat:
> We imagine that the women involved in this unfortunate situation have freed themselves and moved into their own castle, taking the tigers with them. Travelers who stay at the castle repay their hosts by cleaning one of two rooms. Each room either contains treasure, from which the kind hosts allow a guest to take a token to remember their stay, or one of the rescued tigers, which are sometimes unchained and always messy. It may turn out that both rooms contain treasure, both contain tigers, or one contains treasure while the other contains a tiger. 

> In this puzzle, you are a traveller who is staying at the castle. Use the inscriptions on the doors to decide whether each leads to treasure or a tiger; or you may find that the contents cannot be determened from the inscriptions. An additional twist: if the fist door leads to treasure, its inscription is true, but if it leads to a tiger, its inscription is false; if the second door leads to treasure its inscription is false, but if it leads to a tiger its inscription is true.

The generated puzzles are written out like the one shown below.
```
{
  "door1_clue": "at least one room has treasure",
  "door2_clue": "the other room has treasure",
  "door1_propositions": [
    "(D1 -> [D1, D2])",
    "(-D1 -> <-D1, -D2>)"
  ],
  "door2_propositions": [
    "(D2 -> -D1)",
    "(-D2 -> D1)"
  ],
  "solution": [
    "D1",
    "-D2"
  ],
  "solution_text": "Door 1 has treasure. Door 2 has a tiger.",
  "description": "Puzzle 11"
}
```
### Solving the Puzzles
The interface for the tiger and treasure puzzles is provided by 
**tiger.html**. This page loads the puzzle data and presents the puzzles one at time.

A live version of the page can be found at https://dmackinnon1.github.io/inspectorCraig/tiger.html

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

