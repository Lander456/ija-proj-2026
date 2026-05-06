package tool.gameController.gameControllerImpl;

import common.Position;
import game.Game;
import javafx.scene.input.MouseButton;
import view.GameView;

public class GameController implements tool.gameController.GameController {
    private final Game game;
    private final GameView view;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;

        this.view.setController(this);
    }

    public void handleClick(Position position, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            game.selectTile(position);
        }
    }

    public void moveUnit(Position destination) {
        game.handleMove(destination);
    }

    public void attack(Position targetPosition) {
        game.handleAttack(targetPosition);
    }
}
