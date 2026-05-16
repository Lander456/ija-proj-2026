package common.gameActions.gameActionsImpl;

import common.Player;
import common.enums.Actions;
import common.gameActions.GameAction;
import common.gameEvents.eventTypes.UpdateHealthEvent;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

import java.util.HashMap;
import java.util.Map;

public class EndTurnAction implements GameAction {
    private final Game game;
    private final Integer prevFunds;
    private final Player prevActive;
    private final Map<Unit, Integer> healedUnits =  new HashMap<>();

    public EndTurnAction(Game game, Integer prevFunds, Player prevActive) {
        this.game = game;
        this.prevFunds = prevFunds;
        this.prevActive = prevActive;
    }

    @Override
    public void execute() {
        game.endTurn(this);
    }

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

    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.EndTurnActionJson endTurnActionJson = new GameConfig.EndTurnActionJson();

        endTurnActionJson.actionType = Actions.END_TURN.toString();
        endTurnActionJson.prevFunds = prevFunds;
        endTurnActionJson.prevActive = prevActive.getSide().toString();

        return endTurnActionJson;
    }

    public void addHealedUnit(Unit unit, Integer prevHealth){
        healedUnits.put(unit, prevHealth);
    }
}
