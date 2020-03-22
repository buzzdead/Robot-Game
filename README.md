![Crawling crow logo](assets/logo.png)

[![Build Status](https://travis-ci.com/inf112-v20/crawling-crow.svg?branch=master)](https://travis-ci.com/inf112-v20/crawling-crow) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/1ef3b79326324c30a5b5b61d5addef5b)](https://www.codacy.com/gh/inf112-v20/crawling-crow?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=inf112-v20/crawling-crow&amp;utm_campaign=Badge_Grade)

Currently a simple and not functional implementation of Robo Rally.

## Deliverables
-   [Obligatorisk oppgave 1](Deliverables/ObligatoriskOppgave1.md)
-   [Obligatorisk oppgave 2](Deliverables/ObligatoriskOppgave2.md)
-   [Obligatorisk oppgave 3](Deliverables/ObligatoriskOppgave3.md)

## Setup
1.  Install [Maven](https://maven.apache.org/download.cgi)
2.  Clone the repository, `git clone git@github.com:inf112-v20/crawling-crow.git`
3.  Build game with dependencies, `mvn clean verify assembly:single`

## How to run
For now, you have to run the game manually.

1.  Run `Main.java` located in `src/main/java/roborally` in your IDE of choice.
    -   Alternatively use the command `java -jar target/roborally-crawlingcrow-1.0-SNAPSHOT-jar-with-dependencies.jar`

2.  Move around using the [controls](#controls)

3.  When you want to exit, press <kbd>ESC</kbd> or exit the game manually by clicking on the **X**-button.

### How to play
#### Controls
##### Movement
Since you can't program the robot yet, you can move the robot with the following keys:

-   Forward: <kbd>&#8593;</kbd>
-   Backward: <kbd>&#8595;</kbd>
-   Turn left: <kbd>&#8592;</kbd>
-   Turn right: <kbd>&#8594;</kbd>

##### Combat
-   Fire laser: <kbd>F</kbd>

##### Other
-   Exit game: <kbd>ESC</kbd>
-   Start game: <kbd>ENTER</kbd>
-   Check if someone won: <kbd>W</kbd>
-   Register flag: <kbd>SPACE</kbd>
-   Activate FunMode: <kbd>X</kbd>
    -   FunMode: Move all robots randomly: <kbd>M</kbd>
    -   Funmode: Fire lasers for all robots: <kbd>A</kbd>

## Tests
### Automatic tests
All of our automatic tests can be found in `src/test/java/roborally`.

### Manual tests (while running the game)
#### Movement test
Please see the [controls for movement](#movement) above.
-   We use these to see if the `Robot` moves as expected with arrow keys
-   Push other `Robot`s around.
-   Check if `Robot` cannot go outside game board (This will be changed later, because of the game rules).
-   Move `Robot` into wall to see if collision works.
-   Push `Robot`s into walls to see if collision works.
-   Push `Robot`s outside of game bord, to see if they go outside

#### Robot's State changes
-   Push other `Robot`s ontop of _holes_ or _flags_ to see if their state (texture) changes.
-   When your `Robot` is ontop of _holes_ or _flags_, check  if their texture changes.

#### Combat test
Please see the [controls for combat](#combat) above
-   Check if lasers fire when looking left or right and if sounds executes by pressing <kbd>F</kbd>. (Works only horizontal atm)
    -   Check if laser stops on `Robot`s and walls.
    
#### Other test
-   Move `Robot` to **flag 1**, press <kbd>SPACE</kbd>, check console print `A flag has been visited` appears.
    -   Continue for the other flags in ascending order.
    -   When all flags has been visited, check if someone has _won_ by pressing <kbd>W</kbd>.

-   Check if someone has _won_ prematurely (without visiting any flags) by pressing <kbd>W</kbd>.
-   Start round by pressing <kbd>ENTER</kbd> (this will wait for program card input, which haven't been implemented yet).
-   Check if game exits/quits by pressing <kbd>ESC</kbd>.
-   Move into laser to see if the laser stops on the cell the `Robot` is currently on, and reappears when it steps out.

## Known bugs
See [Isses with Bug label](/../../issues?q=is%3Aissue+is%3Aopen+laser+label%3Abug)
-   If no `Robot`s have been generated, you will get a `NullPointerException`. See [#38](/../../issues/38).
-   There are two bugs in `AssetsManager.java`. You have to call specific methods. See [#37](/../../issues/37).
-   Lasers only shoot when the robot is either looking left or right, so only horizontal works now. See [#88](/../../issues/88)

### From Maven template
Currently throws "WARNING: An illegal reflective access operation has occurred", 
when the java version used is >8. This has no effect on function or performance, and is just a warning.

## Class diagram
![Oblig2UML](https://user-images.githubusercontent.com/45336748/75541155-b1b1ea80-5a1d-11ea-8a34-e57d7586e76a.png)
