package tool.gameController.gameControllerImpl;

import common.Position;
import common.enums.GameState;
import common.enums.UnitTypes;
import common.tile.Tile;
import game.Game;
import javafx.scene.input.MouseButton;
import view.GameView;

public class GameController implements tool.gameController.GameController {
    private final Game game;

    public GameController(Game game, GameView view) {
        this.game = game;

        view.setController(this);
    }

    public void handleClick(Position position, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            game.selectTile(position);
        } else if (button == MouseButton.SECONDARY) {
            game.handleActionMenu(position);
        }
    }

    public void moveUnit(Position destination) {
        game.handleMove(destination);
    }

    public void attack(Position targetPosition) {
        game.handleAttack(targetPosition);
    }

    public void undo() {
        game.undo();
    }

    public void redo() {
        game.redo();
    }

    public void endTurn() {
        game.handleEndTurn();
    }

    public void capture(Position position) {
        game.handleCapture(position);
    }

    public String getTileType(Position position) {
        Tile[][] map = game.getMap();
        return map[position.y()][position.x()].getTerrain().getTerrainType();
    }

    public void buildUnit(Position position, UnitTypes unitType) {
        game.handleBuild(position, unitType);
    }

    public void setGameState(GameState gameState) { game.setGameState(gameState); }
}
