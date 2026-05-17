package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

/**
 * Defines an event used to communicate to the observers that a unit's health has changed and thus should be updated in
 * the GUI as well.
 * @param position Position where the affected unit is on the map.
 * @param unitHealth Newly updated health of the unit.
 * @author Tadeas Topinka (xtopint00)
 */
public record UpdateHealthEvent(Position position,
                                Integer unitHealth) implements GameEvent {
}
