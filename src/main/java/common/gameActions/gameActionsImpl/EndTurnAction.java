package common.gameActions.gameActionsImpl;

import common.Player;
import common.gameActions.GameAction;
import common.gameEvents.eventTypes.UpdateHealthEvent;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a class used to contain a singular end turn action performed during the game, mainly used for journaling
 * purposes.
 * @author Tadeas Topinka (xtopint00)
 */
public class EndTurnAction implements GameAction {
    private final Game game;
    private final Integer prevFunds;
    private final Player prevActive;
    private final Map<Unit, Integer> healedUnits =  new HashMap<>();

    /**
     * Constructor for the EndTurnAction
     * @param game Game instance where the action took place, contains references to the objects involved.
     * @param prevFunds Funds before the end turn took place (since it triggers the income phase).
     * @param prevActive Previous active player, who was active before control swapped.
     * @author Tadeas Topinka (xtopint00)
     */
    public EndTurnAction(Game game, Integer prevFunds, Player prevActive) {
        this.game = game;
        this.prevFunds = prevFunds;
        this.prevActive = prevActive;
    }

    /**
     * Executes the end turn action, handing control over to the other player and triggering unit healing and income phase.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void execute() {
        game.endTurn(this);
    }

    /**
     * Rewinds the end turn action, gicing control back to the previous player and refunding all heals and undoing them
     * as well. Refunds all income incurred through this end turn too.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void undo() {
        prevActive.setFunds(prevFunds);
        game.transferControl(prevActive);
        healedUnits.forEach((unit, prevHealth) -> {
            unit.setHealth(prevHealth);
            Player healedOwner = game.getPlayer(unit.getOwnedBy());
            healedOwner.addFunds(unit.cost() / 10);
            game.notifyObservers(new UpdateHealthEvent(unit.getPosition(), unit.getHealth()));
        });
    }

    /**
     * Serializes the action data into a JSON format to be able to save the action.
     * @return JournalEntry of the action, which is a JSONifiable datatype.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.EndTurnActionJson endTurnActionJson = new GameConfig.EndTurnActionJson();

        endTurnActionJson.prevFunds = prevFunds;
        endTurnActionJson.prevActive = prevActive.getSide().toString();

        return endTurnActionJson;
    }

    public void addHealedUnit(Unit unit, Integer prevHealth){
        healedUnits.put(unit, prevHealth);
    }
}
