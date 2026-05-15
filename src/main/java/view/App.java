package view;

import common.enums.GameState;
import common.gameEvents.eventTypes.TurnChangeEvent;
import game.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tool.ai.AIManager;
import tool.gameController.gameControllerImpl.GameController;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Advance Wars by FIT");

        // 1. Initialize the application container showing only the interactive state machine menu
        GameMenu menuView = new GameMenu(primaryStage, this::initializeActiveGameplayScene);
        Scene menuScene = new Scene(menuView, 800, 600);

        this.primaryStage.setScene(menuScene);
        this.primaryStage.show();
    }

    /**
     * This receives the generated configuration payload sequence and loads the map viewport HUD layout.
     */
    private void initializeActiveGameplayScene(Game game) {
        GameView gameView = new GameView(game);
        GameController gameController = new GameController(game, gameView);
        gameView.setController(gameController);

        AIManager aiManager = new AIManager(game, gameView);
        game.addObserver(aiManager);

        ScrollPane scrollMap = new ScrollPane(gameView);
        scrollMap.setPannable(true);

        TurnOverlay turnOverlay = new TurnOverlay();
        gameView.setTurnOverlay(turnOverlay);

        Button passTurnBtn = new Button("End turn");
        passTurnBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        passTurnBtn.setOnAction(e -> gameController.endTurn());

        StackPane.setMargin(passTurnBtn, new Insets(20));
        StackPane.setAlignment(passTurnBtn, Pos.BOTTOM_CENTER);

        Button undoBtn = new Button("Undo");
        undoBtn.setOnAction(e -> gameController.undo());
        undoBtn.setFocusTraversable(false);

        Button redoBtn = new Button("Redo");
        redoBtn.setOnAction(e -> gameController.redo());
        redoBtn.setFocusTraversable(false);

        StackPane root = new StackPane();

        Button resumeBtn = new Button("Resume");
        resumeBtn.setOnAction(e -> {
            if (game.getGameState() == GameState.REPLAY) {
                game.setGameState(GameState.PLAY);

                if (game.isCurrentPlayerAI()) {
                    gameView.setDisable(true);
                    aiManager.update(new TurnChangeEvent(
                            game.getActivePlayer().getSide(),
                            game.getActivePlayer().getFunds()
                    ));
                }

                root.requestFocus();
            }
        });
        resumeBtn.setFocusTraversable(false);

        String timeControlStyle = "-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;";
        resumeBtn.setStyle(timeControlStyle);
        undoBtn.setStyle(timeControlStyle);
        redoBtn.setStyle(timeControlStyle);

        StackPane.setMargin(undoBtn, new Insets(20));
        StackPane.setMargin(redoBtn, new Insets(20));

        StackPane.setAlignment(undoBtn, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(redoBtn, Pos.BOTTOM_RIGHT);

        FundsDisplay fundsDisplay = new FundsDisplay();
        StackPane.setAlignment(fundsDisplay, Pos.TOP_LEFT);

        gameView.setFundsDisplay(fundsDisplay);

        root.getChildren().addAll(scrollMap, fundsDisplay, turnOverlay, passTurnBtn, undoBtn, redoBtn, resumeBtn);

        Scene gameplayScene = new Scene(root, 800, 600);

        gameplayScene.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case SPACE -> {
                    System.out.println("Space pressed");
                    if (game.getGameState() == GameState.PLAY) {
                        game.setGameState(GameState.REPLAY);
                    } else {
                        resumeBtn.fire();
                    }
                }
                case LEFT -> undoBtn.fire();
                case RIGHT -> redoBtn.fire();
            }
        });

        this.primaryStage.setScene(gameplayScene);

        if (game.isCurrentPlayerAI()) {
            gameView.setDisable(true);
            aiManager.update(new TurnChangeEvent(
                    game.getActivePlayer().getSide(),
                    game.getActivePlayer().getFunds()
            ));
        }

        root.setFocusTraversable(true);
        root.requestFocus();

        gameplayScene.setOnMouseClicked(e -> root.requestFocus());
    }

    public static void main(String[] args) {
        launch(args);
    }
}