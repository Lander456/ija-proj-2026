package tool.gameObserver;

import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.*;
import view.GameView;

import javax.swing.*;

public class GameObserver implements tool.GameObserver {
    private final GameView view;

    public GameObserver(GameView view) {
        this.view = view;
    }

    @Override
    public void update(GameEvent event) {
        switch (event) {
            case AttackEvent e -> {
                break;
            }

            case BuildEvent e -> {
                break;
            }

            case CaptureEvent e -> {
                break;
            }

            case MoveEvent e -> {
                view.clearHighlights();
                view.moveUnitSprite(e.from(), e.to());
            }

            case SelectEvent e -> {
                view.clearHighlights();
                if (e.getIsActive()) {
                    view.highlightReachableTiles(e.getReachable());
                }
            }

            case ActionMenuEvent e -> {
                view.showActionMenu(e.getPosition());
            }

            case null, default ->  {
                break;
            }
        }
    }
}
