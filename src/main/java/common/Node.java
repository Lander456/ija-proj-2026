package common;

public class Node {
    private final Position position;
    private final int cost;

    public Node(Position position, int cost) {
        this.position = position;
        this.cost = cost;
    }

    public Position getPosition() {
        return position;
    }

    public int getCost() { return cost; }
}

