package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();

        // Normal bricks
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        brickList.add(new BombBrick());

        nextBricks.add(generateRandomBrick());
        nextBricks.add(generateRandomBrick());
    }

    private Brick generateRandomBrick() {
        int rand = ThreadLocalRandom.current().nextInt(10); // 0~9

        if (rand == 0) {
            return new BombBrick();
        }

        int index = ThreadLocalRandom.current().nextInt(7);
        return brickList.get(index);
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(generateRandomBrick());
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
