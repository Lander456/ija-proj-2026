package common.gameEvents.eventTypes;

import common.Position;
import common.enums.Actions;
import common.gameEvents.GameEvent;

import java.util.List;

public final class ActionMenuEvent implements GameEvent {
    private final Position position;
    private final List<Actions> actions;

    public  ActionMenuEvent(Position position, List<Actions> actions) {
        this.position = position;
        this.actions = actions;
    }

    public Position getPosition() {
        return position;
    }
}
