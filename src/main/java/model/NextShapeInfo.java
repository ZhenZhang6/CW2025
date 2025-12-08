/**
 * Holds information about the next Tetris shape to be spawned.
 * Stores the brick's matrix data and its initial spawn position.
 * Refactored into the model package as part of the improved game architecture.
 */

package model;

import logic.MatrixOperations;

public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}
