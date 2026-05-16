package tool.ai;

import common.enums.GameState;
import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.GameStateChangedEvent;
import common.gameEvents.eventTypes.TurnChangeEvent;
import game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import tool.gameObserver.GameObserver;
import view.GameView;

public class AIManager implements GameObserver {

    private final Game game;
    private final GameView view;
    private final DummyBot bot;
    private Timeline aiTimeLine;

    public AIManager(Game game, GameView view) {
        this.game = game;
        this.view = view;
        this.bot = new DummyBot(game);

        setUpTimeline();
    }

    private void setUpTimeline() {
        aiTimeLine = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> bot.takeAction()));
        aiTimeLine.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void update(GameEvent event) {
        switch (event) {
            case TurnChangeEvent e -> {
                if (game.isCurrentPlayerAI() && game.getGameState() == GameState.PLAY) {
                    view.setDisable(true);
                    if (aiTimeLine == null) {
                        setUpTimeline();
                    }
                    aiTimeLine.play();
                } else {
                    aiTimeLine.stop();
                    view.setDisable(false);
                }
            }
            case GameStateChangedEvent e -> {
                if (e.playerAI()) {
                    if (e.gameState() == GameState.PLAY) {
                        view.setDisable(true);
                        if (aiTimeLine != null) {
                            aiTimeLine.stop();
                        }
                        setUpTimeline();
                        aiTimeLine.play();
                    } else {
                        haltAI();
                        view.setDisable(false);
                    }
                }
            }

            default -> {}
        }
    }

    public void haltAI() {
        if (aiTimeLine != null) {
            aiTimeLine.stop();
        }
    }
}
