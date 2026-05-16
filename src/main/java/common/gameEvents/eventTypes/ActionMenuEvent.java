package common.gameEvents.eventTypes;

import common.Position;
import common.enums.Actions;
import common.gameEvents.GameEvent;

import java.util.List;

public record ActionMenuEvent(Position position,
                              List<Actions> actions) implements GameEvent {
}
