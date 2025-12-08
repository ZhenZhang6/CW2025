/**
 * Immutable result object representing the outcome of row-clearing.
 *
 * <p>This object packages three important results produced by the
 * row-clearing subsystem:</p>
 *
 * <ul>
 *     <li><b>linesRemoved</b> — how many full rows were cleared.</li>
 *     <li><b>newMatrix</b> — the updated board matrix after removal.</li>
 *     <li><b>scoreBonus</b> — the score gained from this clear,
 *         which may include special rules such as Bomb blocks that
 *         double the value of the cleared row.</li>
 * </ul>
 *
 * <p>The class is immutable and safe to pass between logic layers,
 * ensuring no accidental modification of board state.</p>
 */
package logic;

public final class ClearRow {

    private final int linesRemoved;

    private final int[][] newMatrix;

    private final int scoreBonus;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }
}
