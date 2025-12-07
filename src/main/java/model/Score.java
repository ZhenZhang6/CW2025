package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class Score {


    private final IntegerProperty score = new SimpleIntegerProperty(0);

    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);

    private final IntegerProperty highScore = new SimpleIntegerProperty(0);

    private static final Path HIGH_SCORE_PATH =
            Path.of(System.getProperty("user.home"), "tetris_highscore.txt");


    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void add(int value) {
        score.set(score.get() + value);
    }


    public void addLines(int lines) {
        totalLines.set(totalLines.get() + lines);
    }

    public int getTotalLines() {
        return totalLines.get();
    }

    public IntegerProperty totalLinesProperty() {
        return totalLines;
    }


    public int getHighScore() {
        return highScore.get();
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public void loadHighScore() {
        try {
            if (Files.exists(HIGH_SCORE_PATH)) {
                String text = Files.readString(HIGH_SCORE_PATH).trim();
                highScore.set(Integer.parseInt(text));
            }
        } catch (Exception ignored) {
        }
    }

    public void saveHighScore() {
        try {
            Files.writeString(
                    HIGH_SCORE_PATH,
                    Integer.toString(highScore.get()),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException ignored) {
        }
    }

    public void updateHighScore(int newScore) {
        if (newScore > highScore.get()) {
            highScore.set(newScore);
            saveHighScore();
        }
    }


    public void reset() {
        score.set(0);
        totalLines.set(0);
    }
}
