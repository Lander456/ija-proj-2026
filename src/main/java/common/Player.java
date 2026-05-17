package common;

import common.enums.Players;
import common.terrain.Capturable;
import common.unit.Unit;

import java.util.List;

/**
 * Defines a class conatining all the data needed about a player in the game, their units, funds, properties, etc.
 * @author Tadeas Topinka (xtopint00)
 */
public class Player {
    private final Players side;
    private int funds;
    private final List<Capturable> properties;
    private final List<Unit> units;

    /**
     * Constructor for the Player class.
     * @param side Player's side.
     * @param startingFunds Starting funds of the player.
     * @param startingUnits Starting units of the player.
     * @param startingProperties Starting properties of the player.
     * @author Tadeas Topinka (xtopint00)
     */
    public Player(Players side, int startingFunds, List<Unit> startingUnits, List<Capturable> startingProperties) {
        this.side = side;
        this.funds = startingFunds;
        this.units = startingUnits;
        this.properties = startingProperties;
    }

    /**
     * Method used to end turn for the player, triggering their income to be calculated and added to their funds.
     * @author Tadeas Topinka (xtopint00)
     */
    public void endTurn() {
        addFunds(this.properties.stream().mapToInt(Capturable::getIncome).sum());
    }

    /**
     * Method used to start a player's turn, resetting all their units to having not moved and not attacked.
     * @author Tadeas Topinka (xtopint00)
     */
    public void startTurn() {
        for (Unit u : this.units) {
            u.setHasAttacked(false);
            u.setHasMoved(false);
        }
    }

    public Players getSide() { return side; }
    public int getFunds() { return funds; }

    public void addProperty(Capturable property) {
        properties.add(property);
    }
    public void removeProperty(Capturable property) {
        properties.remove(property);
    }
    public void addUnit(Unit unit) {
        units.add(unit);
    }
    public void deleteUnit(Unit unit) {
        units.remove(unit);
    }
    public void spendFunds(int amount) {
        this.funds -= amount;
    }
    public void addFunds(int amount) { this.funds += amount; }
    public void setFunds(int amount) { this.funds = amount; }
    public List<Unit> getUnits() { return units; }
    public List<Capturable> getProperties() { return properties; }
}
