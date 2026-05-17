package common.terrain;

/**
 * Class defining behaviour for all plains tiles.
 * @author Tadeas Topinka (xtopint00)
 * @see Terrain
 */
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
