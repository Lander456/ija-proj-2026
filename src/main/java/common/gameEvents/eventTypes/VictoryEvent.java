package common.gameEvents.eventTypes;

import common.enums.Players;
import common.gameEvents.GameEvent;

public record VictoryEvent(Players player) implements GameEvent {
}
