
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;


public class GameStateTest {


    GameState gameStateBase=GameState.createNewGame();
    GameState gameState1=GameState.loadGame(0,1,new King(3,1),new King(3,7),GameState.createEmptyTiles());
    GameState gameState2=GameState.loadGame(0,1,new King(0,0),new King(4,4),GameState.createEmptyTiles());


    @Test
    void testOperator(){
        GameState gameState=GameState.createNewGame();
        gameState.applyOperator(new Position(3,1));
        assertEquals((gameState1), gameState);
        gameState.applyOperator(new Position(4,4));
        gameState2.setTile(0,1,SquareStatus.REMOVED);
        gameState2.setTile(1,1,SquareStatus.REMOVED);
        assertEquals(1,gameState2.applyOperator(new Position(1,0)));


    }
    @Test
    void testIsAppliable(){
        GameState gameState=GameState.createNewGame();
        assertFalse(gameStateBase.isAppliable(new Position(3,3)));
        assertTrue(gameState.isAppliable(new Position(3,1)));
        assertFalse(gameState.isAppliable(new Position(2,7)));
        gameState.setMoveIndex(1);
        assertFalse(gameState.isAppliable(new Position(3,7)));
    }

    @Test
    void testIsGoal(){
        SquareStatus[][] tiles=GameState.createEmptyTiles();
        tiles[0][1]=SquareStatus.REMOVED;
        tiles[1][1]=SquareStatus.REMOVED;
        tiles[1][0]=SquareStatus.REMOVED;
        GameState gameState=GameState.loadGame(1,1,new King(0,0),new King(4,4),tiles);
        assertEquals( 1,gameState.isGoal());
        gameState.setWhiteKing(new King(3,3));
        assertEquals( -1,gameState.isGoal());
    }

    @Test
    void testIsEmpty(){
        GameState gameState=GameState.createNewGame();
        gameState.applyOperator(new Position(3,1));
    }

    @Test
    void testIsNeighbour(){
       assertTrue(GameState.isNeighbour(new Position(3,4),new Position(4,5)));
        assertTrue(GameState.isNeighbour(new Position(11,11),new Position(11,12)));
        assertFalse(GameState.isNeighbour(new Position(11,11),new Position(1,12)));
    }

    @Test
    void testIsInPlayField(){
        assertTrue(gameStateBase.isInPlayField(new Position(5,6)));
        assertFalse(gameStateBase.isInPlayField(new Position(8,4)));
    }

    @Test
    void testIsKingMoveAppliable(){
        assertTrue(gameStateBase.isKingMoveAppliable(new Position(5,6),new Position(5,7)));
        assertFalse(gameStateBase.isKingMoveAppliable(new Position(7,3),new Position(8,4)));
    }

    @Test
    void testIsValidGameState(){
        assertTrue(gameStateBase.isValidGameState());
        assertTrue(gameState2.isValidGameState());
        gameStateBase.setWhiteKing(new King(8,4));
        assertFalse(gameStateBase.isValidGameState());
    }

    @Test

    void testLoadGame()  {

        assertEquals(GameState.createNewGame(),
                GameState.loadGame(0,0,new King(2,0),new King(3,7),GameState.createEmptyTiles()));

        assertThrows(IllegalArgumentException.class,()->GameState.loadGame(0,0,new King(2,0),new King(7,7),GameState.createEmptyTiles()));

    }





}
