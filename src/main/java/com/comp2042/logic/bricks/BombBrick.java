package com.comp2042.logic.bricks;

import java.util.List;

public class BombBrick implements Brick {

    @Override
    public List<int[][]> getShapeMatrix() {
        return List.<int[][]>of(
                new int[][] {
                        { 9 }
                }
        );
    }
}
