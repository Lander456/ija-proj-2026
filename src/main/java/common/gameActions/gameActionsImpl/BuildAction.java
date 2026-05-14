package common.gameActions.gameActionsImpl;

import common.Position;
import common.enums.UnitTypes;
import common.gameActions.GameAction;
import game.Game;

public class BuildAction implements GameAction {
    private final Game game;
    private final Integer unitCost;
    private final Position position;
    private final UnitTypes unitType;

    public BuildAction(Game game, Integer unitCost, Position position, UnitTypes unitType) {
        this.game = game;
        this.unitCost = unitCost;
        this.position = position;
        this.unitType = unitType;
    }

    @Override
    public void execute() {
        game.spendPlayerFunds(unitCost);
        game.buildUnit(position, unitType);
    }

    @Override
    public void undo() {
        game.removeUnit(position);
        game.addPlayerFunds(unitCost);
    }
}
