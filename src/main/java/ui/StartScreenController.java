package ui;

import controller.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartScreenController {

    @FXML
    private Button startBtn;

    @FXML
    public void initialize() {
        startBtn.setOnAction(e -> startGame());
    }

    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("gameLayout.fxml")
            );

            Parent gameRoot = loader.load();
            GameController gameController = new GameController(loader.getController());

            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.setScene(new Scene(gameRoot, 700, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
