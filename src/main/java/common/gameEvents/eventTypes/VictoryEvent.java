package common.gameEvents.eventTypes;

import common.enums.Players;
import common.gameEvents.GameEvent;

/**
 * Defines an event used to communicate to the observers that a certain player has won the game and thus AI should be
 * stopped and a victory screen be displayed.
 * @param player Player who won, used to show their side as the victor on the victory screen.
 * @author Tadeas Topinka (xtopint00)
 */
public record VictoryEvent(Players player) implements GameEvent {
}
