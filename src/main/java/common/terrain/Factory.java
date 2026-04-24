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
    public Boolean spawnsUnits() {
        return true;
    }

    public Factory(String owner) {
        super(owner);
    }

    @Override
    public String getSprite() {
        switch(this.getOwner()) {
            case RED -> {
                return "resources/sprites/buildings/factoryRed.png";
            }
            case BLUE -> {
                return "resources/sprites/buildings/factoryBlue.png";
            }
            case NEUTRAL -> {
                return "resources/sprites/buildings/factoryUncaptured.png";
            }
            case null, default -> {
                return null;
            }
        }
    }
}
