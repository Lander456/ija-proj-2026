package common.terrain;

import common.enums.Players;

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

    @Override
    public String getSprite() {
        switch (this.getOwner()) {
            case RED -> {
                return "resources/sprites/buildings/cityRed.png";
            }
            case BLUE -> {
                return "resources/sprites/buildings/cityBlue.png";
            }
            case NEUTRAL -> {
                return "resources/sprites/buildings/cityUncaptured.png";
            }
            case null, default -> {
                return null;
            }
        }
    }
}
