package game;

import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.ActionMenuEvent;
import common.gameEvents.eventTypes.MoveEvent;
import common.Position;
import common.Node;
import common.gameEvents.eventTypes.SelectEvent;
import common.tile.Tile;
import common.unit.Unit;
import common.unit.UnitFactory;
import javafx.scene.input.MouseButton;
import tool.GameObserver;

import java.util.*;

/**
 * The Game class contains the map definition and methods for player interactions (creating units, moving, fighting,
 * etc...)
 * @author Tadeas Topinka (xtopint00)
 */
public class Game {

    private final Tile[][] map;
    private final List<GameObserver> observers = new ArrayList<>();
    private Position selectedUnitPos = null;
    private List<Position> currentReachable = new ArrayList<>();

    /**
     * The constructor for the Game class
     * @param mapDef the map definition for the Game to initialise
     * @author Tadeas Topinka (xtopint00)
     */
    public Game(String[] mapDef){
        int rows = mapDef.length;
        int cols = mapDef[0].split(" ").length;
        this.map = new Tile[rows][cols];

        for (int i = 0; i < rows; i++) {
            String[] rowTerrain = mapDef[i].split(" ");

            for (int j = 0; j < cols; j++) {
                String terrainType = rowTerrain[j];
                this.map[i][j] = new Tile(terrainType, "neutral");
            }
        }
    }

    public Game(String[][] mapDef){
        this.map = new Tile[mapDef.length][mapDef[0].length];

        for (int i = 0; i < mapDef.length; i++) {
            for (int j = 0; j < mapDef[i].length; j++) {
                this.map[i][j] = new Tile(mapDef[i][j], "neutral");
            }
        }
    }

    /**
     * Uses the UnitFactory to create a new unit of a specified type on a specified tile
     * @param type which type of unit to initialise
     * @param player which player the unit belongs to
     * @param startX X coordinate of the tile where the unit should spawn
     * @param startY Y coordinate of the tile where the unit should spawn
     * @return a new unit instance with its player ownership, type and poisitional info
     * @author Tadeas Topinka (xtopint00)
     */
    public Unit createUnit(String type, String player, Integer startX, Integer startY) {
        Unit unit = UnitFactory.create(type, player, startX, startY);
        map[startY][startX].setUnit(unit);
        return unit;
    }

    /**
     * Attempts to move a unit from a certain tile to another
     * @param startPosition position from which the unit will be moving
     * @param destination position to which the unit will move
     * @return boolean indicating whether the unit has moved successfully
     * @author Tadeas Topinka (xtopint00)
     */
    public boolean moveUnit(Position startPosition, Position destination) {
        if (getReachableTiles(startPosition).contains(destination)) {
            Unit movedUnit = map[startPosition.getX()][startPosition.getY()].getUnit();

            if (movedUnit == null) return false;

            map[destination.getX()][destination.getY()].setUnit(movedUnit);
            map[startPosition.getX()][startPosition.getY()].setUnit(null);

            movedUnit.setPosition(destination);
            movedUnit.setHasMoved(true);

            notifyObservers(new MoveEvent(startPosition, destination));

            return true;
        }
        return false;
    }

    /**
     * Adds a new observer into the list of observers
     * @param observer a GameObserver instance to add to the observers list
     * @author Tadeas Topinka (xtopint00)
     */
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * notifies all observers in the observer list of a certain event
     * @param event GameEvent to notify the observers about
     * @author Tadeas Topinka (xtopint00)
     */
    public void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.update(event);
        }
    }

    /**
     * method used to get reachable tiles from a certain position on the map
     * @param position position from which to search
     * @return list of all reachable tiles
     */
    public List<Position> getReachableTiles(Position position) {
        int maxCost;
        boolean isWheeled;

        var tile = map[position.getX()][position.getY()];
        var unit = tile.getUnit();

        if (unit == null) {
            return Collections.emptyList();
        }

        String unitType = unit.getUnitType();

        if (unitType.equals("Tank")) {
            maxCost = 6;
            isWheeled = true;
        } else if (unitType.equals("Infantry")) {
            maxCost = 3;
            isWheeled = false;
        } else {
            return Collections.emptyList();  // no unit on given position
        }

        // DIJKSTRA
        int[][] directions = {
                {-1, 0},  // up
                {1, 0},   // down
                {0, -1},  // left
                {0, 1}    // right
        };

        int rows = map.length;
        int cols = map[0].length;

        int[][] distance = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                distance[r][c] = Integer.MAX_VALUE;  // set all elements to "INF"
            }
        }

        distance[position.getX()][position.getY()] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.getCost()));
        pq.add(new Node(position, 0));  // add starting position

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();

            int r = currentNode.getPosition().getX();
            int c = currentNode.getPosition().getY();

            if (currentNode.getCost() > distance[r][c]) continue;

            for (int[] d : directions) {  // neighboring cells
                int newRow = r + d[0];
                int newCol = c + d[1];

                // checking bounds
                if (newRow >= 0 && newCol >= 0 && newRow < rows && newCol < cols) {

                    Integer terrainCost = map[newRow][newCol].getTerrain().getMovementCost(isWheeled);
                    if (terrainCost == null) {
                        continue;
                    }
                    int newCost = currentNode.getCost() + terrainCost;


                    if (newCost < distance[newRow][newCol] && newCost <= maxCost) {
                        distance[newRow][newCol] = newCost;
                        pq.add(new Node(new Position(newRow, newCol), newCost));
                    }
                }
            }
        }

        List<Position> reachableTiles = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (distance[r][c] <= maxCost && distance[r][c] != Integer.MAX_VALUE
                        && !(r == position.getX() && c == position.getY())) {
                    reachableTiles.add(new Position(r,c));
                }
            }
        }

        return reachableTiles;
    }

    public Tile[][] getMap() { return map; }

    public void updateTerrainOwner(int x, int y, String player) {
        if (x >= 0 && x < map[0].length && y >= 0 && y < map.length) {
            String terrainType = map[y][x].getTerrain().getTerrainCode();
            System.out.println(map[y][x].getTerrain().getTerrainType());
            map[y][x] = new Tile(terrainType, player);
        }
    }

    public void handleInput(int r, int c, MouseButton button) {
        Position clickedPos = new Position(r, c);

        if (button == MouseButton.PRIMARY && map[r][c].getUnit() != null) {
            this.selectedUnitPos = new Position(r, c);
            this.currentReachable = getReachableTiles(clickedPos);
            notifyObservers(new SelectEvent(clickedPos, currentReachable, true));
        } else if (button == MouseButton.SECONDARY && selectedUnitPos != null) {
            System.out.println(clickedPos.toString());
            System.out.println(selectedUnitPos.toString());
            System.out.println(currentReachable.toString());
            if (currentReachable.contains(clickedPos)) {
                System.out.println("notifying");
                notifyObservers(new ActionMenuEvent(clickedPos));
            } else {
                cancelSelection();
            }
        }
    }

    private void cancelSelection() {
        this.selectedUnitPos = null;
        this.currentReachable.clear();
        notifyObservers(new SelectEvent(null, null, false));
    }

    public void confirmMove(Position position) {
        if (moveUnit(selectedUnitPos, position)) {
            notifyObservers(new MoveEvent(selectedUnitPos, position));
        }
    }
}
