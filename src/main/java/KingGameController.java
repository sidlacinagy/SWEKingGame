import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.GameState;
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

    private GameState model = GameState.createNewGame();

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
    private void initialize() {
        createBoard();
        createPieces();
        showSelectablePositions();
        setSelectablePositions();
    }

    private void showSelectablePositions() {
    }

    private void setSelectablePositions() {
    }


    private void createBoard() {
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                var square = createSquare();
                if((i+j)%2==1){
                    square.styleProperty().bind(Bindings.when(model.getTileProperty(i,j).isEqualTo(SquareStatus.EMPTY))
                                    .then("-fx-background-color: black; -fx-fill: black;")
                                    .otherwise("-fx-background-color: gray; -fx-fill: gray;"));
                }
                else{
                    square.styleProperty().bind(Bindings.when(model.getTileProperty(i,j).isEqualTo(SquareStatus.EMPTY))
                            .then("-fx-background-color: white; -fx-fill: white;")
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
        var pieceBlack=createPiece(Color.valueOf("green"));
        getSquare(model.getBlackKing().getPosition()).getChildren().add(pieceBlack);

        model.getWhiteKing().getPositionWrapped().addListener(this::piecePositionChange);
        var pieceWhite=createPiece(Color.valueOf("red"));
        getSquare(model.getWhiteKing().getPosition()).getChildren().add(pieceWhite);


    }



    private StackPane getSquare(Position position) {
        for (var child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private Circle createPiece(Color color) {
        var piece = new Circle(20);
        piece.setFill(color);
        return piece;
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
        if(model.applyOperator(position)==1) {
            if (model.isGoal() == 1) {
                System.out.println("fekete");
            } else if (model.isGoal() == 0) {
                System.out.println("fehér");
            }
        }

    }



}
