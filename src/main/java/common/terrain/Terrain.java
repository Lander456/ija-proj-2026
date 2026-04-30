package common.terrain;

import common.enums.Players;

public abstract class Terrain {
    public abstract Integer getMovementCost(Boolean isWheeled);
    public abstract Integer getDefenceBonus();
    public abstract String getTerrainType();
    public abstract String getTerrainCode();

    public Boolean isCapturable() { return false; }

    public Boolean heals() { return false; }

    public Boolean spawnsUnits() { return false; }

    public Integer getIncome() { return 0; }
}
