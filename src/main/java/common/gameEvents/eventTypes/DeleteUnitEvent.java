package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

/**
 * Defines a DeleteUnit event used to communicate to the observers that a unit has been deleted (destroyed) and should
 * be entirely removed from the map.
 * @param position Position of the unit that was destroyed.
 * @author Tadeas Topinka (xtopint00)
 */
public record DeleteUnitEvent (Position position) implements GameEvent {
}
