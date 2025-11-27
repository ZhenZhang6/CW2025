/**
 * Coordinates the game flow between the view (UI) and the game logic.
 * Uses InputManager to interpret user input and delegates actions
 * to GameBoard.
 */
package controller;

import logic.Board;
import logic.ClearRow;
import logic.GameBoard;
import model.DownData;
import model.ViewData;
import ui.GuiController;

public class GameController implements InputEventListener {

    private Board board = new GameBoard(25, 10);

    private final GuiController viewGuiController;

    private final InputManager inputManager = new InputManager();

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        return handleInput(event);
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        return handleInput(event);
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        return handleInput(event);
    }

    private ViewData handleInput(MoveEvent event) {

        InputManager.Action action = inputManager.mapEvent(event);

        switch (action) {
            case MOVE_LEFT:
                board.moveBrickLeft();
                break;
            case MOVE_RIGHT:
                board.moveBrickRight();
                break;
            case ROTATE:
                board.rotateLeftBrick();
                break;
            default:
                break;
        }

        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}

