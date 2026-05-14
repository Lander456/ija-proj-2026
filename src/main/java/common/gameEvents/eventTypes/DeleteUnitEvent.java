package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

public record DeleteUnitEvent (Position position) implements GameEvent {
}
