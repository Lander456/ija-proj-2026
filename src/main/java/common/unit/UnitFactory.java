package common.unit;

/**
 * Defines an abstract factory used to instantiate new units on demand.
 * @author Tadeas Topinka (xtopint00)
 */
public abstract class UnitFactory {
    private static final String infantryString = "Infantry";
    private static final String tankString = "Tank";
    private static final String artilleryString = "Artillery";

    /**
     * Instantiates a new unit for a certain player.
     * @param unitName Name of the unit to be instantiated.
     * @param owningPlayer The unit's owner.
     * @param startX The starting X coordinate of the unit.
     * @param startY The startin Y coordinate of the unit.
     * @return The newly instantiated unit.
     * @author Tadeas Topinka (xtopint00)
     */
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
