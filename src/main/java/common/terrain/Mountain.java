package common.terrain;

public class Mountain extends Terrain{

    public static final String code = "M";

    @Override
    public Integer getMovementCost(Boolean isWheeled) {
        return isWheeled ? null : 2;
    }

    @Override
    public Integer getDefenceBonus() {
        return 4;
    }

    @Override
    public String getSprite() {
        return "resources/sprites/terrain/mountain.png";
    }
}
