/**
 * Performs collision checks for movement, rotation, and spawning.
 * Ensures that actions are valid before the board state is modified.
 */
package logic;

import com.comp2042.logic.bricks.BrickRotator;
import model.BoardState;

import java.awt.*;

public class CollisionDetector {

    public boolean canMove(BoardState board, BrickRotator rotator, Point offset, int dx, int dy) {
        int[][] matrix = board.getMatrix();
        int[][] shape = rotator.getCurrentShape();

        int newX = offset.x + dx;
        int newY = offset.y + dy;

        return !MatrixOperations.intersect(matrix, shape, newX, newY);
    }

    public boolean canRotate(BoardState board, BrickRotator rotator, Point offset, int[][] nextShape) {
        int[][] matrix = board.getMatrix();
        return !MatrixOperations.intersect(matrix, nextShape, offset.x, offset.y);
    }

    public boolean isSpawnConflict(BoardState board, BrickRotator rotator, Point offset) {
        return MatrixOperations.intersect(
                board.getMatrix(),
                rotator.getCurrentShape(),
                offset.x,
                offset.y
        );
    }
}
