public class GameState {
    /**
     * Represents the GameState.
     */

    private int currentPlayer;
    private King whiteKing;
    private King blackKing;
    private Tiles tiles;

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public King getWhiteKing() {
        return whiteKing;
    }

    public void setWhiteKing(King whiteKing) {
        this.whiteKing = whiteKing;
    }

    public King getBlackKing() {
        return blackKing;
    }

    public void setBlackKing(King blackKing) {
        this.blackKing = blackKing;
    }

    public Tiles getTiles() {
        return tiles;
    }

    public void setTiles(Tiles tiles) {
        this.tiles = tiles;
    }
    /**
     * Creates a {@code GameState} object
     *
     * @param currentPlayer the player who moves next. 0 if white, 1 if black
     * @param whiteKing the object that represents the white king
     * @param blackKing the object that represents the black king
     * @param tiles the object that represents the tiles
     */
    public GameState(int currentPlayer, King whiteKing, King blackKing, Tiles tiles) {
        this.currentPlayer = currentPlayer;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.tiles = tiles;
    }

    public static GameState createNewGame() {
        return new GameState(0, new King(2, 0), new King(3, 7), Tiles.createEmptyTiles());
    }

    @Override
    public String toString() {
        return "GameState{" +
                "currentPlayer=" + currentPlayer +
                ", whiteKing=" + whiteKing +
                ", blackKing=" + blackKing +
                ", tiles=" + tiles +
                '}';
    }
}
