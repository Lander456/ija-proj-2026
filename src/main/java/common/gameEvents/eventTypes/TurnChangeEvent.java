package common.gameEvents.eventTypes;

import common.enums.Players;
import common.gameEvents.GameEvent;

/**
 * Defines an event used to communicate to the observers that a turn change had occured and that they should notify the
 * players via the GUI and potentially stop the AI from taking further actions.
 * @param side Player that just received control, used to display a player in the GUI.
 * @param finances Finances of the newly active player.
 * @author Tadeas Topinka (xtopint00)
 */
public record TurnChangeEvent (Players side,
                               int finances) implements GameEvent {
}
