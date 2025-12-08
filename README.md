CW2025 Tetris Coursework Project – README
GitHub Repository

https://github.com/ZhenZhang6/CW2025

Compilation Instructions
Requirements

Java JDK 17 or above

Maven
(Project uses javafx-maven-plugin to run)

Build the project
mvn clean package

Run the game
mvn javafx:run

Implemented and Working Properly

The following features have been completed and are functioning normally:

1. Basic Game Features

Brick movement, rotation, and dropping

Collision detection

Line clearing

Scoring system

Game over detection

Next piece preview

These are core mechanics, all of which have been reorganized and are fully operational.

2. Added a special block: BombBrick

I have added an additional optional block:

- Very low appearance probability
- Displayed in rainbow gradient colors
- If a row contains this block, the row's score will be doubled
- If there are multiple BombBricks in a row, the multiplier will continue to stack

For example: 2 → ×4, 3 → ×8

Related code locations:

- BombBrick.java
- RandomBrickGenerator.java
- RowClearManager.java

The generation and display of BombBrick are now running stably.

3. Game Logic Refactoring

The original template wrote a large amount of logic into a single class, making it difficult to maintain. I broke the logic into several smaller modules:

MoveManager

RotateManager

SpawnManager

CollisionDetector

MergeEngine

RowClearManager

GameBoard now only coordinates these modules rather than handling all the logic itself. The refactored structure is clearer and makes it easier to add new features.

4. Dynamic Falling Speed

The game automatically speeds up based on the number of lines cleared:

Initial speed: 400ms

Every 5 lines cleared → speed increases

Minimum speed: 180ms

The gameplay feels smoother and the difficulty gradually increases.

5. Interface and Interaction Optimization

The interface has received several enhancements:

Visuals

- BombBrick rainbow effect
- Improved button styles, colors, and borders
- New right-side info panel: Score / Lines / High Score / Speed
- Optimized preview style for the 'Next Block'

Functionality

- Pause / Resume button
- Bonus jump animation when a line is cleared
- Display of current falling speed
- Added 'Return to Menu' button to go back to the start screen

- Implemented but Not Working Properly

Currently, all implemented features are functioning correctly, and there are no features that are 'implemented but not working properly.

Features Not Implemented

The following features have not been added yet (due to time constraints or because they are not part of the main goals):

- Sound system
- Level/theme switching
- More complex rotation buffering (wall kick)

- New Java Classes
1. BombBrick.java

Location: src/main/java/com/comp2042/logic/bricks/
This class defines the new special block I added for the coursework.
It appears randomly during gameplay and is rendered using a rainbow gradient.
When a row containing this block is cleared, the score for that row is multiplied.
This class mainly provides the block’s shape and its colour code.

2. MoveManager.java

Location: src/main/java/com/comp2042/logic/
Handles left, right, and down movement of the active brick.
It replaces the movement logic that was originally mixed inside GameBoard.

3. RotateManager.java

Location: src/main/java/com/comp2042/logic/
A dedicated class for handling brick rotation.
It keeps rotation rules clean and avoids clutter inside GameBoard.

4. SpawnManager.java

Location: src/main/java/com/comp2042/logic/
Creates new bricks when needed and positions them at the starting location.
Originally this was handled inside GameBoard, but separating it makes the code much clearer.

5. MergeEngine.java

Location: src/main/java/com/comp2042/logic/
Responsible for merging a landed brick into the board matrix.
Extracting this logic prevents GameBoard from doing too many things at once.

6. RowClearManager.java

Location: src/main/java/com/comp2042/logic/
Handles row detection and clearing.
This is also where the BombBrick multiplier logic is applied.

7. CollisionDetector.java

Location: src/main/java/com/comp2042/logic/
Centralised collision detection so movement and rotation can reuse the same checks.

Modified Java Classes
1. GameBoard.java

I reorganised GameBoard so it works mainly as a coordinator.
Before refactoring, GameBoard handled almost all gameplay logic inside itself.
After the update, it now delegates tasks to the new manager classes (MoveManager, RotateManager, SpawnManager, etc.).
It also now supports BombBrick, next-piece preview, and score multipliers.

2. GuiController.java

Major UI updates were added here:

score, lines, and highest-score display

next-piece preview box

rainbow rendering for BombBrick

pause/resume logic

return-to-menu button

smoother rendering to avoid flicker

updated visuals to match CSS

This was necessary because the original controller only supported very basic drawing.

3. RandomBrickGenerator.java

Modified so that it can produce BombBrick with a small probability.
Also stores a queue of bricks to support the next-piece preview feature.

4. RowClearManager.java

Originally just cleared lines.
Now calculates score bonuses and applies BombBrick multipliers.

5. gameLayout.fxml / window_style.css

Both were updated to fit the new UI:
new buttons, next-piece box, visual effects, and styling.

Unexpected Problems
1. BombBrick rendering issues

At first the BombBrick wouldn’t show rainbow colours because the UI used fixed colour codes.
I had to modify GuiController to support LinearGradient for special blocks.

2. Brick flickering when rotating

Sometimes the brick flickered during rotation because the UI reused the old Rectangle grid.
To fix this, I rebuilt the brickPanel whenever the shape changed.

3. Next-piece preview not updating

The preview lagged behind because the RandomBrickGenerator didn’t maintain a queue at the start.
I added a two-brick queue system to fix the timing.

4. Block collisions acting strange

After refactoring, some movement logic broke because collision checks weren’t shared.
Creating CollisionDetector solved this problem and removed duplicated code.

5. Falling speed becoming inconsistent

Changing the Timeline speed caused jumps and pauses.
I fixed this by recreating the Timeline entirely whenever the fall speed changes.

6. FXML buttons not appearing in the right spot

The layout shifted because the original template mixed absolute and GridPane alignment.
I reorganised the UI and cleaned the FXML to get consistent positions.
