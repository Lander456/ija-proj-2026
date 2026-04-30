package common.terrain;

public class Plain extends Terrain{

    public static final String code = "P";

    @Override
    public Integer getMovementCost(Boolean isWheeled) {
        return 1;
    }

    @Override
    public Integer getDefenceBonus() {
        return 1;
    }

    @Override
    public String getTerrainType() {
        return "plain";
    }

    @Override
    public String getTerrainCode() {
        return code;
    }
}
