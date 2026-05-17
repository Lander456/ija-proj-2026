package common;

/**
 * Defines a Node class used to store the position and cost of moving to said position, used for Dijkstra.
 * @param position Position of the tile in the node.
 * @param cost Cost of moving to said tile.
 * @author Tadeas Topinka (xtopint00)
 */
public record Node(Position position,
                   int cost) {
}

