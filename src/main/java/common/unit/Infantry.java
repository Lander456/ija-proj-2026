package common.unit;

import common.enums.MovementTypes;
import common.enums.Players;

public class Infantry extends Unit {
    public static final String unitType = "Infantry";

    @Override
    public Integer movementRange() {
        return 3;
    }

    @Override
    public MovementTypes movementType() {
        return MovementTypes.FEET;
    }

    @Override
    public Integer cost() {
        return 1000;
    }

    @Override
    public String getUnitType() {
        return unitType;
    }

    @Override
    public Boolean canCapture() {
        return true;
    }

    protected Infantry(String owningPlayer, Integer startX, Integer startY) {
        super(Players.fromString(owningPlayer), startX, startY);
    }
}
