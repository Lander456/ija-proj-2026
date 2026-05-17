package view.menus;

import game.Game;
import javafx.stage.FileChooser;
import tool.io.GameLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class MainMenu extends VBox {

    private final Stage primaryStage;
    private final Consumer<Game> onGameReady;

    public MainMenu(Stage primaryStage, Consumer<Game> onGameReady) {
        this.primaryStage = primaryStage;
        this.onGameReady = onGameReady;

        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(50));
        this.setStyle("-fx-background-color: #2c3e50;");

        showMainMenu();
    }

    private void showMainMenu() {
        this.getChildren().clear();

        Label title = new Label("ADVANCE WARS BY FIT");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold; -fx-padding: 0 0 20 0;");

        Button newGameBtn = new Button("New Game");
        Button loadGameBtn = new Button("Load Game");
        Button exitBtn = new Button("Exit");

        // Shared button style for cohesion
        String menuBtnStyle = "-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-min-width: 220px; -fx-padding: 10 20;";
        newGameBtn.setStyle(menuBtnStyle);
        loadGameBtn.setStyle(menuBtnStyle);
        exitBtn.setStyle(menuBtnStyle);

        newGameBtn.setOnAction(e -> showSetupMenu());
        loadGameBtn.setOnAction(e -> handleLoadGame());
        exitBtn.setOnAction(e -> System.exit(0));

        this.getChildren().addAll(title, newGameBtn, loadGameBtn, exitBtn);
    }

    private void showSetupMenu() {
        this.getChildren().clear();

        Label setupTitle = new Label("MATCH SETUP");
        setupTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        Label mapLabel = new Label("Select Map Battleground:");
        mapLabel.setStyle("-fx-text-fill: #ecf0f1;");
        ComboBox<String> mapPicker = new ComboBox<>();
        mapPicker.getItems().addAll("alaraRange.json", "visionBridge.json"); // Add your map files here
        mapPicker.setValue("alaraRange.json");

        Label redLabel = new Label("Player 1 (RED) Controller:");
        redLabel.setStyle("-fx-text-fill: #ecf0f1;");
        ComboBox<String> redType = new ComboBox<>();
        redType.getItems().addAll("Human", "AI Bot");
        redType.setValue("Human");

        Label blueLabel = new Label("Player 2 (BLUE) Controller:");
        blueLabel.setStyle("-fx-text-fill: #ecf0f1;");
        ComboBox<String> blueType = new ComboBox<>();
        blueType.getItems().addAll("Human", "AI Bot");
        blueType.setValue("AI Bot");

        Button launchBtn = new Button("Launch Match");
        launchBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-min-width: 150px;");

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 150px;");

        launchBtn.setOnAction(e -> {
            String relativeMapPath = "/mapDef/" + mapPicker.getValue();

            boolean redAI = redType.getValue().equals("AI Bot");
            boolean blueAI = blueType.getValue().equals("AI Bot");

            Game game = GameLoader.fromJson(relativeMapPath);

            game.configurePlayerRoles(redAI, blueAI);

            onGameReady.accept(game);
        });

        backBtn.setOnAction(e -> showMainMenu());

        this.getChildren().addAll(setupTitle, mapLabel, mapPicker, redLabel, redType, blueLabel, blueType, launchBtn, backBtn);
    }

    private void handleLoadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open In-Progress Game Save JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                Game loadedGame = GameLoader.fromJson(selectedFile.getAbsolutePath());
                onGameReady.accept(loadedGame);
            } catch (Exception ex) {
                System.err.println("Error decoding save file mapping parameters.");
                ex.printStackTrace();
            }
        }
    }
}