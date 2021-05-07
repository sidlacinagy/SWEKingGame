
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;


public class GameStateTest {

    @Test
    void testOperator(){
        GameState gameState=GameState.createNewGame();
        gameState.applyOperator(new Position(3,1));
        assertEquals( gameState,(new GameState(0,1,new King(3,1),new King(3,7),Tiles.createEmptyTiles())));
        gameState.applyOperator(new Position(4,4));
        Tiles tiles =Tiles.createEmptyTiles();
        tiles.getTiles()[4][4]=SquareStatus.REMOVED;
        assertEquals(gameState,(new GameState(1,0,new King(3,1),new King(3,7),tiles)));

    }
    @Test
    void testIsAppliable(){
        GameState gameState=GameState.createNewGame();
        assertFalse(gameState.isAppliable(new Position(3,3)));
        gameState.applyOperator(new Position(3,1));
        assertFalse(gameState.isAppliable(new Position(3,1)));
        assertTrue(gameState.isAppliable(new Position(2,7)));


    }

    @Test
    void testIsGoal(){
        Tiles tiles=Tiles.createEmptyTiles();
        tiles.getTiles()[0][1]=SquareStatus.REMOVED;
        tiles.getTiles()[1][1]=SquareStatus.REMOVED;
        tiles.getTiles()[1][0]=SquareStatus.REMOVED;
        GameState gameState=new GameState(0,0,new King(0,0),new King(4,4),tiles);
        assertEquals(gameState.isGoal(), 1);
    }



}
