package javafx.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import helper.NamePoints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointsMenuController {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> name;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> point;



    @FXML
    private void mainMenu(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize(){
        point.setCellValueFactory(new PropertyValueFactory<>("point"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        String filePath=System.getProperty("user.home")+"/.KingGame/points.json";
        TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<>() {};
        HashMap map=null;
        try {
            map=OBJECT_MAPPER.readValue(new File(filePath), typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<NamePoints> namePoints= new ArrayList<>();
        List<String> names=new ArrayList<String>(map.keySet());
        List<Integer> points=new ArrayList<Integer>(map.values());
        for(int i=0;i<names.size();i++){
        namePoints.add(new NamePoints(names.get(i),points.get(i)));
        }

        ObservableList<NamePoints> observableList = FXCollections.observableArrayList();
        observableList.addAll(namePoints);
        tableView.setItems(observableList);

    }
}
