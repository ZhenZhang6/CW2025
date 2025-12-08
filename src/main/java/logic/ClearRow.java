/**
 * An immutable object that stores the result of a row-clear operation.
 *
 * <p>Whenever the game clears one or more full rows, the details of that
 * action are bundled into this class:</p>
 *
 * <ul>
 *     <li><b>linesRemoved</b> – how many rows were cleared.</li>
 *     <li><b>newMatrix</b> – the updated board after the rows have been removed.</li>
 *     <li><b>scoreBonus</b> – the points earned from this clear.
 *         If a special block (such as a Bomb block) is involved, this
 *         value may be increased.</li>
 * </ul>
 *
 * <p>The class is kept immutable so that the result can be passed safely
 * between different parts of the game logic without being changed by mistake.</p>
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
