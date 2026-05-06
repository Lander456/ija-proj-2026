package common.unit;

import common.enums.AttackTypes;
import common.enums.MovementTypes;
import common.enums.Players;
import common.enums.UnitTypes;

public class Artillery extends Unit {
    public static final UnitTypes unitType = UnitTypes.Artillery;

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
    public UnitTypes getUnitType() {
        return unitType;
    }

    @Override
    public AttackTypes attackType() { return AttackTypes.LONG_RANGE; }

    protected Artillery(String owningPlayer, Integer startX, Integer startY) {
        super(Players.fromString(owningPlayer), startX, startY);
    }
}
