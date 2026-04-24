package common;

public class Position {
    private Integer x;
    private Integer y;

    public Integer getX() { return x; }
    public Integer getY() { return y; }

    public void setX(Integer x) { this.x = x; }
    public void setY(Integer y) { this.y = y; }

    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position p = (Position) o;
        return x.equals(p.x) && y.equals(p.y);
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
