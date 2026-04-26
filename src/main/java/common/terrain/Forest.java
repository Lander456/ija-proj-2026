package common.terrain;

public class Forest extends Terrain{
    public static final String code = "F";

    @Override
    public Integer getMovementCost(Boolean isWheeled) { return isWheeled ? 2 : 1; }

    @Override
    public Integer getDefenceBonus() {
        return 2;
    }

    @Override
    public String getTerrainType() {
        return "forest";
    }
}
