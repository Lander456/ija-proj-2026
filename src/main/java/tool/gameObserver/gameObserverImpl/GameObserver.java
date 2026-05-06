package tool.gameObserver.gameObserverImpl;

import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.*;
import view.GameView;

public class GameObserver implements tool.gameObserver.GameObserver {
    private final GameView view;

    public GameObserver(GameView view) {
        this.view = view;
    }

    @Override
    public void update(GameEvent event) {
        switch (event) {
            case AttackEvent e -> {
                if (e.attackerHealth() < 0) {
                    view.removeSprite(e.attackerPosition());
                }

                if (e.defenderHealth() < 0) {
                    view.removeSprite(e.defenderPosition());
                }

                if (e.defenderHealth() < 100 && e.defenderHealth() > 0) {
                    view.updateUnitHealth(e.defenderPosition(), e.defenderHealth());
                }

                if (e.attackerHealth() < 100 && e.attackerHealth() > 0) {
                    view.updateUnitHealth(e.attackerPosition(), e.attackerHealth());
                }
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
                view.showActionMenu(e.getPosition(), e.getActions());
            }

            case null, default ->  {
                break;
            }
        }
    }
}
