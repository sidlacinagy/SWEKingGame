package model;

import java.util.Objects;
import java.util.Spliterators;

public class King {

    /**
     * Represents a king.
     */

    private Position position;

    /**
     * {@return the position of the king}
     */

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Creates a {@code state.King} object.
     *
     * @param position the position of the king
     */

    public King(Position position) {
        this.position = position;
    }

    /**
     * Creates a {@code state.King} object.
     *
     * @param row the row coordinate of the king
     * @param col the column coordinate of the king
     */

    public King(int row, int col) {
        this.position = new Position(row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        King king = (King) o;
        return Objects.equals(position, king.position);
    }

    @Override
    public int hashCode() {
        return getPosition().row()*getPosition().col()*25+45543;
    }

    @Override
    public String toString() {
        return "state.King{" +
                "position=" + position +
                '}';
    }
}
