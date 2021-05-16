package javafx.controller;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.GameState;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SaveMenuController {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @FXML
    private HBox hBox0;

    @FXML
    private HBox hBox1;

    @FXML
    private HBox hBox2;

    private GameState model=null;

    private boolean isSaved=false;

    private String whiteName=null;

    private String blackName=null;

    private GameState[] gameStates=new GameState[3];

    public void setModel(GameState gameState,String whiteName,String blackName){
        this.whiteName=whiteName;
        this.blackName=blackName;
        model=gameState;
        initialize();
    }

    private void initialize() {
        Map<Integer,String> values=getOccupiedIndexes();
        setButtons(values);

    }

    private Map<Integer,String> getOccupiedIndexes() {
        Map<Integer, String> values = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            JsonNode jsonNode = JsonReader.getSaveGameJsonNode(i);

            if (!jsonNode.get("isEmpty").asBoolean()) {
                values.put(i, jsonNode.get("time").asText());
            }
        }
        return values;
    }





    private void setButtons(Map<Integer,String> values){
        if(values.containsKey(0)){
            String time=values.get(0);
            setHBoxValues(hBox0,time);
        }
        else{
            setOriginalHBox(hBox0);
        }

        if(values.containsKey(1)){
            String time=values.get(1);
            setHBoxValues(hBox1,time);
        }
        else {
            setOriginalHBox(hBox1);
        }

        if(values.containsKey(2)){
            String time=values.get(2);
            setHBoxValues(hBox2,time);
        }
        else {
            setOriginalHBox(hBox2);
        }
    }

    private void setOriginalHBox(HBox hBox){
        TextField textField= (TextField) hBox.getChildren().get(0);
        Button save= (Button) hBox.getChildren().get(1);
        Button delete= (Button) hBox.getChildren().get(2);
        textField.setText("Empty");
        save.setDisable(false);
        delete.setDisable(true);
    }

    private void setHBoxValues(HBox hBox, String time){
        TextField textField= (TextField) hBox.getChildren().get(0);
        Button save= (Button) hBox.getChildren().get(1);
        Button delete= (Button) hBox.getChildren().get(2);
        textField.setText(time);
        save.setDisable(true);
        delete.setDisable(false);
    }


    @FXML
    private void gameReturn(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
        Parent root = fxmlLoader.load();
        KingGameController controller = fxmlLoader.getController();
        controller.setLoadedGame(model,isSaved,whiteName,blackName);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - 1200) / 2);
        stage.setY((screenBounds.getHeight() - 800) / 2);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saveGame(int index){
        JsonNode jsonNode= JsonReader.getSaveGameJsonNode(index);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ((ObjectNode)jsonNode).put("time", dtf.format(now));
        ((ObjectNode)jsonNode).put("isEmpty",false);
        ((ObjectNode)jsonNode).put("whiteName",whiteName);
        ((ObjectNode)jsonNode).put("blackName",blackName);
        String jsonStr=null;
        try {
            jsonStr = OBJECT_MAPPER.writeValueAsString(model);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ((ObjectNode)jsonNode).put("GameState",jsonStr);
        ObjectWriter writer = OBJECT_MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File(System.getProperty("user.home")+"/.KingGame/kingGame"+index+".json"), jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize();
        isSaved=true;
        Logger.debug("Saved the "+index+". game");
    }

    private void deleteGame(int index){
        JsonNode jsonNode= JsonReader.getSaveGameJsonNode(index);
        ((ObjectNode)jsonNode).put("isEmpty",true);
        ObjectWriter writer = OBJECT_MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File(System.getProperty("user.home")+"/.KingGame/kingGame"+index+".json"), jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize();
        Logger.debug("Deleted the "+index+". game");
    }


    @FXML
    private void saveGame0(Event event) throws IOException{
        saveGame(0);
    }

    @FXML
    private void saveGame1(Event event) throws IOException{
        saveGame(1);
    }

    @FXML
    private void saveGame2(Event event) throws IOException{
        saveGame(2);
    }

    @FXML
    private void deleteGame0(Event event) throws IOException{
        deleteGame(0);
    }

    @FXML
    private void deleteGame1(Event event) throws IOException{
        deleteGame(1);
    }

    @FXML
    private void deleteGame2(Event event) throws IOException{
        deleteGame(2);
    }



}
