package common;

import common.enums.Players;
import common.terrain.Capturable;
import common.unit.Unit;

import java.util.List;

public class Player {
    private final Players side;
    private int funds;
    private final List<Capturable> properties;
    private final List<Unit> units;

    public Player(Players side, int startingFunds, List<Unit> startingUnits, List<Capturable> startingProperties) {
        this.side = side;
        this.funds = startingFunds;
        this.units = startingUnits;
        this.properties = startingProperties;
    }

    public void endTurn() {
        addFunds(this.properties.stream().mapToInt(Capturable::getIncome).sum());
    }

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
