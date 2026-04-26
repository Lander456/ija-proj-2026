package common.unit;

import common.enums.MovementTypes;
import common.enums.Players;

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

    protected Tank(Players owningPlayer, Integer startX, Integer startY) {
        super(owningPlayer, startX, startY);
    }
}
