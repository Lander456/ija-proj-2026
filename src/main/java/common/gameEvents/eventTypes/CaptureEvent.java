package common.gameEvents.eventTypes;

import common.Position;
import common.enums.Players;
import common.gameEvents.GameEvent;

public record CaptureEvent(Position capturableTile,
                           Players capturer) implements GameEvent {
}
