package common.terrain;

/**
 * Class defining behaviour for all mountain tiles.
 * @author Tadeas Topinka
 * @see Terrain
 */
public class Mountain extends Terrain{

    public static final String code = "M";

    @Override
    public Integer getMovementCost(Boolean isWheeled) {
        return isWheeled ? null : 2;
    }

    @Override
    public Integer getDefenceBonus() {
        return 4;
    }

    @Override
    public String getTerrainType() {
        return "mountain";
    }

    @Override
    public String getTerrainCode() {
        return code;
    }
}
