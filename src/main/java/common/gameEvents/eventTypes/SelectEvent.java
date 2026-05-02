package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

import java.util.List;

public final class SelectEvent implements GameEvent {
    private final Position position;
    private final List<Position> reachable;
    private final boolean isActive;

    public SelectEvent(final Position position, final List<Position> reachable, final boolean active) {
        this.position = position;
        this.reachable = reachable;
        this.isActive = active;
    }

    public Position getPosition() {
        return position;
    }
    public List<Position> getReachable() {
        return reachable;
    }
    public boolean getIsActive() {
        return isActive;
    }
}
