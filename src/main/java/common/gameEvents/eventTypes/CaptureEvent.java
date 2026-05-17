package common.gameEvents.eventTypes;

import common.Position;
import common.enums.Players;
import common.gameEvents.GameEvent;

/**
 * Defines a Capture event used to communicate to the observers that a capturing action has occurred and its results
 * should be displayed to the player.
 * @param capturableTile Tile that has undergone a capture action.
 * @param capturer Unit that performed the capturing.
 * @author Tadeas Topinka (xtopint00)
 */
public record CaptureEvent(Position capturableTile,
                           Players capturer) implements GameEvent {
}
