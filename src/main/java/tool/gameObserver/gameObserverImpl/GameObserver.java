package tool.gameObserver.gameObserverImpl;

import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.*;
import view.GameView;

/**
 * Implementation of the GameObserver, used to convey updates from the Game to the View rendering it.
 * @author Tadeas Topinka (xtopint00)
 */
public class GameObserver implements tool.gameObserver.GameObserver {
    private final GameView view;

    /**
     * Constructor for the GameObserver.
     * @param view View to bind the GameObserver to.
     * @author Tadeas Topinka (xtopint00)
     */
    public GameObserver(GameView view) {
        this.view = view;
    }

    /**
     * Method used to update the GameObserver about an event that occurred in the game, that should be relayed to the
     * View and rendered.
     * @param event Event that occurred inside the Game.
     * @author Tadeas Topinka (xtopint00)
     */
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
                Integer unitHealth = e.unit().getHealth();
                view.addUnitSprite(e.position(), e.unit());
                if (unitHealth < 100) {
                    view.updateUnitHealth(e.position(), unitHealth);
                }
            }

            case CaptureEvent e -> view.updateTerrainSprite(e.capturableTile(), e.capturer());

            case MoveEvent e -> {
                view.clearHighlights();
                view.moveUnitSprite(e.from(), e.to());
            }

            case SelectEvent e -> {
                view.clearHighlights();
                if (e.isActive()) {
                    view.highlightReachableTiles(e.reachable());
                }
            }

            case ActionMenuEvent e -> view.showActionMenu(e.position(), e.actions());

            case TurnChangeEvent e -> view.turnChange(e.side());

            case FundsUpdateEvent e -> view.updateFundsDisplay(e.amount());

            case DeleteUnitEvent e -> view.removeSprite(e.position());

            case UpdateHealthEvent e -> view.updateUnitHealth(e.position(), e.unitHealth());

            case VictoryEvent e -> view.gameEnd(e.player());

            case null, default ->  {

            }
        }
    }
}
