package javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class NameGetterController {

    @FXML
    private TextField textFieldWhite;

    @FXML
    private TextField textFieldBlack;

    @FXML
    private Button button;

    @FXML
    private void initialize(){
        textFieldWhite.promptTextProperty().set("White player's name:");
        textFieldBlack.promptTextProperty().set("Black player's name:");
    }

    @FXML
    private void startGame(){
        if(textFieldWhite.getText().isEmpty() ||
                textFieldBlack.getText().isEmpty() ||
                textFieldWhite.getText().equals(textFieldBlack.getText())){
                handleInvalidNames();
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            KingGameController controller = fxmlLoader.getController();
            controller.setNewGame(textFieldWhite.getText(),textFieldBlack.getText());
            Stage stage = (Stage) textFieldBlack.getScene().getWindow();
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.show();
            Logger.debug("New Game loaded");
        }
    }

    private void handleInvalidNames(){
        textFieldBlack.setText("");
        textFieldWhite.setText("");
        initialize();
        }
}
