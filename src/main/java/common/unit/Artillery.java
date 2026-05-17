package common.unit;

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
    public Integer minAttackRange() {
        return 2;
    }

    @Override
    public Integer maxAttackRange() {
        return 3;
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
    public void setHasMoved(Boolean hasMoved) {
        super.setHasMoved(hasMoved);
        super.setHasAttacked(hasMoved);
    }

    protected Artillery(String owningPlayer, Integer startX, Integer startY) {
        super(Players.fromString(owningPlayer), startX, startY);
    }
}
