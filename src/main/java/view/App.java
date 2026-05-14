package view;

import game.Game;
import io.GameLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tool.gameController.gameControllerImpl.GameController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Game gameLogic = GameLoader.fromJson("/mapDef/alaraRange.json");
        GameView gameView = new GameView(gameLogic);
        GameController gameController = new GameController(gameLogic, gameView);

        gameView.setController(gameController);

        ScrollPane scrollMap = new ScrollPane(gameView);
        scrollMap.setPannable(true);

        TurnOverlay turnOverlay = new TurnOverlay();
        gameView.setTurnOverlay(turnOverlay);

        Button passTurnBtn = new Button("End turn");
        passTurnBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        passTurnBtn.setOnAction(e -> gameController.endTurn());

        StackPane.setMargin(passTurnBtn, new Insets(20));
        StackPane.setAlignment(passTurnBtn, Pos.BOTTOM_RIGHT);

        FundsDisplay fundsDisplay = new FundsDisplay();
        StackPane.setAlignment(fundsDisplay, Pos.TOP_LEFT);

        gameView.setFundsDisplay(fundsDisplay);

        StackPane root = new StackPane();
        root.getChildren().addAll(scrollMap, fundsDisplay, turnOverlay, passTurnBtn);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Advance wars, yay");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
