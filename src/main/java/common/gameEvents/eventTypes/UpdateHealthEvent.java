package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

public record UpdateHealthEvent(Position position,
                                Integer unitHealth) implements GameEvent {
}
