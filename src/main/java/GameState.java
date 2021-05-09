import java.util.Arrays;
import java.util.Objects;

public class GameState {
    /**
     * Represents the GameState.
     */
    private int currentPlayer;
    private int moveIndex;
    private King whiteKing;
    private King blackKing;
    private SquareStatus[][] tiles;

    private static final Position WHITEKINGSTARTPOS = new Position(2, 0);
    private static final Position BLACKKINGSTARTPOS = new Position(3, 7);
    private static final int BOARDLENGTH = 6;
    private static final int BOARDHEIGHT = 8;
    private static final int STARTINGPLAYER = 0;
    private static final int STARTINGMOVE = 0;


    public int getMoveIndex() {
        return moveIndex;
    }

    public void setMoveIndex(int moveIndex) {
        this.moveIndex = moveIndex;
    }

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

    public SquareStatus[][] getTiles() {
        return tiles;
    }

    public void setTiles(SquareStatus[][] tiles) {
        this.tiles = tiles;
    }

    public void setTile(int row, int col, SquareStatus status){

        this.tiles[row][col]=status;
        //exception

    }



    /**
     * Creates a {@code GameState} object
     *
     * @param currentPlayer the player who moves next. 0 if white, 1 if black
     * @param moveIndex     the object that represents the current move. 0 if the king will be moved, 1 if a tile will be removed
     * @param whiteKing     the object that represents the white king
     * @param blackKing     the object that represents the black king
     * @param tiles         the object that represents the tiles
     */
    private GameState(int currentPlayer, int moveIndex, King whiteKing, King blackKing, SquareStatus[][] tiles) {
        this.currentPlayer = currentPlayer;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.tiles = tiles;
        this.moveIndex = moveIndex;
    }

    public static GameState createNewGame() {
        return new GameState(STARTINGPLAYER, STARTINGMOVE, new King(WHITEKINGSTARTPOS), new King(BLACKKINGSTARTPOS), createEmptyTiles());
    }

    public static GameState loadGame(int currentPlayer, int moveIndex, King whiteKing, King blackKing, SquareStatus[][] tiles)  {
        GameState loadedGameState=new GameState(currentPlayer, moveIndex, whiteKing, blackKing, tiles);
        if (loadedGameState.isValidGameState()) {
            return new GameState(currentPlayer, moveIndex, whiteKing, blackKing, tiles);
        }
        throw new IllegalArgumentException();

    }

    public static SquareStatus[][] createEmptyTiles() {
        SquareStatus[][] init = new SquareStatus[BOARDLENGTH][BOARDHEIGHT];
        for (int i = 0; i < init.length; i++) {
            for (int j = 0; j < init[0].length; j++) {
                init[i][j] = SquareStatus.EMPTY;
            }
        }
        return init;

    }

    @Override
    public String toString() {
        return "GameState{" +
                "currentPlayer=" + currentPlayer +
                ", moveIndex=" + moveIndex +
                ", whiteKing=" + whiteKing +
                ", blackKing=" + blackKing +
                ", tiles=" + tiles +
                '}';
    }


    /**
     * @param position the position where the King will be moved or where the tile will be removed.
     * @return returns -2 if the Operator could not be applied, -1 if was successful, but the game not ended, 0 or 1 depending on who have won.
     */

    public int applyOperator(Position position) {


        if (isAppliable(position)) {
            if (this.getMoveIndex() == 0) {
                this.applyKingMove(position);
                //todo notify

            } else if (this.getMoveIndex() == 1) {
                this.applyTileRemove(position);
                //todo notify
            }

            if(this.isGoal()==-1) {
                return -1;
            }
        }

        if(this.isGoal()!=-1){
            return isGoal();
        }



        return -2;

    }

    /**
     * @param position the position where the king will be moved
     * Sets the new {@code GameState} object with the new locations
     */

    public void applyKingMove(Position position) {
        if (this.getCurrentPlayer() == 0) {
            this.setMoveIndex(1);
            this.getWhiteKing().setPosition(position);
        } else if (this.getCurrentPlayer() == 1) {
            this.setMoveIndex(1);
            this.getBlackKing().setPosition(position);
        }

        //todo exception

    }

    /**
     * @param position the position where the tile will be removed
     * Sets the new {@code GameState} object with the new tiles
     */

    public void applyTileRemove(Position position) {
        this.setTile(position.row(),position.col(),SquareStatus.REMOVED);
        this.setMoveIndex(0);
        this.setCurrentPlayer((this.getCurrentPlayer()+1)%2);
    }

    /**
     * @param goalPosition the position of the operator
     * @return returns whether the given {@code Position} could be applied in the current {@code GameState}
     */

    public boolean isAppliable(Position goalPosition) {

        //if the next move is moving the king
        if (this.getMoveIndex() == 0) {
            Position currentKingPosition = null;
            if (this.getCurrentPlayer() == 0) {
                currentKingPosition = this.getWhiteKing().getPosition();
            } else if (this.getCurrentPlayer() == 1) {
                currentKingPosition = this.getBlackKing().getPosition();
            }

            return isKingMoveAppliable(currentKingPosition, goalPosition);

        }
        //if the next move is removing a tile
        else if (this.getMoveIndex() == 1) {
            return isInPlayField(goalPosition) && isEmpty(goalPosition);
        }
        //todo exception
        return false;

    }

    /**
     * @param currentKingPosition the position of the king that will be moved
     * @param goalPosition        the position where the king will be moved
     * @return returns whether the king could be moved to the given location
     */

    public boolean isKingMoveAppliable(Position currentKingPosition, Position goalPosition) {
        return isInPlayField(goalPosition) && isNeighbour(currentKingPosition, goalPosition) && isEmpty(goalPosition);

    }


    public boolean isInPlayField(Position goalPosition) {
        return goalPosition.row() >= 0 && goalPosition.row() < this.getTiles().length &&
                goalPosition.col() >= 0 && goalPosition.col() < this.getTiles()[0].length;
    }

    public static boolean isNeighbour(Position currentKingPosition, Position goalPosition) {
        for (Direction direction : Direction.values()) {
            if (currentKingPosition.getTarget(direction).equals(goalPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(Position goalPosition) {
        return !goalPosition.equals(this.getBlackKing().getPosition()) &&
                !goalPosition.equals(this.getWhiteKing().getPosition()) &&
                this.getTiles()[goalPosition.row()][goalPosition.col()] != SquareStatus.REMOVED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        boolean tile = true;
        for (int i = 0; i < this.getTiles().length; i++) {
            if (!Arrays.equals(gameState.getTiles()[i], this.getTiles()[i])) {
                tile = false;
            }
        }

        return currentPlayer == gameState.currentPlayer &&
                moveIndex == gameState.moveIndex &&
                whiteKing.equals(gameState.whiteKing) &&
                blackKing.equals(gameState.blackKing) &&
                tile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPlayer, moveIndex, whiteKing, blackKing, tiles);
    }

    /**
     * @return returns -1 if it is not a goal state, 0 or 1 depending on which player have won. 0 if white, 1 if black.
     */

    public int isGoal() {
        boolean whiteCanMove=false;
        boolean blackCanMove=false;
        for (Direction direction : Direction.values()) {
            Position whitePosition = new Position(this.getWhiteKing().getPosition().row() + direction.getRowChange(),
                    this.getWhiteKing().getPosition().col() + direction.getColChange());
            if (isAppliable(whitePosition)) {
                whiteCanMove=true;
            }
            Position blackPosition = new Position(this.getBlackKing().getPosition().row() + direction.getRowChange(),
                    this.getBlackKing().getPosition().col() + direction.getColChange());
            if (isAppliable(blackPosition)) {
                blackCanMove=true;
            }
        }

        if(whiteCanMove && blackCanMove){
            return -1;
        }

        if(!blackCanMove){
            return 0;
        }

        if(!whiteCanMove){
            return 1;
        }

        return 9999;


    }

    public boolean isValidGameState() {

        if(countRemovedSquares(this.getTiles())%2!=this.getCurrentPlayer()){
            return false;
        }

        //if either king is not in the playfield returns false
        if(!this.isInPlayField(this.getBlackKing().getPosition())||!this.isInPlayField(this.getWhiteKing().getPosition())){
            return false;
        }
        //if either king is on a removed square or if they are on the same square returns false
        if(this.getTiles()[getBlackKing().getPosition().row()][getBlackKing().getPosition().col()] == SquareStatus.REMOVED ||
                this.getTiles()[getWhiteKing().getPosition().row()][getWhiteKing().getPosition().col()] == SquareStatus.REMOVED ||
                this.getBlackKing().getPosition().equals(this.getWhiteKing().getPosition())){
            return false;
        }


        return true;



    }


    public static int countRemovedSquares(SquareStatus[][] tiles){
        int count=0;
        for(int i=0;i<tiles.length;i++)
            for(int j=0;j<tiles[0].length;j++){
                if(tiles[i][j]==SquareStatus.REMOVED)
                    count++;
            }
        return count;
    }

}
