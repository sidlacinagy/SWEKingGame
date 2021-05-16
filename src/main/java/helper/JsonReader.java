package helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class, that contains Json deserialize helper methods.
 */
public class JsonReader {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    /**
     * @param index the index of the savegame.
     * @return Returns the Json as a String.
     */
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

    /**
     * @param index the index of the savegame.
     * @return Returns a {@code JsonNode} from the given savegame.
     */
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


}
