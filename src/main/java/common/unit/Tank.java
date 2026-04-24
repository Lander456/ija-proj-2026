package common.unit;

import common.enums.MovementTypes;

public class Tank extends Unit {
    public static final String unitType = "Tank";

    @Override
    public Integer movementRange() {
        return 6;
    }

    @Override
    public MovementTypes movementType() {
        return MovementTypes.WHEELS;
    }

    @Override
    public Integer cost() {
        return 7000;
    }

    @Override
    public String getUnitType() {
        return unitType;
    }

    protected Tank(String owningPlayer, Integer startX, Integer startY) {
        super(owningPlayer, startX, startY);
    }
}
