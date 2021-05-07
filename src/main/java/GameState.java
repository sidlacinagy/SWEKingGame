public class GameState {
    /**
     * Represents the GameState.
     */
    private static GameState gameState=null;
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

    /**
     * Creates a {@code GameState} object
     *
     * @param currentPlayer the player who moves next. 0 if white, 1 if black
     * @param moveIndex the object that represents the current move. 0 if the king will be moved, 1 if a tile will be removed
     * @param whiteKing the object that represents the white king
     * @param blackKing the object that represents the black king
     * @param tiles the object that represents the tiles
     */
    private GameState(int currentPlayer, int moveIndex, King whiteKing, King blackKing, Tiles tiles) {
        this.currentPlayer = currentPlayer;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.tiles = tiles;
        this.moveIndex = moveIndex;
    }

    public static void createNewGame() {
        gameState= new GameState(0, 0, new King(2, 0), new King(3, 7), Tiles.createEmptyTiles());
    }

    public static void loadGame(int currentPlayer, int moveIndex, King whiteKing, King blackKing, Tiles tiles){
        if(isValidSaveGame(new GameState(currentPlayer,moveIndex,whiteKing,blackKing,tiles)))
        {
        gameState=new GameState(currentPlayer,moveIndex,whiteKing,blackKing,tiles);
        }
        else{
            System.out.println("Hibás fájl");
        }
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

//return -1 ha volt alkalmazott, 0 vagy 1 ha nyert valaki, -2 ha a lépés nem engedélyezett
    public int applyOperator(Position position){

       if (isAppliable(position)){

            if(this.getMoveIndex()==0){

                gameState=gameState.applyKingMove(position);
                //todo notify
                if(isGoal(gameState)!=-1){
                    return isGoal(gameState);
                }
            }

            else if(this.getMoveIndex()==1){

                gameState=gameState.applyTileRemove(position);
                //todo notify
            }

            return -1;

        }
        return -2;

    }

    public GameState applyKingMove(Position position){
            return new GameState(this.getCurrentPlayer(),1,new King(position),this.getBlackKing(),this.getTiles());
    }

    public GameState applyTileRemove(Position position){
        SquareStatus[][] tiles =this.getTiles().getTiles();
        tiles[position.row()][position.col()]=SquareStatus.REMOVED;

        return new GameState((this.getCurrentPlayer()+1)%2,0,this.getWhiteKing(),this.getBlackKing(),new Tiles(tiles));
    }



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
    //if the next move is removing a tile
            else if(this.getMoveIndex()==1){
                    return isInPlayField(goalPosition) && isOccupied(goalPosition);
            }

            return false;

    }


    public boolean isKingMoveAppliable(Position currentKingPosition,Position goalPosition){

        return isInPlayField(goalPosition) && isNeighbour(currentKingPosition,goalPosition) && isOccupied(goalPosition);

    }


    public boolean isInPlayField(Position goalPosition){
        return goalPosition.row() >= 0 && goalPosition.row() < this.getTiles().getTiles().length &&
                goalPosition.col() >= 0 && goalPosition.col() < this.getTiles().getTiles()[0].length;
    }

    public boolean isNeighbour(Position currentKingPosition,Position goalPosition){
        for (Direction direction : Direction.values()) {
            if(currentKingPosition.getTarget(direction)==goalPosition){
                return true;
            }
        }
        return false;
    }

    public boolean isOccupied(Position goalPosition){
        return !goalPosition.equals(this.getBlackKing().getPosition()) &&
                !goalPosition.equals(this.getWhiteKing().getPosition()) &&
                this.getTiles().getTiles()[goalPosition.row()][goalPosition.col()] != SquareStatus.REMOVED;
    }





    public int isGoal(GameState gameState){
        King currentKing;
        if(gameState.getCurrentPlayer()==0){
           currentKing=gameState.getWhiteKing();
        }
        else{
           currentKing=gameState.getBlackKing();
        }
       for(Direction direction : Direction.values()){
           Position position=new Position(currentKing.getPosition().row()+direction.getRowChange(),
                   currentKing.getPosition().col()+direction.getColChange());
           if(gameState.isAppliable(position)){
               return -1;
           }
       }
       return (gameState.getCurrentPlayer()+1)%2;

    }

    public static boolean isValidSaveGame(GameState gameState){
        //todo

        return false;
    }
}
