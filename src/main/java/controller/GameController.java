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

        // ============================
        // 方块不能再下落 → 落地逻辑
        // ============================
        if (!canMove) {

            // 合并到背景
            board.mergeBrickToBackground();

            // 消行（只在 Board 内部更新分数，不在这里重复加）
            clearRow = board.clearRows();

            // soft drop 只在“按下键”导致落地时 +1 分
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }

            // 刷新背景
            viewGuiController.refreshGameBackground(board.getBoardMatrix());

            // 尝试生成下一块
            boolean spawnConflict = board.createNewBrick();

            // 刷新新砖显示
            viewGuiController.refreshBrick(board.getViewData());

            if (spawnConflict) {
                viewGuiController.gameOver();
            }

        } else {
            // 正常下落，不加分
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
