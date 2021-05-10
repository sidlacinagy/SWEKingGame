package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class GameStateTest {


    GameState gameStateBase= GameState.createNewGame();
    GameState gameState1= GameState.loadGame(0,1,new King(3,1),new King(3,7), GameState.createEmptyTiles());
    GameState gameState2= GameState.loadGame(0,1,new King(0,0),new King(4,4), GameState.createEmptyTiles());


    @Test
    void testOperator(){
        GameState gameState= GameState.createNewGame();
        gameState.applyOperator(new Position(3,1));
        Assertions.assertEquals((gameState1), gameState);
        gameState.applyOperator(new Position(4,4));
        Assertions.assertEquals(1,gameState2.applyOperator(new Position(1,0)));
        Assertions.assertEquals(-1,gameState2.applyOperator(new Position(7,7)));
    }

    @Test

    void testApplyKingAndTileRemove(){
        GameState gameState3= GameState.loadGame(0,0,new King(3,1),new King(3,7), GameState.createEmptyTiles());
        GameState gameStateTest= GameState.loadGame(0,1,new King(3,2),new King(3,7), GameState.createEmptyTiles());
        gameState3.applyKingMove(new Position(3,2));
        Assertions.assertEquals(gameStateTest,gameState3);
        gameState3.applyTileRemove(new Position(5,5));
        gameStateTest.setMoveIndex(0);
        gameStateTest.setCurrentPlayer(1);
        gameStateTest.setTile(5,5, SquareStatus.REMOVED);
        Assertions.assertEquals(gameStateTest,gameState3);
        gameState3.applyKingMove(new Position(4,6));
        gameStateTest.setMoveIndex(1);
        gameStateTest.setBlackKing(new King(4,6));
        Assertions.assertEquals(gameStateTest,gameState3);
        gameState3.applyTileRemove(new Position(2,2));
        gameStateTest.setTile(2,2, SquareStatus.REMOVED);
        gameStateTest.setMoveIndex(0);
        gameStateTest.setCurrentPlayer(0);
        Assertions.assertEquals(gameStateTest,gameState3);
    }

    @Test
    void testIsAppliable(){
        GameState gameState= GameState.createNewGame();
        Assertions.assertFalse(gameStateBase.isAppliable(new Position(3,3)));
        Assertions.assertTrue(gameState.isAppliable(new Position(3,1)));
        Assertions.assertFalse(gameState.isAppliable(new Position(2,7)));
        gameState.setMoveIndex(1);
        Assertions.assertFalse(gameState.isAppliable(new Position(3,7)));
    }

    @Test
    void testIsGoal(){
        SquareStatus[][] tiles= GameState.createEmptyTiles();
        tiles[0][1]= SquareStatus.REMOVED;
        tiles[1][1]= SquareStatus.REMOVED;
        tiles[1][0]= SquareStatus.REMOVED;
        GameState gameState= GameState.loadGame(1,1,new King(0,0),new King(4,4),tiles);
        Assertions.assertEquals( 1,gameState.isGoal());
        gameState.setWhiteKing(new King(3,3));
        Assertions.assertEquals( -1,gameState.isGoal());
    }

    @Test
    void testIsEmpty(){
        GameState gameState= GameState.createNewGame();
        gameState.applyOperator(new Position(3,1));
    }

    @Test
    void testIsNeighbour(){
       Assertions.assertTrue(GameState.isNeighbour(new Position(3,4),new Position(4,5)));
        Assertions.assertTrue(GameState.isNeighbour(new Position(11,11),new Position(11,12)));
        Assertions.assertFalse(GameState.isNeighbour(new Position(11,11),new Position(1,12)));
    }

    @Test
    void testIsInPlayField(){
        Assertions.assertTrue(gameStateBase.isInPlayField(new Position(5,6)));
        Assertions.assertFalse(gameStateBase.isInPlayField(new Position(8,4)));
    }

    @Test
    void testIsKingMoveAppliable(){
        Assertions.assertTrue(gameStateBase.isKingMoveAppliable(new Position(5,6),new Position(5,7)));
        Assertions.assertFalse(gameStateBase.isKingMoveAppliable(new Position(7,3),new Position(8,4)));
    }

    @Test
    void testIsValidGameState(){
        Assertions.assertTrue(gameStateBase.isValidGameState());
        Assertions.assertTrue(gameState2.isValidGameState());
        gameStateBase.setWhiteKing(new King(8,4));
        Assertions.assertFalse(gameStateBase.isValidGameState());
    }

    @Test

    void testLoadGame()  {

        Assertions.assertEquals(GameState.createNewGame(),
                GameState.loadGame(0,0,new King(2,0),new King(3,7), GameState.createEmptyTiles()));

        assertThrows(IllegalArgumentException.class,()-> GameState.loadGame(0,0,new King(2,0),new King(7,7), GameState.createEmptyTiles()));
        assertThrows(IllegalArgumentException.class,()-> GameState.loadGame(1,0,new King(2,0),new King(3,3), GameState.createEmptyTiles()));
        assertThrows(IllegalArgumentException.class,()-> GameState.loadGame(0,0,new King(2,0),new King(2,0), GameState.createEmptyTiles()));
    }





}
