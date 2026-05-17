package common.terrain;

import common.enums.Players;
import common.unit.Unit;

/**
 * Abstract class defining base behaviour for all capturable tiles.
 * @author Tadeas Topinka (xtopint00)
 * @see Terrain
 */
public abstract class Capturable extends Terrain{
    private Players owner = Players.NEUTRAL;

    private Integer resistance = 20;

    public Integer getResistance() { return resistance; }
    public void setResistance(Integer resistance) { this.resistance = resistance; }

    public Players getOwner() { return owner; }
    public void setOwner(Players owner) { this.owner = owner; }

    /**
     * Performs the capture action on the capturable tile.
     * @param capturingUnit Unit that is performing the capturing action.
     * @author Tadeas Topinka (xtopint00)
     */
    public void capture(Unit capturingUnit) {
        resistance = resistance - capturingUnit.getHealth() / 10;

        if (resistance <= 0) {
            this.setOwner(capturingUnit.getOwnedBy());
            this.setResistance(20);
        }
    }

    public Capturable(String owner) {
        this.owner = Players.fromString(owner);
    }
}
