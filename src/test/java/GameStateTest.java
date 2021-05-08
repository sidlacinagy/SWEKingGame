
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;


public class GameStateTest {

    @Test
    void testOperator(){
        GameState gameState=GameState.createNewGame();
        gameState.applyOperator(new Position(3,1));
        assertEquals( gameState,(GameState.loadGame(0,1,new King(3,1),new King(3,7),GameState.createEmptyTiles())));
        gameState.applyOperator(new Position(4,4));
        SquareStatus[][] tiles =GameState.createEmptyTiles();
        tiles[4][4]=SquareStatus.REMOVED;
        assertEquals(gameState,(GameState.loadGame(1,0,new King(3,1),new King(3,7),tiles)));

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
        SquareStatus[][] tiles=GameState.createEmptyTiles();
        tiles[0][1]=SquareStatus.REMOVED;
        tiles[1][1]=SquareStatus.REMOVED;
        tiles[1][0]=SquareStatus.REMOVED;
        GameState gameState=GameState.loadGame(0,0,new King(0,0),new King(4,4),tiles);
        assertEquals(gameState.isGoal(), 1);
    }



}
