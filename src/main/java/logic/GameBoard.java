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

    // ★ 新增：管理当前 + 下一块
    private Brick currentBrick;
    private Brick nextBrick;

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

        // ★ 初始化 next brick
        this.nextBrick = brickGenerator.getBrick();

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

    // ========================
    // ★ Spawn 使用 nextBrick 系统
    // ========================
    @Override
    public boolean createNewBrick() {

        // 当前砖等于 next
        currentBrick = nextBrick;

        // 再生成新的下一块
        nextBrick = brickGenerator.getBrick();

        // 设置到 rotator
        brickRotator.setBrick(currentBrick);

        // 初始 offset（你原来 spawnManager 默认是 (4,10)）
        currentOffset = new Point(4, 10);

        // 是否立即冲突（game over）
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
                nextBrick.getShapeMatrix().get(0) // ★ 返回下一块
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
        score.addLines(result.getLinesRemoved());
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

        // 重置 current，但 next 已经提前生成
        currentBrick = null;

        // 创建第一块（使用 nextBrick）
        createNewBrick();
    }

    // =============================
    // ★ GUI: 用来显示下一个方块
    // =============================
    @Override
    public NextShapeInfo getNextShapeInfo() {
        return new NextShapeInfo(
                nextBrick.getShapeMatrix().get(0),
                0   // 位置你不需要，用不到
        );
    }
}
