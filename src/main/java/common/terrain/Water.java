package common.terrain;

public class Water extends Terrain{

    public static final String code = "W";

    @Override
    public Integer getMovementCost(Boolean isWheeled) {
        return null;
    }

    @Override
    public Integer getDefenceBonus() {
        return null;
    }

    @Override
    public String getTerrainType() {
        return "water";
    }
}
