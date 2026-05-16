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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import tool.io.GameSaver;
import tool.ai.AIManager;
import tool.gameController.gameControllerImpl.GameController;

import java.io.File;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Advance Wars by FIT");

        GameMenu menuView = new GameMenu(primaryStage, this::initialiseMatch);
        Scene menuScene = new Scene(menuView, 800, 600);

        this.primaryStage.setScene(menuScene);
        this.primaryStage.show();
    }

    private void initialiseMatch(Game game) {
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

        String timeControlStyle = "-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;";
        undoBtn.setStyle(timeControlStyle);
        redoBtn.setStyle(timeControlStyle);

        StackPane.setMargin(undoBtn, new Insets(20));
        StackPane.setMargin(redoBtn, new Insets(20));

        StackPane.setAlignment(undoBtn, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(redoBtn, Pos.BOTTOM_RIGHT);

        Button saveBtn = getSaveBtn(game, root);

        StackPane.setMargin(saveBtn, new Insets(20));
        StackPane.setAlignment(saveBtn, Pos.TOP_RIGHT);

        FundsDisplay fundsDisplay = new FundsDisplay();
        StackPane.setAlignment(fundsDisplay, Pos.TOP_LEFT);

        gameView.setFundsDisplay(fundsDisplay);

        root.getChildren().addAll(scrollMap, fundsDisplay, turnOverlay, passTurnBtn, undoBtn, redoBtn, saveBtn);

        Scene gameplayScene = new Scene(root, 800, 600);

        gameplayScene.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case SPACE -> {
                    if (game.getGameState() == GameState.PLAY) {
                        game.setGameState(GameState.PAUSE);
                    } else {
                        game.setGameState(GameState.PLAY);
                    }
                }
                case LEFT -> {
                    if (game.getGameState() == GameState.PAUSE) {
                        undoBtn.fire();
                    }
                }
                case RIGHT -> {
                    if (game.getGameState() == GameState.PAUSE) {
                        redoBtn.fire();
                    }
                }
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

    private @NotNull Button getSaveBtn(Game game, StackPane root) {
        Button saveBtn = new Button("Save game");
        saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        saveBtn.setFocusTraversable(false);

        saveBtn.setOnAction(e -> {
            GameState originalState = game.getGameState();
            game.setGameState(GameState.PAUSE);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save game");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON save files (*.json)", "*.json"));
            fileChooser.setInitialFileName("saveGame.json");
            fileChooser.setInitialDirectory(new File("./"));

            File selectedFile = fileChooser.showSaveDialog(primaryStage);

            if (selectedFile != null) {
                GameSaver.toFile(selectedFile, game);
            }

            game.setGameState(originalState);
            root.requestFocus();
        });
        return saveBtn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}