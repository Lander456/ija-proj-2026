package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;
import common.unit.Unit;

public record MoveEvent(Position from,
                        Position to,
                        Unit unit) implements GameEvent {
}
