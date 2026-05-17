package common.terrain;

/**
 * Class defining the behaviour for all HQ tiles.
 * @author Tadeas Topinka
 * @see Capturable
 */
public class HQ extends Capturable{

    public static final String code = "H";

    @Override
    public Integer getMovementCost(Boolean isWheeled) {
        return 1;
    }

    @Override
    public Integer getDefenceBonus() {
        return 4;
    }

    @Override
    public String getTerrainType() {
        return "hq";
    }

    @Override
    public String getTerrainCode() {
        return code;
    }

    public HQ(String owner) {
        super(owner);
    }
}
