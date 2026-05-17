package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Defines the GameEndScreen GUI element, displayed to the player when a game ends (by capturing the opponent's HQ)
 * @author Tadeas Topinka (xtopint00)
 */
public class GameEndScreen extends VBox {
    private final Label victoryMessage;
    private final Button replayBtn;
    private final Button saveBtn;
    private final Button quitBtn;

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Button getQuitBtn() {
        return quitBtn;
    }

    public Button getReplayBtn() {
        return replayBtn;
    }

    public GameEndScreen() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(40));

        this.setStyle("-fx-background-color: rgba(24, 44, 97, 0.95);");
        this.setVisible(false);

        victoryMessage = new Label();
        victoryMessage.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f1c40f; -fx-padding: 0 0 20 0;");

        String btnStyle = "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 16px; -fx-min-width: 250px; -fx-padding: 12 25; -fx-background-radius: 5;";

        replayBtn = new Button("Undo last turn (Replay)");
        replayBtn.setStyle(btnStyle);

        saveBtn = new Button("Save game");
        saveBtn.setStyle(btnStyle);

        quitBtn = new Button("Quit to main menu");
        quitBtn.setStyle(btnStyle);

        this.getChildren().addAll(victoryMessage,  replayBtn, saveBtn, quitBtn);
    }

    public void showGameEndScreen(String winningSide) {
        victoryMessage.setText(winningSide + " player won!");
        this.setVisible(true);
        this.requestFocus();
    }
}
