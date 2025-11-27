/**
 * Central game logic manager.
 * Delegates all major gameplay operations (movement, rotation,
 * spawning, merging, clearing) to dedicated manager classes.
 * Keeps track of the active brick's offset and board state only.
 * This structure improves modularity and maintainability compared
 * to the original monolithic implementation.
 */
package logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.BrickRotator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import model.BoardState;
import model.NextShapeInfo;
import model.Score;
import model.ViewData;

import java.awt.*;

public class GameBoard implements Board {

    private final int width;
    private final int height;

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final CollisionDetector collisionDetector;
    private final MergeEngine mergeEngine;
    private final RowClearManager rowClearManager;
    private final SpawnManager spawnManager;
    private final RotateManager rotateManager;
    private final MoveManager moveManager;


    private BoardState boardState;
    private Point currentOffset;

    private final Score score;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;

        this.boardState = new BoardState(width, height);

        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.collisionDetector = new CollisionDetector();
        this.mergeEngine = new MergeEngine();
        this.rowClearManager = new RowClearManager();
        this.spawnManager = new SpawnManager();
        this.rotateManager = new RotateManager();
        this.moveManager = new MoveManager();


        this.score = new Score();

        newGame();
    }

    @Override
    public boolean moveBrickDown() {
        return moveManager.move(boardState, brickRotator, currentOffset, 0, 1);
    }

    @Override
    public boolean moveBrickLeft() {
        return moveManager.move(boardState, brickRotator, currentOffset, -1, 0);
    }

    @Override
    public boolean moveBrickRight() {
        return moveManager.move(boardState, brickRotator, currentOffset, 1, 0);
    }

    @Override
    public boolean rotateLeftBrick() {
        return rotateManager.rotate(
                boardState,
                brickRotator,
                collisionDetector,
                currentOffset
        );
    }

    @Override
    public boolean createNewBrick() {
        currentOffset = new Point();
        return spawnManager.spawn(
                boardState,
                brickGenerator,
                brickRotator,
                collisionDetector,
                currentOffset
        );
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardState.getMatrix();
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y,
                brickGenerator.getNextBrick().getShapeMatrix().get(0)
        );
    }

    @Override
    public void mergeBrickToBackground() {
        int[][] newMatrix = mergeEngine.merge(
                boardState.getMatrix(),
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
        replaceMatrix(newMatrix);
    }

    private void replaceMatrix(int[][] newMatrix) {
        int[][] target = boardState.getMatrix();
        for (int x = 0; x < width; x++) {
            System.arraycopy(newMatrix[x], 0, target[x], 0, height);
        }
    }

    @Override
    public ClearRow clearRows() {
        ClearRow result = rowClearManager.clear(boardState.getMatrix());
        replaceMatrix(result.getNewMatrix());
        score.add(result.getScoreBonus());
        return result;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        boardState.reset();
        score.reset();
        createNewBrick();
    }
}
