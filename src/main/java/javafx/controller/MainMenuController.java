package javafx.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.tinylog.Logger;

public class MainMenuController {

    @FXML
    private void quitGame(Event event) throws IOException {
        Platform.exit();
        Logger.debug("Exiting");
    }
    @FXML
    private void loadGame(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loadmenu.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Loading screen loaded");
    }
    @FXML
    private void startGame(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/nameGetter.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Getting names...");

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
                copyJson("/kingGame.json",target);

            }
        }
        String pointsLocation=dir+"/points.json";
        if (!Files.exists(Paths.get(pointsLocation))){
            Files.createFile(Paths.get(pointsLocation));
            FileWriter myWriter = new FileWriter(pointsLocation);
            myWriter.write("{}");
            myWriter.close();
        }

    }

    private void copyJson(String source, String target){
        String mainPath=null;
        try {
            URI uri = getClass().getResource(source).toURI();
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
