package common.terrain;

/**
 * Abstract class defining the base behaviour for all terrain.
 * @author Tadeas Topinka (xtopint00)
 */
public abstract class Terrain {
    public abstract Integer getMovementCost(Boolean isWheeled);
    public abstract Integer getDefenceBonus();
    public abstract String getTerrainType();
    public abstract String getTerrainCode();

    public Boolean heals() { return false; }

    public Boolean spawnsUnits() { return false; }

    public Integer getIncome() { return 0; }
}
