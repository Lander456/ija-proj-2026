package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;
import common.unit.Unit;

public record BuildEvent(Position position,
                         Unit unit) implements GameEvent {
}
