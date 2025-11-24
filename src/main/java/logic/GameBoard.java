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
        this.score = new Score();

        newGame();
    }

    @Override
    public boolean moveBrickDown() {
        if (!collisionDetector.canMove(boardState, brickRotator, currentOffset, 0, 1)) {
            return false;
        }
        currentOffset.translate(0, 1);
        return true;
    }

    @Override
    public boolean moveBrickLeft() {
        if (!collisionDetector.canMove(boardState, brickRotator, currentOffset, -1, 0)) {
            return false;
        }
        currentOffset.translate(-1, 0);
        return true;
    }

    @Override
    public boolean moveBrickRight() {
        if (!collisionDetector.canMove(boardState, brickRotator, currentOffset, 1, 0)) {
            return false;
        }
        currentOffset.translate(1, 0);
        return true;
    }

    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo next = brickRotator.getNextShape();

        if (!collisionDetector.canRotate(boardState, brickRotator, currentOffset, next.getShape())) {
            return false;
        }

        brickRotator.setCurrentShape(next.getPosition());
        return true;
    }

    @Override
    public boolean createNewBrick() {
        Brick brick = brickGenerator.getBrick();
        brickRotator.setBrick(brick);

        currentOffset = new Point(4, 10);

        return collisionDetector.isSpawnConflict(boardState, brickRotator, currentOffset);
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
        int[][] newMatrix = MatrixOperations.merge(
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
        ClearRow clearRow = MatrixOperations.checkRemoving(boardState.getMatrix());
        replaceMatrix(clearRow.getNewMatrix());
        return clearRow;
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
