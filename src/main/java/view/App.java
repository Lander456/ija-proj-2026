package view;

import game.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        String[] mockMap = {
                "P P M",
                "P F W",
                "P P P"
        };

        Game gameLogic = new Game(mockMap);

        gameLogic.createUnit("Infantry", "Player1", 0, 0);

        GameView gameView = new GameView(gameLogic);

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
