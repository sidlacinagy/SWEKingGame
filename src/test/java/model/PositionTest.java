package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> Assertions.assertEquals(expectedRow, position.row()),
                () -> Assertions.assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0, 0);
    }

    @Test
    void getTarget() {
        assertPosition(-1, 0, position.getTarget(Direction.UP));
        assertPosition(0, 1, position.getTarget(Direction.RIGHT));
        assertPosition(1, 0, position.getTarget(Direction.DOWN));
        assertPosition(0, -1, position.getTarget(Direction.LEFT));
    }


    @Test
    void setTarget_up() {
        position.setTarget(Direction.UP);
        assertPosition(-1, 0, position);
    }

    @Test
    void setTarget_right() {
        position.setTarget(Direction.RIGHT);
        assertPosition(0, 1, position);
    }

    @Test
    void setTarget_down() {
        position.setTarget(Direction.DOWN);
        assertPosition(1, 0, position);
    }

    @Test
    void setTarget_left() {
        position.setTarget(Direction.LEFT);
        assertPosition(0, -1, position);
    }



    @Test
    void testEquals() {
        Assertions.assertEquals(position, position);
        Assertions.assertEquals(new Position(position.row(), position.col()), position);
        Assertions.assertNotEquals(new Position(Integer.MIN_VALUE, position.col()), position);
        Assertions.assertNotEquals(new Position(position.row(), Integer.MAX_VALUE), position);
        Assertions.assertNotEquals(new Position(Integer.MIN_VALUE, Integer.MAX_VALUE), position);
        Assertions.assertNotEquals(position, null);
        Assertions.assertNotEquals(position, "Hello, World!");
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(position.hashCode(), position.hashCode());
        Assertions.assertEquals(new Position(position.row(), position.col()).hashCode(), position.hashCode());
    }

    @Test
    void testClone() {
        var clone = position.clone();
        Assertions.assertEquals(position, clone);
        assertNotSame(position, clone);
    }

    @Test
    void testToString() {
        Assertions.assertEquals("(0,0)", position.toString());
    }

}
