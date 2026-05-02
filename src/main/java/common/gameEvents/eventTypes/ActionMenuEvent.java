package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

public final class ActionMenuEvent implements GameEvent {
    private final Position position;

    public  ActionMenuEvent(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
