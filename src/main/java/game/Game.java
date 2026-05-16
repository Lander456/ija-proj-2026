package game;

import common.Player;
import common.enums.*;
import common.gameActions.gameActionsImpl.*;
import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.*;
import common.Position;
import common.Node;
import common.terrain.Capturable;
import common.terrain.Terrain;
import common.tile.Tile;
import common.unit.Unit;
import common.unit.UnitFactory;
import common.unit.UnitRegistry;
import tool.io.dto.GameConfig;
import tool.gameObserver.GameObserver;

import java.util.*;

/**
 * The Game class contains the map definition and methods for player interactions (creating units, moving, fighting,
 * etc...)
 * @author Tadeas Topinka (xtopint00)
 */
public class Game {

    private boolean redAI;
    private boolean blueAI;
    private Player bluePlayer;
    private Player redPlayer;
    private Player activePlayer;
    private GameState gameState = GameState.PLAY;
    private GameConfig gameConfig;

    public final Journal gameJournal = new Journal();
    private final Tile[][] map;
    private final List<GameObserver> observers = new ArrayList<>();
    private Position selectedTilePos = null;
    private List<Position> currentReachable = new ArrayList<>();

    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        notifyObservers(new GameStateChangedEvent(gameState));
    }

    public GameState getGameState() {
        return gameState;
    }

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
        this.redPlayer = new Player(Players.RED, 2000, new ArrayList<>(),  new ArrayList<>());
        this.bluePlayer = new Player(Players.BLUE, 2000, new ArrayList<>(), new ArrayList<>());

        this.activePlayer = redPlayer;

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
     * @param startX X coordinate of the tile position the unit should spawn
     * @param startY Y coordinate of the tile position the unit should spawn
     * @return a new unit instance with its player ownership, type and poisitional info
     * @author Tadeas Topinka (xtopint00)
     */
    public Unit createUnit(String type, String player, Integer startX, Integer startY) {
        Unit unit = UnitFactory.create(type, player, startX, startY);
        System.out.println(unit.getOwnedBy().toString());
        map[startY][startX].setUnit(unit);
        if (player.equalsIgnoreCase(Players.RED.toString()) || player.equalsIgnoreCase(Players.RED.getLabel())) {
            redPlayer.addUnit(unit);
        } else {
            bluePlayer.addUnit(unit);
        }

        return unit;
    }

    /**
     * Attempts to move a unit attackerPosition a certain tile to another
     * @param startPosition position attackerPosition which the unit will be moving
     * @param destination position to which the unit will move
     * @author Tadeas Topinka (xtopint00)
     */
    public void moveUnit(Position startPosition, Position destination, Unit movedUnit) {
        if (getReachableTiles(startPosition).contains(destination)) {

            map[destination.getY()][destination.getX()].setUnit(movedUnit);
            map[startPosition.getY()][startPosition.getX()].setUnit(null);

            movedUnit.setPosition(destination);
            movedUnit.setHasMoved(true);

            if (movedUnit.getUnitType() == UnitTypes.Artillery) {
                movedUnit.setHasAttacked(true);
            }

            notifyObservers(new MoveEvent(startPosition, destination));
        }
    }

    public void teleportUnit(Position destination, Unit unit) {
        Position startPosition = unit.getPosition();

        map[startPosition.getY()][startPosition.getX()].setUnit(null);
        map[destination.getY()][destination.getX()].setUnit(unit);

        unit.setPosition(destination);

        notifyObservers(new MoveEvent(startPosition, destination));
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
     * method used to get reachable tiles attackerPosition a certain position on the map
     * @param position position attackerPosition which to search
     * @return list of all reachable tiles
     */
    public List<Position> getReachableTiles(Position position) {
        int maxCost;
        boolean isWheeled;

        var tile = map[position.getY()][position.getX()];
        var unit = tile.getUnit();

        if (unit == null) {
            return Collections.emptyList();
        }

        maxCost = unit.movementRange();
        isWheeled = unit.movementType() == MovementTypes.WHEELS;

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

        distance[position.getY()][position.getX()] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.getCost()));
        pq.add(new Node(position, 0));  // add starting position

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();

            int r = currentNode.getPosition().getY();
            int c = currentNode.getPosition().getX();

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
                        pq.add(new Node(new Position(newCol, newRow), newCost));
                    }
                }
            }
        }

        List<Position> reachableTiles = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (distance[r][c] <= maxCost && distance[r][c] != Integer.MAX_VALUE
                        && !(r == position.getY() && c == position.getX())) {

                    if (map[r][c].getUnit() == null) {
                        reachableTiles.add(new Position(c, r));
                    }
                }
            }
        }

        return reachableTiles;
    }

    public Tile[][] getMap() { return map; }

    public void updateTerrainOwner(int x, int y, String player) {
        if (x >= 0 && x < map[0].length && y >= 0 && y < map.length) {
            String terrainType = map[y][x].getTerrain().getTerrainCode();
            map[y][x] = new Tile(terrainType, player);
            System.out.println("Player " + player);
            if (Players.fromString(player).equals(Players.RED)) {
                bluePlayer.removeProperty((Capturable) map[y][x].getTerrain());
                redPlayer.addProperty((Capturable) map[y][x].getTerrain());
            } else {
                redPlayer.removeProperty((Capturable) map[y][x].getTerrain());
                bluePlayer.addProperty((Capturable) map[y][x].getTerrain());
            }
        }
    }

    public void selectTile(Position position) {
        this.selectedTilePos = new Position(position.getX(), position.getY());
        Unit selectedUnit = map[position.getY()][position.getX()].getUnit();
        if (selectedUnit != null && selectedUnit.getOwnedBy() == activePlayer.getSide() && !selectedUnit.getHasMoved()) {
            this.currentReachable = getReachableTiles(position);
            notifyObservers(new SelectEvent(position, currentReachable, true));
        } else {
            cancelSelection();
        }
    }

    public void handleActionMenu(Position position) {
        if (this.selectedTilePos == null) {
            return;
        }

        List<Actions> actions = new ArrayList<>();
        Terrain terrain = map[position.getY()][position.getX()].getTerrain();
        Unit selectedUnit = map[this.selectedTilePos.getY()][this.selectedTilePos.getX()].getUnit();
        Unit unit = map[position.getY()][position.getX()].getUnit();

        if (selectedUnit != null && (!selectedUnit.getHasMoved() || !selectedUnit.getHasAttacked())) {
            if (unit != null && getAttackReach(selectedTilePos, selectedUnit.minAttackRange(), selectedUnit.maxAttackRange()).contains(position) && !selectedUnit.getHasAttacked()) {
                actions.add(Actions.ATTACK);
            } else if (!selectedUnit.getHasMoved() && !position.equals(selectedUnit.getPosition())) {
                actions.add(Actions.MOVE);
            } else if (terrain instanceof Capturable && ((Capturable) terrain).getOwner() != this.activePlayer.getSide() && position.equals(selectedUnit.getPosition()) && selectedUnit.canCapture() && !selectedUnit.getHasMoved() && !selectedUnit.getHasAttacked()) {
                actions.add(Actions.CAPTURE);
            }
        }

        if (terrain.spawnsUnits() && unit == null && ((Capturable) terrain).getOwner() == this.activePlayer.getSide()) {
            actions.add(Actions.BUILD);
        }

        notifyObservers(new ActionMenuEvent(position, actions));
    }

    public void handleAttack(Position target) {
        Unit attackingUnit = map[this.selectedTilePos.getY()][this.selectedTilePos.getX()].getUnit();
        Unit targetUnit = map[target.getY()][target.getX()].getUnit();

        if (attackingUnit != null && targetUnit != null) {
            gameJournal.addAndExecute(new AttackAction(this, attackingUnit, targetUnit, attackingUnit.getHealth(), targetUnit.getHealth()));
        }
    }

    public void handleCapture(Position position) {
        Tile tile = map[position.getY()][position.getX()];

        Capturable capturable = (Capturable) tile.getTerrain();
        Unit unit = tile.getUnit();

        gameJournal.addAndExecute(new CaptureAction(this, position, unit, capturable.getOwner(), capturable.getResistance()));
    }

    public void transferProperty(Capturable property, Players oldOwner, Players newOwner, Position position) {
        if (oldOwner == Players.NEUTRAL) {
            Player newPlayerOwner = (newOwner == Players.RED) ? redPlayer : bluePlayer;
            newPlayerOwner.addProperty(property);
        } else if (newOwner == Players.NEUTRAL) {
            Player oldPlayerOwner = (oldOwner == Players.RED) ? redPlayer : bluePlayer;
            oldPlayerOwner.removeProperty(property);
        } else {
            Player oldPlayerOwner = (oldOwner == Players.RED) ? redPlayer : bluePlayer;
            Player newPlayerOwner = (newOwner == Players.RED) ? redPlayer : bluePlayer;

            oldPlayerOwner.removeProperty(property);
            newPlayerOwner.addProperty(property);
        }

        notifyObservers(new CaptureEvent(position, newOwner));
    }

    private void cancelSelection() {
        this.currentReachable.clear();
        notifyObservers(new SelectEvent(null, null, false));
    }

    public Boolean captureTile(Position position, Unit unit) {
        boolean captured = false;
        Capturable capturable = (Capturable) map[position.getY()][position.getX()].getTerrain();
        Players oldController = capturable.getOwner();

        capturable.capture(unit);
        unit.setHasMoved(true);
        unit.setHasAttacked(true);

        if (capturable.getOwner() != oldController) {
            captured = true;
            transferProperty(capturable, oldController, capturable.getOwner(), position);
        }

        return captured;
    }

    public Tile getTile(Position position) {
        return map[position.getY()][position.getX()];
    }

    public void handleMove(Position position) {
        Unit movedUnit = map[selectedTilePos.getY()][selectedTilePos.getX()].getUnit();
        if (movedUnit == null) {
            return;
        }

        if (!getReachableTiles(selectedTilePos).contains(position)) {
            return;
        }

        MoveAction move = new MoveAction(this, selectedTilePos, position, movedUnit);
        gameJournal.addAndExecute(move);
    }

    public void attack(Unit attacker, Unit defender) {
        Position defenderPosition = defender.getPosition();
        Position attackerPosition = attacker.getPosition();

        Terrain defenderTerrain = map[defenderPosition.getY()][defenderPosition.getX()].getTerrain();
        Integer attackDamage = CombatService.calculateDamage(attacker, defender, defenderTerrain);

        defender.takeDamage(attackDamage);
        attacker.setHasAttacked(true);
        attacker.setHasMoved(true);

        if (defender.getHealth() > 0) {
            if (getAttackReach(defenderPosition, defender.minAttackRange(), defender.maxAttackRange()).contains(attackerPosition)) {
                System.out.println("CounterAttacking!!");
                Terrain attackerTerrain = map[attackerPosition.getY()][attackerPosition.getX()].getTerrain();
                Integer counterAttackDamage = CombatService.calculateDamage(defender, attacker, attackerTerrain);

                attacker.takeDamage(counterAttackDamage);

                if (attacker.getHealth() < 0) {
                    map[attackerPosition.getY()][attackerPosition.getX()].setUnit(null);
                    activePlayer.deleteUnit(attacker);
                }
            }
        } else {
            map[defenderPosition.getY()][defenderPosition.getX()].setUnit(null);
            if ((defender.getOwnedBy() == Players.RED)) {
                redPlayer.deleteUnit(defender);
            } else {
                bluePlayer.deleteUnit(defender);
            }
        }

        notifyObservers(new AttackEvent(attackerPosition, defenderPosition, attacker.getHealth(), defender.getHealth()));
    }

    public List<Position> getAttackReach(Position from, Integer minRange, Integer maxRange) {
        Unit attackingUnit = getTile(from).getUnit();
        List<Position> targets = new ArrayList<>();

        for (int r = -maxRange; r <= maxRange; r++) {
            int maxC = maxRange - Math.abs(r);

            for (int c = -maxC; c <= maxC; c++) {
                int currentDistance = Math.abs(r) + Math.abs(c);

                if (currentDistance >= minRange && currentDistance <= maxRange) {
                    int targetX = from.getX() + c;
                    int targetY = from.getY() + r;

                    if (targetX >= 0 && targetX < map[0].length && targetY >= 0 && targetY < map.length) {
                        Unit targetUnit = map[targetY][targetX].getUnit();
                        if (targetUnit != null && targetUnit.getOwnedBy() != attackingUnit.getOwnedBy()) {
                            targets.add(new Position(targetX, targetY));
                        }
                    }
                }
            }
        }
        return targets;
    }

    public void handleEndTurn() {
        gameJournal.addAndExecute(new EndTurnAction(this, activePlayer.getFunds(), activePlayer));
    }

    public void endTurn(EndTurnAction endTurnAction) {
        this.selectedTilePos = null;
        this.currentReachable.clear();
        activePlayer.endTurn();
        System.out.println("ActivePlayer = " + activePlayer.getSide() + " player funds: " + activePlayer.getFunds());

        activePlayer = (activePlayer == bluePlayer) ? redPlayer : bluePlayer;
        activePlayer.startTurn();
        healUnits(activePlayer, endTurnAction);

        notifyObservers(new FundsUpdateEvent(activePlayer.getFunds()));
        notifyObservers(new TurnChangeEvent(activePlayer.getSide(), activePlayer.getFunds()));
    }

    public void healUnits(Player player, EndTurnAction endTurnAction) {
        System.out.println("Healing units for player: " + activePlayer.getSide());
        System.out.println("PlayerUnitCount = " + activePlayer.getUnits().size());
        for (Unit u : activePlayer.getUnits()) {
            Integer unitHealth = u.getHealth();
            Position unitPosition = u.getPosition();
            int healingCost = u.cost()/10;
            Terrain unitTerrain = map[unitPosition.getY()][unitPosition.getX()].getTerrain();

            if (unitTerrain instanceof Capturable capturable) {
                System.out.println(capturable.getOwner() + "==" + u.getOwnedBy());
                if (unitHealth < 100 && capturable.heals() && player.getFunds() >= healingCost && capturable.getOwner() == u.getOwnedBy()) {
                    player.spendFunds(healingCost);
                    if (100 - unitHealth > 20) {
                        u.setHealth(unitHealth + 20);
                    } else {
                        u.setHealth(100 - unitHealth);
                    }
                }
            }

            endTurnAction.addHealedUnit(u, unitHealth);

            notifyObservers(new UpdateHealthEvent(unitPosition, u.getHealth()));
        }
    }

    public Player getPlayer(Players playerSide) {
        return (redPlayer.getSide() == playerSide) ? redPlayer : bluePlayer;
    }

    public void transferControl(Player receiver) {
        activePlayer = receiver;
        notifyObservers(new FundsUpdateEvent(activePlayer.getFunds()));
        notifyObservers(new TurnChangeEvent(activePlayer.getSide(), activePlayer.getFunds()));
    }

    public void buildUnit(Position position, UnitTypes unitType) {
        Unit createdUnit = createUnit(unitType.toString(), activePlayer.getSide().toString(), position.getX(), position.getY());
        createdUnit.setHasAttacked(true);
        createdUnit.setHasMoved(true);
        notifyObservers(new BuildEvent(position, createdUnit));
    }

    public void handleBuild(Position position, UnitTypes unitType) {
        int unitCost = UnitRegistry.getCost(unitType);
        if (unitCost <= activePlayer.getFunds()) {
            BuildAction build = new BuildAction(this, unitCost, position, unitType);
            gameJournal.addAndExecute(build);
        }
    }

    public void removeUnit(Position position) {
        Unit unit = map[position.getY()][position.getX()].getUnit();
        map[position.getY()][position.getX()].setUnit(null);
        activePlayer.deleteUnit(unit);
        notifyObservers(new DeleteUnitEvent(position));
    }

    public void addPlayerFunds(Integer amount) {
        activePlayer.addFunds(amount);
        notifyObservers(new FundsUpdateEvent(activePlayer.getFunds()));
    }

    public void spendPlayerFunds(Integer amount) {
        activePlayer.spendFunds(amount);
        notifyObservers(new FundsUpdateEvent(activePlayer.getFunds()));
    }

    public void restoreUnit(Unit unit, Position position) {
        map[position.getY()][position.getX()].setUnit(unit);
        notifyObservers(new BuildEvent(position, unit));
    }

    public void undo() {
        gameJournal.undoLast();
    }

    public void redo() {
        gameJournal.redoLast();
    }

    public Player getActivePlayer() { return activePlayer; }

    public void configurePlayerRoles(boolean redAI, boolean blueAI) {
        this.redAI = redAI;
        this.blueAI = blueAI;
    }

    public boolean isCurrentPlayerAI() {
        if (getActivePlayer().getSide() == Players.RED) return redAI;
        if (getActivePlayer().getSide() == Players.BLUE) return blueAI;
        return false;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public boolean getRedAI() {
        return redAI;
    }
    public boolean getBlueAI() {
        return blueAI;
    }
    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }
}
