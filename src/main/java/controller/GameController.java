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

    // ★ 新增：总消行数
    private int totalLines = 0;

    public GameController(GuiController c) {
        viewGuiController = c;

        board.createNewBrick();
        viewGuiController.setEventListener(this);

        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        // 下一块
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


            // 若消行，累计
            if (clearRow.getLinesRemoved() > 0) {
                totalLines += clearRow.getLinesRemoved();

                // 通知 GUI 更新掉落速度
                viewGuiController.updateFallSpeed(totalLines);
            }

            // soft drop +1
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }

            // 刷新背景
            viewGuiController.refreshGameBackground(board.getBoardMatrix());

            // 生成下一块
            boolean spawnConflict = board.createNewBrick();

            // 刷新当前方块
            viewGuiController.refreshBrick(board.getViewData());

            // 刷新 next piece
            viewGuiController.updateNextPiece(board.getNextShapeInfo());

            if (spawnConflict) {
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

        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        totalLines = 0; // 重置

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.updateNextPiece(board.getNextShapeInfo());
        viewGuiController.updateFallSpeed(0); // 恢复初始速度
    }
}
