import java.util.Objects;

public class GameState {
    /**
     * Represents the GameState.
     */
    private int currentPlayer;
    private int moveIndex;
    private King whiteKing;
    private King blackKing;
    private Tiles tiles;


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

    public Tiles getTiles() {
        return tiles;
    }

    public void setTiles(Tiles tiles) {
        this.tiles = tiles;
    }

    public void setGameState(GameState gameState){
        this.setBlackKing(gameState.getBlackKing());
        this.setCurrentPlayer(gameState.getCurrentPlayer());
        this.setMoveIndex(gameState.getMoveIndex());
        this.setTiles(gameState.getTiles());
        this.setWhiteKing(gameState.getWhiteKing());
    }

    /**
     * Creates a {@code GameState} object
     *
     * @param currentPlayer the player who moves next. 0 if white, 1 if black
     * @param moveIndex the object that represents the current move. 0 if the king will be moved, 1 if a tile will be removed
     * @param whiteKing the object that represents the white king
     * @param blackKing the object that represents the black king
     * @param tiles the object that represents the tiles
     */
    public GameState(int currentPlayer, int moveIndex, King whiteKing, King blackKing, Tiles tiles) {
        this.currentPlayer = currentPlayer;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.tiles = tiles;
        this.moveIndex = moveIndex;
    }

    public static GameState createNewGame() {
       return new GameState(0, 0, new King(2, 0), new King(3, 7), Tiles.createEmptyTiles());
    }

    public static GameState loadGame(int currentPlayer, int moveIndex, King whiteKing, King blackKing, Tiles tiles){
        if(isValidSaveGame(new GameState(currentPlayer,moveIndex,whiteKing,blackKing,tiles)))
        {
        return new GameState(currentPlayer,moveIndex,whiteKing,blackKing,tiles);
        }
        else{
            System.out.println("Hibás fájl");
        }
        return null;
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
     * @return returns -2 if the Operator could not be applied, -1 if was succesful, but the game not ended, 0 or 1 depending on who have won.
     */

    public int applyOperator(Position position){

       if (isAppliable(position)){
            if(this.getMoveIndex()==0){
                this.setGameState(applyKingMove(position));
                //todo notify
                if(this.isGoal()!=-1){
                    return this.isGoal();
                }
            }

            else if(this.getMoveIndex()==1){
                this.setGameState(applyTileRemove(position));
                //todo notify
            }

            return -1;

        }
        return -2;

    }

    /**
     * @param position the position where the king will be moved
     * @return the new {@code GameState} object with the new locations
     */

    public GameState applyKingMove(Position position){
        if(this.getCurrentPlayer()==0){
            return new GameState(this.getCurrentPlayer(),1,new King(position),this.getBlackKing(),this.getTiles());}
        else if(this.getCurrentPlayer()==1){
            return new GameState(this.getCurrentPlayer(),1, this.getWhiteKing(), new King(position),this.getTiles());}
        return null;
    }

    /**
     * @param position the position where the tile will be removed
     * @return the new {@code GameState} object with the new locations
     */

    public GameState applyTileRemove(Position position){
        SquareStatus[][] tiles =this.getTiles().getTiles();
        tiles[position.row()][position.col()]=SquareStatus.REMOVED;

        return new GameState((this.getCurrentPlayer()+1)%2,0,this.getWhiteKing(),this.getBlackKing(),new Tiles(tiles));
    }

    /**
     * @param goalPosition the position of the operator
     * @return returns whether the given {@code Position} could be applied in the current {@code GameState}
     */

    public boolean isAppliable(Position goalPosition){

        //if the next move is moving the king
            if(this.getMoveIndex()==0){
                Position currentKingPosition=null;
                if(this.getCurrentPlayer()==0) {
                    currentKingPosition = this.getWhiteKing().getPosition();
                }
                else if(this.getCurrentPlayer()==1){
                    currentKingPosition = this.getBlackKing().getPosition();
                }

               return isKingMoveAppliable(currentKingPosition,goalPosition);

            }
            else if(this.getMoveIndex()==1){
                    return isInPlayField(goalPosition) && isEmpty(goalPosition);
            }

            return false;

    }

    /**
     * @param currentKingPosition the position of the king that will be moved
     * @param goalPosition the position where the king will be moved
     * @return returns whether the king could be moved to the given location
     */

    public boolean isKingMoveAppliable(Position currentKingPosition,Position goalPosition){
        return isInPlayField(goalPosition) && isNeighbour(currentKingPosition,goalPosition) && isEmpty(goalPosition);

    }


    public boolean isInPlayField(Position goalPosition){
        return goalPosition.row() >= 0 && goalPosition.row() < this.getTiles().getTiles().length &&
                goalPosition.col() >= 0 && goalPosition.col() < this.getTiles().getTiles()[0].length;
    }

    public boolean isNeighbour(Position currentKingPosition,Position goalPosition){
        for (Direction direction : Direction.values()) {
            if(currentKingPosition.getTarget(direction).equals(goalPosition)){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(Position goalPosition){
        return !goalPosition.equals(this.getBlackKing().getPosition()) &&
                !goalPosition.equals(this.getWhiteKing().getPosition()) &&
                this.getTiles().getTiles()[goalPosition.row()][goalPosition.col()] != SquareStatus.REMOVED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return currentPlayer == gameState.currentPlayer &&
                moveIndex == gameState.moveIndex &&
                whiteKing.equals(gameState.whiteKing) &&
                blackKing.equals(gameState.blackKing) &&
                tiles.equals(gameState.tiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPlayer, moveIndex, whiteKing, blackKing, tiles);
    }

    /**
     * @return returns -1 if it is not a goal state, 0 or 1 depending on which player have won. 0 if white, 1 if black.
     */

    public int isGoal(){
        King currentKing;
        if(this.getCurrentPlayer()==0){
           currentKing=this.getWhiteKing();
        }
        else{
           currentKing=this.getBlackKing();
        }
       for(Direction direction : Direction.values()){
           Position position=new Position(currentKing.getPosition().row()+direction.getRowChange(),
                   currentKing.getPosition().col()+direction.getColChange());
           if(isAppliable(position)){
               return -1;
           }
       }
       return (this.getCurrentPlayer()+1)%2;

    }

    public static boolean isValidSaveGame(GameState gameState){
        //todo

        return false;
    }
}
