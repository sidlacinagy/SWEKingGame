package javafx.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.GameState;
import model.King;
import model.Position;
import model.SquareStatus;
import org.tinylog.Logger;


public class KingGameController {

    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private boolean isSaved=false;
    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;
    private List<Position> selectablePositions = new ArrayList<>();
    private GameState model = null;
    private String whiteName=null;
    private String blackName=null;
    private StringProperty kingColor=new ReadOnlyStringWrapper();
    private StringProperty nextMoveName=new ReadOnlyStringWrapper();

    @FXML
    private TextField textField;

    @FXML
    private GridPane gridPane;



    @FXML
    private void undo(Event event) throws IOException {
        model.undo();

        Logger.debug("Applied undo");
    }

    @FXML
    private void redo(Event event) throws IOException {
        model.redo();

        Logger.debug("Applied redo");
    }

    @FXML
    private void mainMenu(Event event) throws IOException {
        if(!isSaved) {
            ButtonType quit = new ButtonType("Quit to Main Menu", ButtonBar.ButtonData.OK_DONE);
            ButtonType play = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved progress will be lost", quit, play);
            //alert.setContentText("All progress will be lost");
            alert.setHeaderText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && quit.equals(result.get())) {
                loadMenu();
                Logger.debug("Loading main menu");
            }
        }
        else {
            loadMenu();
            Logger.debug("Loading main menu");
        }
    }

    @FXML
    private void saveGame(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/savemenu.fxml"));
        Parent root = fxmlLoader.load();
        SaveMenuController controller = fxmlLoader.getController();
        controller.setModel(model,whiteName,blackName);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Loading saving screen");
    }



    public void setNewGame(String whiteName,String blackName) {
        this.whiteName=whiteName;
        this.blackName=blackName;
        model = GameState.createNewGame();
        initialize();
    }

    public void setLoadedGame(GameState gameState, boolean isSaved,String whiteName,String blackName) {
        model = gameState;
        this.whiteName=whiteName;
        this.blackName=blackName;
        this.isSaved=isSaved;
        initialize();
    }

    private void initialize() {
        createBoard();
        createPieces();
        gridPane.getStyleClass().add("gridPane");
        kingColor.bind(Bindings.when(model.getCurrentPlayerProperty().isEqualTo(0)).then("White").otherwise("Black"));
        nextMoveName.bind(Bindings.when(model.getMoveIndexProperty().isEqualTo(0)).then("move the king").otherwise("remove a tile"));
        textField.textProperty().bind(Bindings.concat(kingColor).concat(" to ").concat(nextMoveName));
    }



    private void createBoard() {
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                var square = createSquare();
                if ((i + j) % 2 == 1) {
                    square.styleProperty().bind(Bindings.when(model.getTileProperty(i, j).isEqualTo(SquareStatus.EMPTY))
                            .then("-fx-background-color: SaddleBrown; -fx-fill: SaddleBrown;")
                            .otherwise("-fx-background-color: gray; -fx-fill: gray;"));
                } else {
                    square.styleProperty().bind(Bindings.when(model.getTileProperty(i, j).isEqualTo(SquareStatus.EMPTY))
                            .then("-fx-background-color: AntiqueWhite; -fx-fill: AntiqueWhite;")
                            .otherwise("-fx-background-color: gray; -fx-fill: gray;"));
                }
                gridPane.add(square, j, i);
            }
        }
    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void createPieces() {
        model.getBlackKing().getPositionWrapped().addListener(this::piecePositionChange);
        ImageView blackKing = null;
        try {
            blackKing = new ImageView(new Image(String.valueOf(getClass().getResource("/blackKing.png").toURI())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        getSquare(model.getBlackKing().getPosition()).getChildren().add(blackKing);

        ImageView whiteKing = null;
        try {
            whiteKing = new ImageView(new Image(String.valueOf(getClass().getResource("/whiteKing.png").toURI())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        model.getWhiteKing().getPositionWrapped().addListener(this::piecePositionChange);
        getSquare(model.getWhiteKing().getPosition()).getChildren().add(whiteKing);


    }


    private StackPane getSquare(Position position) {
        for (var child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }


    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }


    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        if (model.getMoveIndex() == 0) {
            handleKingClick(position);
        } else if (model.getMoveIndex() == 1) {
            handleTileClick(position);
        }
        processIsGoal();
    }

    private void processIsGoal() {
        int isGoal = model.isGoal();
        if (isGoal == 0 || isGoal == 1) {
            String s = "";
            if (model.isGoal() == 1) {
                try {
                    handleWin(blackName,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (model.isGoal() == 0) {
                try {
                    handleWin(whiteName,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    private void handleWin(String s,int winningPlayerIndex) throws IOException {
        int diff=setPoints(winningPlayerIndex);
        String headerText;

        if(winningPlayerIndex==0){
        headerText=whiteName+"+"+diff+"\n"+blackName+"-"+diff;
        }

        else{
            headerText=whiteName+"-"+diff+"\n"+blackName+"+"+diff;
        }

        ImageView winner = null;
        if (winningPlayerIndex==0) {
            try {
                winner = new ImageView(new Image(String.valueOf(getClass().getResource("/whiteKing.png").toURI())));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (winningPlayerIndex==1) {
            try {
                winner = new ImageView(new Image(String.valueOf(getClass().getResource("/blackKing.png").toURI())));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        Logger.debug(s+" wins");
        ButtonType playing = new ButtonType("Keep Playing", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Quit to main menu", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, s + " wins", playing, quit);
        alert.setContentText("Quit to main menu?");
        alert.setHeaderText(headerText);
        alert.setTitle(s + " wins");
        alert.setGraphic(winner);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && quit.equals(result.get())) {
            loadMenu();
        }
    }

    private int setPoints(int winningPlayerIndex){
        String filePath=System.getProperty("user.home")+"/.KingGame/points.json";
        TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<>() {
        };
        HashMap map=null;
        try {
            map=OBJECT_MAPPER.readValue(new File(filePath), typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.putIfAbsent(whiteName, 1000);
        map.putIfAbsent(blackName, 1000);
        int whitePoint=(int)map.get(whiteName);
        int blackPoint=(int)map.get(blackName);
        int sum=whitePoint+blackPoint;
        float blackRatio=(float)blackPoint/sum;
        float whiteRatio=(float)whitePoint/sum;
        int diff=0;
        if(winningPlayerIndex==0){
            diff= (int) ((50)*blackRatio);
            map.replace(whiteName,whitePoint+diff);
            map.replace(blackName,blackPoint-diff);
        }

        if(winningPlayerIndex==1){
            diff= (int) ((50)*whiteRatio);
            map.replace(whiteName,whitePoint-diff);
            map.replace(blackName,blackPoint+diff);
        }

        ObjectWriter writer = OBJECT_MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File(filePath), map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diff;



    }

    private void loadMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void handleTileClick(Position position) {
        if(model.applyOperator(position)==1){
            isSaved=false;
        }
        Logger.debug("Applying tile operator on "+position);
    }

    private void handleKingClick(Position position) {
        switch (selectionPhase) {
            case SELECT_FROM -> {
                King king = null;
                if (model.getCurrentPlayer() == 0) {
                    king = model.getWhiteKing();
                } else if (model.getCurrentPlayer() == 1) {
                    king = model.getBlackKing();
                }
                if (position.equals(king.getPosition())) {
                    selectablePositions = model.getAppliablePositions(king);
                    setSelectablePositions(selectablePositions);
                    selectionPhase = selectionPhase.alter();
                } else {
                    hideSelectablePositions();
                }
            }
            case SELECT_TO -> {
                hideSelectablePositions();
                if(model.applyOperator(position)==1){
                    isSaved=false;
                }
                Logger.debug("Applying king operator on "+position);
                selectionPhase = selectionPhase.alter();
            }
        }
    }

    private void setSelectablePositions(List<Position> positions) {
        for (Position move : positions) {
            var piece = new Circle(10);
            piece.setFill(Color.valueOf("black"));
            getSquare(move).getChildren().add(piece);
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getChildren().clear();

        }
        selectablePositions.clear();
    }


}
