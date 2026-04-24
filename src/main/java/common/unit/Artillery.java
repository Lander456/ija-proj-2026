package common.unit;

import common.enums.AttackTypes;
import common.enums.MovementTypes;

public class Artillery extends Unit {
    public static final String unitType = "Artillery";

    @Override
    public Integer movementRange() {
        return 5;
    }

    @Override
    public MovementTypes movementType() {
        return MovementTypes.WHEELS;
    }

    @Override
    public Integer cost() {
        return 6000;
    }

    @Override
    public String getUnitType() {
        return unitType;
    }

    @Override
    public AttackTypes attackType() { return AttackTypes.LONG_RANGE; }

    protected Artillery(String owningPlayer, Integer startX, Integer startY) {
        super(owningPlayer, startX, startY);
    }
}
