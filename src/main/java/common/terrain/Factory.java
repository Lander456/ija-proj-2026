package common.terrain;

public class Factory extends Capturable{

    public static final String code = "FA";

    @Override
    public Integer getMovementCost(Boolean isWheeled) {
        return 1;
    }

    @Override
    public Integer getDefenceBonus() {
        return 3;
    }

    @Override
    public String getTerrainType() {
        return "factory";
    }

    @Override
    public Boolean spawnsUnits() {
        return true;
    }

    public Factory(String owner) {
        super(owner);
    }
}
