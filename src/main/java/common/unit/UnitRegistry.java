package common.unit;

import common.enums.UnitTypes;

import java.util.EnumMap;
import java.util.Map;

/**
 * Defines a registry containing the data about all units.
 * @author Tadeas Topinka (xtopint00)
 */
public class UnitRegistry {
    private static final Map<UnitTypes, Unit> unitTemplates = new EnumMap<>(UnitTypes.class);

    static {
        unitTemplates.put(UnitTypes.Infantry, new Infantry("neutral", 0, 0));
        unitTemplates.put(UnitTypes.Tank, new Tank("neutral", 0, 0));
        unitTemplates.put(UnitTypes.Artillery, new Artillery("neutral", 0, 0));
    }

    /**
     * Gets the cost of a unit.
     * @param type Type of unit to get the cost for.
     * @return The integer cost of the unit type in question.
     * @author Tadeas Topinka (xtopint00)
     */
    public static int getCost(UnitTypes type) {
        Unit u = unitTemplates.get(type);
        return (u != null) ? u.cost() : 0;
    }
}
