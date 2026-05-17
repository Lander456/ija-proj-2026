package view;

import common.enums.GameState;
import common.gameEvents.eventTypes.TurnChangeEvent;
import game.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tool.io.GameSaver;
import tool.ai.AIManager;
import tool.gameController.gameControllerImpl.GameController;
import view.menus.MainMenu;
import view.menus.PauseMenu;

import java.io.File;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Advance Wars by FIT");

        MainMenu menuView = new MainMenu(primaryStage, this::initialiseGame);
        Scene menuScene = new Scene(menuView, 800, 600);

        this.primaryStage.setScene(menuScene);
        this.primaryStage.show();
    }

    private void initialiseGame(Game game) {
        GameView gameView = new GameView(game);
        Label turnLabel = gameView.getTurnLabel();
        gameView.turnChange(game.getActivePlayer().getSide());
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

        VBox turnControlPanel = new VBox();
        turnControlPanel.setAlignment(Pos.CENTER);
        turnControlPanel.setSpacing(10);
        turnControlPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.75); -fx-background-radius: 10; -fx-padding: 10;");
        turnControlPanel.getChildren().addAll(turnLabel, passTurnBtn);

        turnControlPanel.setMaxHeight(VBox.USE_PREF_SIZE);
        turnControlPanel.setMaxWidth(VBox.USE_PREF_SIZE);

        StackPane.setMargin(turnControlPanel, new Insets(20));
        StackPane.setAlignment(turnControlPanel, Pos.BOTTOM_CENTER);

        Button undoBtn = new Button("Undo");
        undoBtn.setOnAction(e -> gameController.undo());
        undoBtn.setFocusTraversable(false);

        Button redoBtn = new Button("Redo");
        redoBtn.setOnAction(e -> gameController.redo());
        redoBtn.setFocusTraversable(false);

        StackPane root = new StackPane();

        PauseMenu pauseMenu = new PauseMenu();

        String timeControlStyle = "-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;";
        undoBtn.setStyle(timeControlStyle);
        redoBtn.setStyle(timeControlStyle);

        StackPane.setMargin(undoBtn, new Insets(20));
        StackPane.setMargin(redoBtn, new Insets(20));

        StackPane.setAlignment(undoBtn, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(redoBtn, Pos.BOTTOM_RIGHT);

        FundsDisplay fundsDisplay = new FundsDisplay();
        StackPane.setAlignment(fundsDisplay, Pos.TOP_LEFT);

        gameView.setFundsDisplay(fundsDisplay);

        GameEndScreen gameEndScreen = new GameEndScreen();

        gameEndScreen.getReplayBtn().setOnAction(e -> {
            game.undo();
            gameEndScreen.setVisible(false);
            gameView.setDisable(false);
            root.requestFocus();
        });
        gameEndScreen.getSaveBtn().setOnAction(e -> {
            game.undo();
            saveGame(game);
        });
        gameEndScreen.getQuitBtn().setOnAction(e -> quitGame());

        gameView.setGameEndScreen(gameEndScreen);

        root.getChildren().addAll(scrollMap, fundsDisplay, turnOverlay, turnControlPanel, undoBtn, redoBtn, gameEndScreen, pauseMenu);

        Scene gameplayScene = new Scene(root, 800, 600);

        Runnable togglePauseMenu = () -> {
            if (pauseMenu.isVisible()) {
                pauseMenu.setVisible(false);
                root.requestFocus();
            } else {
                pauseMenu.setVisible(true);
                game.setGameState(GameState.PAUSE);
                pauseMenu.requestFocus();
            }
        };

        pauseMenu.getResumeBtn().setOnAction(e -> togglePauseMenu.run());
        pauseMenu.getSaveBtn().setOnAction(e -> saveGame(game));

        pauseMenu.getQuitBtn().setOnAction(e -> quitGame());

        gameplayScene.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case ESCAPE -> togglePauseMenu.run();
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

    private void saveGame(Game game) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save game");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON save files (*.json)", "*.json"));
        fileChooser.setInitialFileName("saveGame.json");
        fileChooser.setInitialDirectory(new File("./"));

        File selectedFile = fileChooser.showSaveDialog(primaryStage);

        if (selectedFile != null) {
            GameSaver.toFile(selectedFile, game);
        }
    }

    private void quitGame() {
        MainMenu menuView = new MainMenu(primaryStage, this::initialiseGame);
        Scene menuScene = new Scene(menuView, 800, 600);

        this.primaryStage.setScene(menuScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}