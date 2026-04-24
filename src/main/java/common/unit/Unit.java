package common.unit;

import common.Position;
import common.enums.AttackTypes;
import common.enums.MovementTypes;
import common.enums.Players;

public abstract class Unit {
    private Integer health = 100;
    private Boolean hasMoved = true;
    private Boolean hasAttacked = true;
    private Position position;
    private final Players ownedBy;

    public abstract Integer movementRange();

    public abstract MovementTypes movementType();

    public abstract Integer cost();

    public abstract String getUnitType();

    public Boolean canCapture() { return false; }

    public AttackTypes attackType() { return AttackTypes.CLOSE_RANGE; }

    public Integer getHealth() { return health; }
    public void setHealth(Integer health) { this.health = health; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public Players getOwnedBy() { return ownedBy; }

    public Boolean getHasMoved() { return hasMoved; }
    public void setHasAttacked(Boolean hasAttacked) { this.hasAttacked = hasAttacked; }

    public Boolean getHasAttacked() { return hasAttacked; }
    public void setHasMoved(Boolean hasMoved) { this.hasMoved = hasMoved; }

    protected Unit(Players owningPlayer, Integer startX, Integer startY) {
        this.position = new Position(startX, startY);
        this.ownedBy = owningPlayer;
    }

    @Override
    public String toString() {
        return String.format("{%s[%d,%d][%d]}", this.getUnitType(), this.getPosition().getX(), this.getPosition().getY(), this.getHealth());
    }
}
