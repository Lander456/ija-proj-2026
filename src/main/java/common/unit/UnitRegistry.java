package common.unit;

import common.enums.UnitTypes;

import java.util.EnumMap;
import java.util.Map;

public class UnitRegistry {
    private static final Map<UnitTypes, Unit> unitTemplates = new EnumMap<>(UnitTypes.class);

    static {
        unitTemplates.put(UnitTypes.Infantry, new Infantry("neutral", 0, 0));
        unitTemplates.put(UnitTypes.Tank, new Tank("neutral", 0, 0));
        unitTemplates.put(UnitTypes.Artillery, new Artillery("neutral", 0, 0));
    }

    public static int getCost(UnitTypes type) {
        Unit u = unitTemplates.get(type);
        return (u != null) ? u.cost() : 0;
    }
}
