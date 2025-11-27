/**
 * Handles horizontal and vertical movement of the active brick.
 * Updates the brick offset only when movement is allowed.
 * Uses CollisionDetector to verify movement safety.
 */
package logic;

import com.comp2042.logic.bricks.BrickRotator;
import model.BoardState;

import java.awt.*;

public class MoveManager {

    private final CollisionDetector detector = new CollisionDetector();

    public boolean move(BoardState state,
                        BrickRotator rotator,
                        Point offset,
                        int dx,
                        int dy) {

        if (!detector.canMove(state, rotator, offset, dx, dy)) {
            return false;
        }

        offset.translate(dx, dy);
        return true;
    }
}
