package view.menus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PauseMenu extends VBox {
    private final Button saveBtn;
    private final Button resumeBtn;
    private final Button quitBtn;

    public PauseMenu() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        this.setStyle("-fx-background-color: rgba(44, 62, 80, 0.85);");
        this.setVisible(false);

        String buttonStyle = "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 16px; -fx-min-width: 200px; -fx-padding: 10 20;";

        resumeBtn = new Button("Resume game");
        resumeBtn.setStyle(buttonStyle);

        saveBtn = new Button("Save game");
        saveBtn.setStyle(buttonStyle);

        quitBtn = new Button("Quit");
        quitBtn.setStyle(buttonStyle);

        this.getChildren().addAll(saveBtn, resumeBtn, quitBtn);
    }

    public Button getSaveBtn() {
        return saveBtn;
    }
    public Button getResumeBtn() {
        return resumeBtn;
    }
    public Button getQuitBtn() {
        return quitBtn;
    }
}
