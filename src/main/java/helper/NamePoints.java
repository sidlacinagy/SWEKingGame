package helper;

/**
 * Represents a name-point key-value pair.
 */
public class NamePoints {
    private String name;
    private Integer point;

    /**
     * Creates a name-point key-value pair.
     * @param name the name of the player.
     * @param point the point of the player.
     */
    public NamePoints(String name, Integer point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
