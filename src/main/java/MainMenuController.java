import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.GameState;
import model.King;
import model.Position;
import model.SquareStatus;

public class MainMenuController {

    @FXML
    private void quitGame(Event event) throws IOException {
        Platform.exit();
    }
    @FXML
    private void loadGame(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loadmenu.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void startGame(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
        Parent root = fxmlLoader.load();
        KingGameController controller = fxmlLoader.getController();
        controller.setNewGame();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - 1200) / 2);
        stage.setY((screenBounds.getHeight() - 800) / 2);
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void initialize() throws IOException {
        String dir=System.getProperty("user.home");
        dir=dir+"/.KingGame";
        if (!Files.exists(Paths.get(dir))) {
            Files.createDirectory(Paths.get(dir));
        }
        for(int i=0;i<3;i++) {
            String target=dir;
            target=target+"/kingGame"+i+".json";
            if (!Files.exists(Paths.get(target))) {
                copyJson(target);

            }
        }

    }

    private void copyJson(String target){
        String mainPath=null;
        try {
            URI uri = getClass().getResource("/kingGame.json").toURI();
            mainPath = Paths.get(uri).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            Files.copy(Paths.get(mainPath), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
