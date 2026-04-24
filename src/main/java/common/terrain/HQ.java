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

    public HQ(String owner) {
        super(owner);
    }

    @Override
    public String getSprite() {
        switch(this.getOwner()) {
            case RED -> {
                return "resources/sprites/buildings/hqRed.png";
            }
            case BLUE -> {
                return "resources/sprites/buildings/hqBlue.png";
            }
            case null, default -> {
                return null;
            }
        }
    }
}
