package common.terrain;

/**
 * Abstract factory used to instantiate any terrain based on its terrainCode and owner.
 * @author Tadeas Topinka (xtopint00)
 */
public abstract class TerrainFactory {
    /**
     * Creates a new Terrain instance based on its code and owner.
     * @param terrainCode Terrain code found in the map definition.
     * @param owner Default owner of the tile with the terrain, if the terrain allows for an owner.
     * @return Newly initialised terrain.
     * @author Tadeas Topinka (xtopint00)
     */
    public static Terrain create(String terrainCode, String owner) {
        return switch(terrainCode.toUpperCase()) {
            case Plain.code -> new Plain();
            case City.code -> new City(owner);
            case Factory.code -> new Factory(owner);
            case Forest.code -> new Forest();
            case HQ.code -> new HQ(owner);
            case Mountain.code -> new Mountain();
            case Water.code -> new Water();
            default -> throw new IllegalArgumentException("Unknown terrain type: " + terrainCode);
        };
    }
}
