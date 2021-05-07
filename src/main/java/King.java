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
     * Creates a {@code King} object.
     *
     * @param position the position of the king
     */

    public King(Position position) {
        this.position = position;
    }

    /**
     * Creates a {@code King} object.
     *
     * @param row the row coordinate of the king
     * @param col the column coordinate of the king
     */

    public King(int row, int col) {
        this.position = new Position(row, col);
    }

    @Override
    public String toString() {
        return "King{" +
                "position=" + position +
                '}';
    }
}
