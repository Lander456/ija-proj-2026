package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

public record MoveEvent(Position from,
                        Position to) implements GameEvent {
}
