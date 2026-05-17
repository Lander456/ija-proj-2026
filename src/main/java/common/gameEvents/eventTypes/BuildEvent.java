package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;
import common.unit.Unit;

/**
 * Defines a Build event used to transmit to the observers that a unit has been built and should be shown to the player.
 * @param position Position where the unit was built.
 * @param unit Unit built.
 * @author Tadeas Topinka (xtopint00)
 */
public record BuildEvent(Position position,
                         Unit unit) implements GameEvent {
}
