package common.gameActions.gameActionsImpl;

import common.Position;
import common.gameActions.GameAction;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

/**
 * Defines a class used to contain a singular move action performed during the game, mainly used for journaling
 * purposes.
 * @author Tadeas Topinka (xtopint00)
 */
public class MoveAction implements GameAction {
    private final Game game;
    private final Position start;
    private final Position end;
    private final Unit unit;

    /**
     * Constructor for the MoveAction.
     * @param game Game instance in which the move action took place, contains all the references to the involved classes.
     * @param start Position where the move originated from.
     * @param end Destination position for this move.
     * @param unit Unit which is being moved.
     * @author Tadeas Topinka (xtopint00)
     */
    public MoveAction(Game game, Position start, Position end, Unit unit) {
        this.game = game;
        this.start = start;
        this.end = end;
        this.unit = unit;
    }

    /**
     * Executes the move action and moves a unit on the game board from its original place to the destination.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void execute() {
        game.moveUnit(start, end, unit);
    }

    /**
     * Rewinds the move action, teleporting a unit back to the original space from where it began its move. Uses a
     * special teleport method to disregard dijkstra.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void undo() {
        game.teleportUnit(start, unit);
        unit.setHasMoved(false);
    }

    /**
     * Serializes the action data into a JSON format to be able to save the action.
     * @return JournalEntry of the action, which is a JSONifiable datatype.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.MoveActionJson moveActionJson = new GameConfig.MoveActionJson();

        moveActionJson.startX = start.x();
        moveActionJson.startY = start.y();
        moveActionJson.endX = end.x();
        moveActionJson.endY = end.y();

        return moveActionJson;
    }
}
