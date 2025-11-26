package logic;

import com.comp2042.logic.bricks.BrickRotator;
import model.BoardState;
import model.NextShapeInfo;

import java.awt.*;

public class RotateManager {

    public boolean rotate(BoardState state,
                          BrickRotator rotator,
                          CollisionDetector detector,
                          Point offset) {

        NextShapeInfo next = rotator.getNextShape();

        if (!detector.canRotate(state, rotator, offset, next.getShape())) {
            return false;
        }

        rotator.setCurrentShape(next.getPosition());
        return true;
    }
}
