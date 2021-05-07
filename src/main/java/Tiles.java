import java.util.Arrays;

public class Tiles {
    /**
     * Represents the tiles of the board.
     */

    private SquareStatus tiles[][];

    /**
     * {@return a 2d array representing the tiles.}
     */

    public SquareStatus[][] getTiles() {
        return tiles;
    }

    public void setTiles(SquareStatus[][] tiles) {
        this.tiles = tiles;
    }

    public Tiles(SquareStatus[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Creates an empty {@code Tiles} object.
     * This is the starting position of the game regarding the tiles.
     */

    public static Tiles createEmptyTiles(){
        SquareStatus[][] init=new SquareStatus[6][8];
        for(int i=0;i<init.length;i++){
            for(int j=0;j<init[0].length;j++){
                init[i][j]=SquareStatus.EMPTY;
            }
        }
        return new Tiles(init);

    }

    @Override
    public String toString() {
        return "Tiles{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }
}
