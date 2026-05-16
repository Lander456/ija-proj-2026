package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

import java.util.List;

public record SelectEvent(List<Position> reachable,
                          boolean isActive) implements GameEvent {
}
