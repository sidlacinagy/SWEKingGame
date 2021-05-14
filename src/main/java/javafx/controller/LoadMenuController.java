package javafx.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.GameState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LoadMenuController {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @FXML
    private void mainMenu(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private HBox hBox0;

    @FXML
    private HBox hBox1;

    @FXML
    private HBox hBox2;
    @FXML
    private void initialize() {
        Map<Integer,String> values=getOccupiedIndexes();
        setButtons(values);

    }

    private Map<Integer,String> getOccupiedIndexes() {
        Map<Integer, String> values = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            JsonNode jsonNode = getJsonNode(i);

            if (!jsonNode.get("isEmpty").asBoolean()) {
                values.put(i, jsonNode.get("time").asText());
            }
        }
        return values;
    }

    private JsonNode getJsonNode(int index){
        JsonNode jsonNode=null;
        String content=getJsonAsString(index);
        try {
            jsonNode = OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonNode;
    }

    private String getJsonAsString(int index){
        String filePath=System.getProperty("user.home")+"/.KingGame/kingGame"+index+".json";
        String content = null;
        try {
            content = new String (Files.readAllBytes( Paths.get(filePath) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
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
        Button load= (Button) hBox.getChildren().get(1);
        Button delete= (Button) hBox.getChildren().get(2);
        textField.setText("Empty");
        load.setDisable(true);
        delete.setDisable(true);
    }

    private void setHBoxValues(HBox hBox, String time){
        TextField textField= (TextField) hBox.getChildren().get(0);
        Button load= (Button) hBox.getChildren().get(1);
        Button delete= (Button) hBox.getChildren().get(2);
        textField.setText(time);
        load.setDisable(false);
        delete.setDisable(false);
    }


    private void loadGame(int index){
        JsonNode jsonNode=getJsonNode(index);
        String jsonGameState=jsonNode.get("GameState").asText();
        ObjectMapper objectMapper = new ObjectMapper();
        GameState gameState=null;
        try {
            gameState=objectMapper.readValue(jsonGameState, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KingGameController controller = fxmlLoader.getController();
        controller.setLoadedGame(gameState);
        Stage stage = (Stage) hBox0.getScene().getWindow();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - 1200) / 2);
        stage.setY((screenBounds.getHeight() - 800) / 2);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void deleteGame(int index){
        JsonNode jsonNode=getJsonNode(index);
        ((ObjectNode)jsonNode).put("isEmpty",true);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File(System.getProperty("user.home")+"/.KingGame/kingGame"+index+".json"), jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize();
    }

    @FXML
    private void load0(Event event) throws IOException{
        loadGame(0);
    }

    @FXML
    private void load1(Event event) throws IOException{
        loadGame(1);
    }

    @FXML
    private void load2(Event event) throws IOException{
        loadGame(2);
    }

    @FXML
    private void delete0(Event event) throws IOException{
        deleteGame(0);
    }

    @FXML
    private void delete1(Event event) throws IOException{
        deleteGame(1);
    }

    @FXML
    private void delete2(Event event) throws IOException{
        deleteGame(2);
    }

}
