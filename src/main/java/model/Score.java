package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    // 当前得分
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    // ★ 新增：总消除的行数（用于加速难度、显示 UI）
    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);

    // ========== 得分 ==========

    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void add(int value) {
        score.set(score.get() + value);
    }

    // ========== 行数统计（新增） ==========

    /** ★ 调用它来累计被清除的行数 */
    public void addLines(int lines) {
        totalLines.set(totalLines.get() + lines);
    }

    /** ★ GUI 调用它来显示总共消了多少行 */
    public int getTotalLines() {
        return totalLines.get();
    }

    public IntegerProperty totalLinesProperty() {
        return totalLines;
    }

    // ========== 重置 ==========

    public void reset() {
        score.set(0);
        totalLines.set(0);
    }
}
