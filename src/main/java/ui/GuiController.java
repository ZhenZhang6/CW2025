/**
 * Handles all JavaFX UI updates, including board rendering,
 * brick drawing, score display, notifications, next-piece preview,
 * pause/resume toggling, and fall-speed adjustments.
 * Acts as the visual layer of the MVC structure.
 */

package ui;

import model.DownData;
import model.ViewData;
import model.NextShapeInfo;
import controller.EventSource;
import controller.EventType;
import controller.InputEventListener;
import controller.MoveEvent;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 28;
    private static final int HIDDEN_ROWS = 2;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;

    @FXML private Button pauseButton;
    @FXML private Button restartButton;

    @FXML private Label scoreLabel;
    @FXML private Label linesLabel;

    @FXML private Label highScoreLabel;
    private int highestScore = 0;

    @FXML private GridPane nextPreview;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    public void updateLines(int totalLines) {
        linesLabel.setText("Lines: " + totalLines);
    }

    public void updateHighestScore(int score) {
        if (score > highestScore) {
            highestScore = score;
            highScoreLabel.setText("Highest: " + highestScore);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setHgap(0);
        gamePanel.setVgap(0);
        brickPanel.setHgap(0);
        brickPanel.setVgap(0);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (!isPause.get() && !isGameOver.get()) {

                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(
                                new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }

                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(
                                new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }

                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(
                                new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }

                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }

                if (keyEvent.getCode() == KeyCode.N) {
                    restartGame(null);
                }
            }
        });

        gameOverPanel.setVisible(false);

        Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);

                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - HIDDEN_ROWS);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));

                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanel.getLayoutY() + (brick.getyPosition() - HIDDEN_ROWS) * BRICK_SIZE);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));

        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    public void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(gamePanel.getLayoutY() + (brick.getyPosition() - HIDDEN_ROWS) * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = HIDDEN_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void moveDown(MoveEvent event) {

        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);

            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel =
                        new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            refreshBrick(downData.getViewData());
        }

        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void togglePause(ActionEvent event) {

        if (isGameOver.get()) return;

        if (isPause.get()) {
            timeLine.play();
            isPause.set(false);
            pauseButton.setText("Pause");

        } else {
            timeLine.pause();
            isPause.set(true);
            pauseButton.setText("Resume");
        }
    }

    public void restartGame(ActionEvent event) {

        timeLine.stop();
        gameOverPanel.setVisible(false);

        isPause.set(false);
        isGameOver.set(false);

        eventListener.createNewGame();

        timeLine.play();
        pauseButton.setText("Pause");

        gamePanel.requestFocus();
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    public void updateFallSpeed(int totalLines) {

        int base = 400;
        int reduce = (totalLines / 5) * 20;
        int newSpeed = Math.max(180, base - reduce);

        if (timeLine != null) {
            timeLine.stop();
        }

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(newSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));

        timeLine.setCycleCount(Timeline.INDEFINITE);

        if (!isPause.get() && !isGameOver.get()) {
            timeLine.play();
        }
    }

    public void updateNextPiece(NextShapeInfo nextInfo) {
        if (nextPreview == null) return;

        nextPreview.getChildren().clear();
        int[][] shape = nextInfo.getShape();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {

                Rectangle r = new Rectangle(20, 20);
                r.setFill(getFillColor(shape[i][j]));
                r.setArcHeight(6);
                r.setArcWidth(6);

                nextPreview.add(r, j, i);
            }
        }
    }
}
