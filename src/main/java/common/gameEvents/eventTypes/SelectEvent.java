package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

import java.util.List;

/**
 * Defines an event used to communicate to the observers that a player has selected a tile and thus all reachable tiles
 * should be overlain with a blue colour.
 * @param reachable Reachable tiles that should be overlaid with a blue colour, distinguishing them from unreachable.
 * @param isActive Boolean indicating whether the given unit is active.
 * @author Tadeas Topinka
 */
public record SelectEvent(List<Position> reachable,
                          boolean isActive) implements GameEvent {
}
