/**
 * Merges the active brick into the board matrix when it lands.
 * Produces a new matrix but does not directly update score or view.
 */
package logic;

public class MergeEngine {

    public int[][] merge(int[][] boardMatrix, int[][] shape, int x, int y) {
        return MatrixOperations.merge(boardMatrix, shape, x, y);
    }
}
