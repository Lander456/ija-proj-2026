package common.gameEvents.eventTypes;

import common.Position;
import common.enums.Actions;
import common.gameEvents.GameEvent;

import java.util.List;

/**
 * Defines an ActionMenu event used to communicate to the observer that an action menu should be shown to the user.
 * @param position Position where the action menu should appear.
 * @param actions Actions available through the action menu.
 * @author Tadeas Topinka (xtopint00)
 */
public record ActionMenuEvent(Position position,
                              List<Actions> actions) implements GameEvent {
}
