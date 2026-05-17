package common.unit;

import common.enums.MovementTypes;
import common.enums.Players;
import common.enums.UnitTypes;

/**
 * Defines the behaviour for all the tank units in the game.
 * @author Tadeas Topinka (xtopint00)
 * @see Unit
 */
public class Tank extends Unit {
    public static final UnitTypes unitType = UnitTypes.Tank;

    @Override
    public Integer minAttackRange() {
        return 1;
    }

    @Override
    public Integer maxAttackRange() {
        return 1;
    }

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
    public UnitTypes getUnitType() {
        return unitType;
    }

    protected Tank(String owningPlayer, Integer startX, Integer startY) {
        super(Players.fromString(owningPlayer), startX, startY);
    }
}
