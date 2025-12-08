package model;

import logic.ClearRow;
/**
 * Data container returned after a downward movement action.
 * Holds both the updated view data of the active brick and
 * any row-clearing information resulting from the move.
 */

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}