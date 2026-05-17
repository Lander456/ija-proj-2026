package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

/**
 * Defines a move event used to communicate to the observers that a unit has moved and should be rerendered on a
 * different tile.
 * @param from The position the unit moved from.
 * @param to The position the unit moved to.
 * @author Tadeas Topinka (xtopint00)
 */
public record MoveEvent(Position from,
                        Position to) implements GameEvent {
}
