package model;

public class BoardState {

    private final int width;
    private final int height;
    private int[][] matrix;

    public BoardState(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new int[width][height];
    }

    public int get(int x, int y) {
        return matrix[x][y];
    }

    public void set(int x, int y, int value) {
        matrix[x][y] = value;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void reset() {
        matrix = new int[width][height];
    }
}
