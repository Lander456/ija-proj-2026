package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;
import common.unit.Unit;

public record CaptureEvent(Position capturableTile,
                           Unit capturer) implements GameEvent {
}
