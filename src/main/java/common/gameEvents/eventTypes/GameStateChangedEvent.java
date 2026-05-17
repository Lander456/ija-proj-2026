package common.gameEvents.eventTypes;

import common.enums.GameState;
import common.gameEvents.GameEvent;

/**
 * Defines an event used to communicate to the observers that a new game state has been set (either PLAY or PAUSE) and
 * thus they should react by freezing the AI and allowing player interaction and rewinding.
 * @param gameState GameState that has newly been set.
 * @param playerAI Boolean indicating whether the active player is an AI or not.
 * @author Tadeas Topinka (xtopint00)
 */
public record GameStateChangedEvent(GameState gameState,
                                    boolean playerAI) implements GameEvent {
}
