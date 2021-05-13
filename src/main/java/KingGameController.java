import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

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
import javafx.scene.control.*;
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

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;
    private List<Position> selectablePositions = new ArrayList<>();
    GameState model = null;

    public void setGameModel(GameState model) {
        this.model = model;
    }

    @FXML
    private GridPane gridPane;

    private Position selected;

    @FXML
    private void undo(Event event) throws IOException {
        model.undo();
    }

    @FXML
    private void redo(Event event) throws IOException {
        model.redo();
    }

    @FXML
    private TextField textField;

    public void setNewGame() {
        model = GameState.createNewGame();
        initialize();
    }

    private void initialize() {
        createBoard();
        createPieces();
        showSelectablePositions();
        setSelectablePositions();
        textField.setText("White to move king");
    }


    private void showSelectablePositions() {
    }

    private void setSelectablePositions() {
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
        Font font = new Font(50);
        Label blackKing = new Label("\u265A");
        blackKing.setFont(font);
        getSquare(model.getBlackKing().getPosition()).getChildren().add(blackKing);

        model.getWhiteKing().getPositionWrapped().addListener(this::piecePositionChange);
        Label whiteKing = new Label("\u2654");
        whiteKing.setFont(font);
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
        String kingText = "White";
        String moveText = "move king";
        if (model.getCurrentPlayer() == 1) {
            kingText = "Black";
        }
        if (model.getMoveIndex() == 1) {
            moveText = "remove tile";
        }
        textField.setText(kingText + " to " + moveText);
        int isGoal = model.isGoal();
        if (isGoal == 0 || isGoal == 1) {
            String s = "";
            if (model.isGoal() == 1) {
                try {
                    handleWin("Black");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (model.isGoal() == 0) {
                try {
                    handleWin("White");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handleWin(String s) throws IOException {
        ButtonType playing = new ButtonType("Keep Playing", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        textField.setText(s + " wins");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Quit to main menu?", playing, quit);
        alert.setContentText(s + " wins");
        alert.setHeaderText("Quit to main menu?");
        alert.setTitle("Quit to main menu?");
        Optional<ButtonType> result = alert.showAndWait();
        if (quit.equals(result.get())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    private void handleTileClick(Position position) {
        model.applyOperator(position);
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
                model.applyOperator(position);
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
