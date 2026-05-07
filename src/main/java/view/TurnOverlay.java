package view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TurnOverlay extends StackPane {
    private final Label turnLabel;

    public TurnOverlay() {
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
        this.setMouseTransparent(true);
        this.setOpacity(0);

        turnLabel = new Label("");
        turnLabel.setStyle("-fx-font-size: 60px; -fx-font-weight: bold; -fx-text-fill: white;");

        this.getChildren().add(turnLabel);
    }

    public void showTurn(String playerName, Color color) {
        turnLabel.setText(playerName + "'s Turn");
        turnLabel.setTextFill(color);
        this.setMouseTransparent(false);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> this.setMouseTransparent(true));

        new SequentialTransition(fadeIn, pause, fadeOut).play();
    }
}
