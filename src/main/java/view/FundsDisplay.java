package view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FundsDisplay extends HBox {
    private final Label fundsLabel;

    public FundsDisplay() {
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 10; -fx-background-radius: 0 0 10 0; -fx-border-color: #f1c40f; -fx-border-width: 0 2 2 0;");

        this.fundsLabel = new Label("G: 2000");
        this.fundsLabel.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 18px; -fx-font-weight: bold;");

        this.getChildren().add(fundsLabel);
        this.setMouseTransparent(true);
    }

    public void updateFunds(int amount) {
        this.fundsLabel.setText("G: " + amount);
    }
}
