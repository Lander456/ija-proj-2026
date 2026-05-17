package common;

/**
 * Defines a position record, used to store an x and a y coordinate in tandem, defining a concrete position on the game
 * map.
 * @param x X coordinate.
 * @param y Y coordinate.
 * @author Tadeas Topinka (xtopint00)
 */
public record Position(Integer x, Integer y) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position(Integer x1, Integer y1))) return false;

        return x.equals(x1) && y.equals(y1);
    }

}
