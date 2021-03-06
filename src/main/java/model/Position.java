package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents a 2D position.
 */
public class Position implements Cloneable {

    private int row;

    private int col;

    /**
     * Creates a {@code state.Position} object.
     *
     * @param row the row coordinate of the position
     * @param col the column coordinate of the position
     */
    @JsonCreator
    public Position(@JsonProperty("row") int row, @JsonProperty("col") int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return the row coordinate of the position.
     */
    @JsonGetter
    public int row() {
        return row;
    }

    /**
     * @return the column coordinate of the position.
     */
    @JsonGetter
    public int col() {
        return col;
    }

    /**
     * @return the position whose vertical and horizontal distances from this
     * position are equal to the coordinate changes of the direction given.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    @JsonIgnore
    public Position getTarget(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }



    /**
     * Changes the position by the coordinate changes of the direction given.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public void setTarget(Direction direction) {
        row += direction.getRowChange();
        col += direction.getColChange();
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (! (o instanceof Position)) {
            return false;
        }
        return  ((Position)o).row == row && ((Position)o).col == col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public Position clone() {
        Position copy;
        try {
            copy = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        return copy;
    }


    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}