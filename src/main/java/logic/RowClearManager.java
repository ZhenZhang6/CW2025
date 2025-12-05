/**
 * Detects and removes full rows from the board.
 * Returns a ClearRow object containing the updated matrix
 * and the number of cleared lines.
 */
package logic;

public class RowClearManager {

    public ClearRow clear(int[][] boardMatrix) {

        // 先让 MatrixOperations 找出消除结果
        ClearRow result = MatrixOperations.checkRemoving(boardMatrix);

        int removed = result.getLinesRemoved();
        int[][] updatedMatrix = result.getNewMatrix();

        // ★ 计算分数（每行 50 分）
        int bonus = removed * 50;

        // ★ 返回一个新的 ClearRow，带分数
        return new ClearRow(removed, updatedMatrix, bonus);
    }
}
