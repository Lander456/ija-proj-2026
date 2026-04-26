package common.terrain;

public class City extends Capturable{

    public static final String code = "C";

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
        return "city";
    }

    @Override
    public Integer getIncome() {
        return 1000;
    }

    @Override
    public Boolean heals() {
        return true;
    }

    public City(String owner) {
        super(owner);
    }
}
