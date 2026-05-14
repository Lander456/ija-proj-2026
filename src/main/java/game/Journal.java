package game;

import common.gameActions.GameAction;

import java.util.ArrayDeque;
import java.util.Deque;

public class Journal {
    private final Deque<GameAction> history = new ArrayDeque<>();
    private final Deque<GameAction> redoStack = new ArrayDeque<>();

    public void addAndExecute(GameAction action) {
        action.execute();
        history.push(action);
        redoStack.clear();
    }

    public void undoLast() {
        if (!history.isEmpty()) {
            GameAction action = history.pop();
            action.undo();
            redoStack.push(action);
        }
    }

    public void redoLast() {
        if (!redoStack.isEmpty()) {
            GameAction action = redoStack.pop();
            action.execute();
            history.push(action);
        }
    }
}
