package common.gameEvents.eventTypes;

import common.enums.Players;
import common.gameEvents.GameEvent;

public record TurnChangeEvent (Players side,
                               int finances) implements GameEvent {
}
