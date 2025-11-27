/**
 * Translates raw input events into high-level gameplay actions.
 * Keeps GameController independent from low-level event details.
 */
package controller;

public class InputManager {

    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_DOWN,
        ROTATE,
        NONE
    }

    public Action mapEvent(MoveEvent event) {
        switch (event.getEventType()) {
            case LEFT:
                return Action.MOVE_LEFT;
            case RIGHT:
                return Action.MOVE_RIGHT;
            case DOWN:
                return Action.MOVE_DOWN;
            case ROTATE:
                return Action.ROTATE;
            default:
                return Action.NONE;
        }
    }
}
