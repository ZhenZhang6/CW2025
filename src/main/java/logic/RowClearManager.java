package logic;

public class RowClearManager {

    public ClearRow clear(int[][] boardMatrix) {
        return MatrixOperations.checkRemoving(boardMatrix);
    }
}
