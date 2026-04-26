package common.terrain;

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

    public HQ(String owner) {
        super(owner);
    }
}
