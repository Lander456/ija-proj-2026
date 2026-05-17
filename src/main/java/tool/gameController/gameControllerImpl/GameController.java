package tool.gameController.gameControllerImpl;

import common.Position;
import common.enums.GameState;
import common.enums.UnitTypes;
import common.tile.Tile;
import game.Game;
import javafx.scene.input.MouseButton;
import view.GameView;

/**
 * Defines the GameController class used to communicate commands from the GUI to the Game instance.
 * @author Tadeas Topinka (xtopint00)
 */
public class GameController implements tool.gameController.GameController {
    private final Game game;

    /**
     * GameController constructor.
     * @param game Game instance to bind this controller to.
     * @param view View to bind this controller to.
     * @author Tadeas Topinka (xtopint00)
     */
    public GameController(Game game, GameView view) {
        this.game = game;

        view.setController(this);
    }

    /**
     * Handle for when the view is clicked.
     * @param position Position that the player clicked.
     * @param button Mouse button used to click.
     * @author Tadeas Topinka (xtopint00)
     */
    public void handleClick(Position position, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            game.selectTile(position);
        } else if (button == MouseButton.SECONDARY) {
            game.handleActionMenu(position);
        }
    }

    /**
     * Used to convey a move order to the Game.
     * @param destination Destination for the move action.
     * @author Tadeas Topinka (xtopint00)
     */
    public void moveUnit(Position destination) {
        game.handleMove(destination);
    }

    /**
     * Used to convey an attack order to the Game.
     * @param targetPosition Position targeted by the attack.
     * @author Tadeas Topinka (xtopint00)
     */
    public void attack(Position targetPosition) {
        game.handleAttack(targetPosition);
    }

    /**
     * Used to convey an undo command to the Game.
     * @author Tadeas Topinka (xtopint00)
     */
    public void undo() {
        game.undo();
    }

    /**
     * Used to convey a redo command to the Game.
     * @author Tadeas Topinka (xotpint00)
     */
    public void redo() {
        game.redo();
    }

    /**
     * Used to convey an end turn command to the Game.
     * @author Tadeas Topinka (xtopint00)
     */
    public void endTurn() {
        game.handleEndTurn();
    }

    /**
     * Used to convey a capture command to the Game.
     * @param position Position where the capturing is taking place.
     * @author Tadeas Topinka (xtopint00)
     */
    public void capture(Position position) {
        game.handleCapture(position);
    }

    /**
     * Queries the game for what tile is present at certain coordinates.
     * @param position Position to query for its tile type.
     * @return String with the tile type.
     * @author Tadeas Topinka (xotpint00)
     */
    public String getTileType(Position position) {
        Tile[][] map = game.getMap();
        return map[position.y()][position.x()].getTerrain().getTerrainType();
    }

    /**
     * Conveys a build command to the Game.
     * @param position Position where the units should be built.
     * @param unitType Unit type to be built.
     * @author Tadeas Topinka (xtopint00)
     */
    public void buildUnit(Position position, UnitTypes unitType) {
        game.handleBuild(position, unitType);
    }

    /**
     * Conveys the set game state command to the Game.
     * @param gameState GameState to set the Game to.
     * @author Tadeas Topinka (xtopint00)
     */
    public void setGameState(GameState gameState) { game.setGameState(gameState); }
}
