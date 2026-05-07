package common.gameEvents.eventTypes;

import common.gameEvents.GameEvent;

public record FundsUpdateEvent(int amount) implements GameEvent {
}
