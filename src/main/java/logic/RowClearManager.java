/**
 * Detects and removes full rows from the board.
 * Returns a ClearRow object containing the updated matrix
 * and the number of cleared lines.
 */
package logic;

public class RowClearManager {

    public ClearRow clear(int[][] boardMatrix) {

        ClearRow result = MatrixOperations.checkRemoving(boardMatrix);

        int removed = result.getLinesRemoved();
        int[][] updatedMatrix = result.getNewMatrix();

        int bonus = removed * 50;

        return new ClearRow(removed, updatedMatrix, bonus);
    }
}
