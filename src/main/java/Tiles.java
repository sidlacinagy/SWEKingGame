import java.util.Arrays;

public class Tiles {
    /**
     * Represents the tiles of the board.
     */

    private SquareStatus[][] tiles;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tiles tiles1 = (Tiles) o;
        for(int i=0;i<this.getTiles().length;i++){
            if (!Arrays.equals(tiles1.getTiles()[i],this.getTiles()[i])){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    @Override
    public String toString() {
        StringBuilder string;
        string = new StringBuilder("Tiles{" +
                "tiles=");
                for(int i=0;i<getTiles().length;i++){
                    string.append(Arrays.toString(getTiles()[i]));
                }
                string.append('}');
                return string.toString();
    }
}
