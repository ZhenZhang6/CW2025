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

        if (removed == 0) {
            return new ClearRow(0, updatedMatrix, 0);
        }

        int baseScore = removed * 50;

        int bombCount = countBombBlocks(boardMatrix, result);

        int multiplier = (int) Math.pow(2, bombCount);

        int totalScore = baseScore * multiplier;

        return new ClearRow(removed, updatedMatrix, totalScore);
    }

    private int countBombBlocks(int[][] boardMatrix, ClearRow result) {
        int bombCount = 0;

        int[][] newMatrix = result.getNewMatrix();

        for (int y = 0; y < boardMatrix.length; y++) {
            boolean rowWasFull = true;
            for (int x = 0; x < boardMatrix[y].length; x++) {
                if (boardMatrix[y][x] == 0) {
                    rowWasFull = false;
                    break;
                }
            }

            if (rowWasFull) {
                for (int x = 0; x < boardMatrix[y].length; x++) {
                    if (boardMatrix[y][x] == 9) bombCount++;
                }
            }
        }
        return bombCount;
    }
}
