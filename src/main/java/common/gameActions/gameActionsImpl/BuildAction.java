package common.gameActions.gameActionsImpl;

import common.Position;
import common.enums.UnitTypes;
import common.gameActions.GameAction;
import game.Game;
import tool.io.dto.GameConfig;

/**
 * Defines a class used to contain a singular build action performed during the game, mainly used for journaling
 * purposes.
 * @author Tadeas Topinka (xtopint00)
 */
public class BuildAction implements GameAction {
    private final Game game;
    private final Integer unitCost;
    private final Position position;
    private final UnitTypes unitType;

    /**
     * Constructor for the BuildAction class.
     * @param game Game instance in which the action occurred.
     * @param unitCost Cost of the unit built, used to refund the player upon rewinding the action.
     * @param position Position where the unit got spawned, used to remove the unit during rewinds and to properly place the unit during execution.
     * @param unitType Type of unit spawned, used during execution to spawn the correct kind of unit.
     * @author Tadeas Topinka (xtopint00)
     */
    public BuildAction(Game game, Integer unitCost, Position position, UnitTypes unitType) {
        this.game = game;
        this.unitCost = unitCost;
        this.position = position;
        this.unitType = unitType;
    }

    /**
     * Executes the saved build action, spawning a unit.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void execute() {
        game.spendPlayerFunds(unitCost);
        game.buildUnit(position, unitType);
    }

    /**
     * Rewinds the action, removing the built unit and refunding the player who built it.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void undo() {
        game.removeUnit(position);
        game.addPlayerFunds(unitCost);
    }

    /**
     * Serializes the action data into a JSON format to be able to save the action.
     * @return JournalEntry of the action, which is a JSONifiable datatype.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.BuildActionJson buildActionJson = new GameConfig.BuildActionJson();

        buildActionJson.unitCost = unitCost;
        buildActionJson.x = position.x();
        buildActionJson.y = position.y();
        buildActionJson.unitType = unitType.toString();

        return buildActionJson;
    }
}
