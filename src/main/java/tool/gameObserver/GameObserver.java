package tool.gameObserver;

import common.gameEvents.GameEvent;

/**
 * The GameObserver interface defines a single method used to send a notification to the observer
 * @author Tadeas Topinka (xtopint00)
 */
public interface GameObserver {
    void update(GameEvent event);
}
