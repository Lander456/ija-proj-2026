package common.gameEvents.eventTypes;

import common.enums.GameState;
import common.gameEvents.GameEvent;

public record GameStateChangedEvent(GameState gameState,
                                    boolean playerAI) implements GameEvent {
}
