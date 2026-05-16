package common.unit;

public abstract class UnitFactory {
    private static final String infantryString = "Infantry";
    private static final String tankString = "Tank";
    private static final String artilleryString = "Artillery";

    public static Unit create(String unitName, String owningPlayer, Integer startX, Integer startY) {
        System.out.println("Creating unit for " + owningPlayer);
        return switch (unitName) {
            case artilleryString -> new Artillery(owningPlayer, startX, startY);
            case infantryString -> new Infantry(owningPlayer, startX, startY);
            case tankString -> new Tank(owningPlayer, startX, startY);
            default -> throw new IllegalArgumentException("Unknown unit type: " + unitName);
        };
    }
}
