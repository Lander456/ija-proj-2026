package common.unit;

import common.enums.MovementTypes;
import common.enums.Players;
import common.enums.UnitTypes;

/**
 * Defines the behaviour for all the infantry units in the game.
 * @author Tadeas Topinka (xtopint00)
 * @see Unit
 */
public class Infantry extends Unit {
    public static final UnitTypes unitType = UnitTypes.Infantry;

    @Override
    public Integer movementRange() {
        return 3;
    }

    @Override
    public MovementTypes movementType() {
        return MovementTypes.FEET;
    }

    @Override
    public Integer minAttackRange() {
        return 1;
    }

    @Override
    public Integer maxAttackRange() {
        return 1;
    }

    @Override
    public Integer cost() {
        return 1000;
    }

    @Override
    public UnitTypes getUnitType() {
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
