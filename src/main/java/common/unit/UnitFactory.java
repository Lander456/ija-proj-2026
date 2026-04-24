package common.unit;

public abstract class UnitFactory {
    public static Unit create(String unitName, String owningPlayer, Integer startX, Integer startY) {
        return switch (unitName) {
            case Artillery.unitType -> new Artillery(owningPlayer, startX, startY);
            case Infantry.unitType -> new Infantry(owningPlayer, startX, startY);
            case Tank.unitType -> new Tank(owningPlayer, startX, startY);
            default -> throw new IllegalArgumentException("Unknown unit type: " + unitName);
        };
    }
}
