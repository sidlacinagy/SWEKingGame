package javafx.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReader {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String getSaveGameJsonAsString(int index){
        String filePath=System.getProperty("user.home")+"/.KingGame/kingGame"+index+".json";
        String content = null;
        try {
            content = new String (Files.readAllBytes( Paths.get(filePath) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static JsonNode getSaveGameJsonNode(int index){
        JsonNode jsonNode=null;
        String content= JsonReader.getSaveGameJsonAsString(index);
        try {
            jsonNode = OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonNode;
    }

    public static String getPointsJsonAsString(){
        String filePath=System.getProperty("user.home")+"/.KingGame/points.json";
        String content = null;
        try {
            content = new String (Files.readAllBytes( Paths.get(filePath) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static JsonNode getPointsJsonNode(){
        JsonNode jsonNode=null;
        String content= JsonReader.getPointsJsonAsString();
        try {
            jsonNode = OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonNode;
    }
}
