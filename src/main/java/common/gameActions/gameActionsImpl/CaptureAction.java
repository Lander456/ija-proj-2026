package common.gameActions.gameActionsImpl;

import common.Position;
import common.enums.Players;
import common.gameActions.GameAction;
import common.terrain.Capturable;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

public class CaptureAction implements GameAction {
    private final Game game;
    private final Position position;
    private final Unit capturingUnit;
    private final Players prevOwner;
    private final Integer prevResistance;
    private Boolean ownershipChanged;

    public CaptureAction(Game game, Position position, Unit capturingUnit, Players prevOwner, Integer prevResistance) {
        this.game = game;
        this.position = position;
        this.capturingUnit = capturingUnit;
        this.prevOwner = prevOwner;
        this.prevResistance = prevResistance;
    }

    @Override
    public void execute() {
        this.ownershipChanged = game.captureTile(position, capturingUnit);
    }

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
