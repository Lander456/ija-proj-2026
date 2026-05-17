package common.terrain;

/**
 * Class defining base behaviour for all the water in the game.
 * @author Tadeas Topinka (xtopint00)
 * @see Terrain
 */
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

    @Override
    public String getTerrainCode() {
        return code;
    }
}
