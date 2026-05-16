package common.gameActions.gameActionsImpl;

import common.Position;
import common.enums.Actions;
import common.enums.UnitTypes;
import common.gameActions.GameAction;
import game.Game;
import tool.io.dto.GameConfig;

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

    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.BuildActionJson buildActionJson = new GameConfig.BuildActionJson();

        buildActionJson.unitCost = unitCost;
        buildActionJson.x = position.getX();
        buildActionJson.y = position.getY();
        buildActionJson.unitType = unitType.toString();

        return buildActionJson;
    }
}
