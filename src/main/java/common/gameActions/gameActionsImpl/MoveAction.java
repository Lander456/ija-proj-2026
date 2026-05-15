package common.gameActions.gameActionsImpl;

import common.Position;
import common.gameActions.GameAction;
import common.unit.Unit;
import game.Game;

public class MoveAction implements GameAction {
    private final Game game;
    private final Position start;
    private final Position end;
    private final Unit unit;

    public MoveAction(Game game, Position start, Position end, Unit unit) {
        this.game = game;
        this.start = start;
        this.end = end;
        this.unit = unit;
    }

    @Override
    public void execute() {
        game.moveUnit(start, end, unit);
        unit.setHasMoved(true);
    }

    @Override
    public void undo() {
        game.teleportUnit(start, unit);
        unit.setHasMoved(false);
    }
}
