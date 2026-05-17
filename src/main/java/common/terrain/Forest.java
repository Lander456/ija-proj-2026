package common.terrain;

/**
 * Class defining the behaviour for all forest tiles.
 * @author Tadeas Topinka (xtopint00)
 * @see Terrain
 */
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

    @Override
    public String getTerrainCode() {
        return code;
    }
}
