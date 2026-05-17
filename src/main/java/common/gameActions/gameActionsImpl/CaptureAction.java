package common.gameActions.gameActionsImpl;

import common.Position;
import common.enums.Players;
import common.gameActions.GameAction;
import common.terrain.Capturable;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

/**
 * Defines a class used to contain a singular capture action performed during the game, mainly used for journaling
 * purposes.
 * @author Tadeas Topinka (xtopint00)
 */
public class CaptureAction implements GameAction {
    private final Game game;
    private final Position position;
    private final Unit capturingUnit;
    private final Players prevOwner;
    private final Integer prevResistance;
    private Boolean ownershipChanged;

    /**
     * Constructor for the CaptureAction class.
     * @param game Game instance where this action took place to have references to the objects involved.
     * @param position Position where the capture action took place.
     * @param capturingUnit Unit that performed the capturing.
     * @param prevOwner Previous owner of the tile, mainly used for rewinding the action in case the owner changes.
     * @param prevResistance Resistance of the tile before the capture action took place, mainly used for rewinding the action.
     * @author Tadeas Topinka (xtopint00)
     */
    public CaptureAction(Game game, Position position, Unit capturingUnit, Players prevOwner, Integer prevResistance) {
        this.game = game;
        this.position = position;
        this.capturingUnit = capturingUnit;
        this.prevOwner = prevOwner;
        this.prevResistance = prevResistance;
    }

    /**
     * Executes the saved capture action, performing a capture with a unit on the given position coordinates,
     * potentially flipping tile ownership.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void execute() {
        this.ownershipChanged = game.captureTile(position, capturingUnit);
    }

    /**
     * Rewinds the capture action, resetting the resistance of the tile to the amount before the capturing took place
     * and potentially flipping the tile's ownership back to the original owner.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public void undo() {
        Capturable capturable = (Capturable) game.getTile(position).getTerrain();

        capturable.setResistance(prevResistance);

        capturingUnit.setHasAttacked(false);
        capturingUnit.setHasMoved(false);
        if (ownershipChanged) {
            game.transferProperty(capturable, capturable.getOwner(), prevOwner, position);
        }
    }

    /**
     * Serializes the action data into a JSON format to be able to save the action.
     * @return JournalEntry of the action, which is a JSONifiable datatype.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.CaptureActionJson captureActionJson = new GameConfig.CaptureActionJson();

        captureActionJson.x = position.x();
        captureActionJson.y = position.y();
        captureActionJson.prevOwner = prevOwner.toString();
        captureActionJson.prevResistance = prevResistance;

        return captureActionJson;
    }
}
