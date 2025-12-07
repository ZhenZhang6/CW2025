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

    private int totalLines = 0;

    public GameController(GuiController c) {
        viewGuiController = c;

        board.getScore().loadHighScore();
        viewGuiController.updateHighestScore(board.getScore().getHighScore());

        board.createNewBrick();
        viewGuiController.setEventListener(this);

        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        viewGuiController.updateNextPiece(board.getNextShapeInfo());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {

        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {

            board.mergeBrickToBackground();

            clearRow = board.clearRows();
            viewGuiController.updateLines(board.getScore().getTotalLines());

            if (clearRow.getLinesRemoved() > 0) {
                totalLines += clearRow.getLinesRemoved();

                viewGuiController.updateFallSpeed(totalLines);
            }

            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }

            board.getScore().updateHighScore(board.getScore().getScore());
            viewGuiController.updateHighestScore(board.getScore().getHighScore());

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

            boolean spawnConflict = board.createNewBrick();

            viewGuiController.refreshBrick(board.getViewData());

            viewGuiController.updateNextPiece(board.getNextShapeInfo());

            if (spawnConflict) {
                board.getScore().updateHighScore(board.getScore().getScore());
                viewGuiController.updateHighestScore(board.getScore().getHighScore());
                viewGuiController.gameOver();
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
            case MOVE_LEFT -> board.moveBrickLeft();
            case MOVE_RIGHT -> board.moveBrickRight();
            case ROTATE -> board.rotateLeftBrick();
        }

        board.getScore().updateHighScore(board.getScore().getScore());
        viewGuiController.updateHighestScore(board.getScore().getHighScore());

        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        totalLines = 0;

        viewGuiController.updateHighestScore(board.getScore().getHighScore());

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.updateNextPiece(board.getNextShapeInfo());
        viewGuiController.updateFallSpeed(0);
    }
}
