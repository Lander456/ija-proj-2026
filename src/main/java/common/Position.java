package common;

public record Position(Integer x, Integer y) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position(Integer x1, Integer y1))) return false;

        return x.equals(x1) && y.equals(y1);
    }

}
