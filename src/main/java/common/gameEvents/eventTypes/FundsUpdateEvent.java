package common.gameEvents.eventTypes;

import common.gameEvents.GameEvent;

/**
 * Defines an event used to communicate to the observers that a player's funds had undergone a change and should be
 * rerendered with a new value.
 * @param amount The current funds available to the player.
 * @author Tadeas Topinka (xtopint00)
 */
public record FundsUpdateEvent(int amount) implements GameEvent {
}
