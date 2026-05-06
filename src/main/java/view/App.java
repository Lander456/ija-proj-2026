package view;

import game.Game;
import io.GameLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import tool.gameController.gameControllerImpl.GameController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        String[] mockMap = {
                "P P M",
                "P F W",
                "P P P"
        };

        Game gameLogic = GameLoader.fromJson("/mapDef/alaraRange.json");

        GameView gameView = new GameView(gameLogic);

        GameController gameController = new GameController(gameLogic, gameView);

        gameView.setController(gameController);

        ScrollPane root = new ScrollPane(gameView);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Advance wars, yay");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
