/**
 * Creates and positions a new falling brick.
 * Also performs spawn collision checks to detect game-over conditions.
 */
package logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.BrickRotator;
import model.BoardState;

import java.awt.*;

public class SpawnManager {

    public boolean spawn(BoardState state,
                         BrickGenerator generator,
                         BrickRotator rotator,
                         CollisionDetector detector,
                         Point offsetHolder) {

        Brick brick = generator.getBrick();
        rotator.setBrick(brick);

        offsetHolder.setLocation(4, 10);

        return detector.isSpawnConflict(state, rotator, offsetHolder);
    }
}
