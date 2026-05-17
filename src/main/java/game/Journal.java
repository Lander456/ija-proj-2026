package game;

import common.gameActions.GameAction;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Defines a class used to keep the journal of all actions performed in the game.
 * @author Tadeas Topinka (xtopint00)
 */
public class Journal {
    private final Deque<GameAction> history = new ArrayDeque<>();
    private final Deque<GameAction> redoStack = new ArrayDeque<>();

    /**
     * Adds an action into the journal and immediately  executes it.
     * @param action Action to be added to the journal and executed.
     * @author Tadeas Topinka (xtopint00)
     */
    public void addAndExecute(GameAction action) {
        action.execute();
        history.push(action);
        redoStack.clear();
    }

    /**
     * Undos the last performed action and adds it to the list of actions undone.
     * @author Tadeas Topinka (xtopint00)
     */
    public void undoLast() {
        if (!history.isEmpty()) {
            GameAction action = history.pop();
            action.undo();
            redoStack.push(action);
        }
    }

    /**
     * Redos the last undone action.
     * @author Tadeas Topinka (xtopint00)
     */
    public void redoLast() {
        if (!redoStack.isEmpty()) {
            GameAction action = redoStack.pop();
            action.execute();
            history.push(action);
        }
    }

    public Deque<GameAction> getHistory() {
        return history;
    }
}
