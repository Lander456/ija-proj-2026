package common.terrain;

public abstract class TerrainFactory {
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
