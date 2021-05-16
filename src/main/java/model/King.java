package model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Represents a king.
 */
public class King {



    private ReadOnlyObjectWrapper<Position> position;

    /**
     * @return Returns the wrapped position of the king.
     */
    @JsonIgnore
    public ReadOnlyObjectWrapper<Position> getPositionWrapped() {
        return position;
    }
    /**
     * @return Returns the position of the king.
     */
    @JsonGetter
    public Position getPosition() {
        return position.get();
    }

    public void setPosition(Position position) {
        this.position.set(position);
    }

    /**
     * Creates a {@code state.King} object.
     *
     * @param position the position of the king
     */

    public King(Position position) {
        this.position=new ReadOnlyObjectWrapper<>(position);
    }


    /**
     * Creates a {@code state.King} object.
     *
     * @param row the row coordinate of the king
     * @param col the column coordinate of the king
     */

    public King(int row, int col) {
        this.position = new ReadOnlyObjectWrapper<>(new Position(row, col));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        King king = (King) o;
        return king.getPosition().equals(this.getPosition());
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
