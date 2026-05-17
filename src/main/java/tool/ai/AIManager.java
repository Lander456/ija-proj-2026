package tool.ai;

import common.enums.GameState;
import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.GameStateChangedEvent;
import common.gameEvents.eventTypes.TurnChangeEvent;
import common.gameEvents.eventTypes.VictoryEvent;
import game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import tool.gameObserver.GameObserver;
import view.GameView;

/**
 * Defines an observer class used to control the AI activity in the game.
 * @author Tadeas Topinka (xtopint00)
 */
public class AIManager implements GameObserver {

    private final Game game;
    private final GameView view;
    private final DummyBot bot;
    private Timeline aiTimeLine;

    /**
     * Constructor for the AIManager class.
     * @param game Game instance this manager will be overlooking.
     * @param view View this manager will be communicating the events to.
     * @author Tadeas Topinka (xtopint00)
     */
    public AIManager(Game game, GameView view) {
        this.game = game;
        this.view = view;
        this.bot = new DummyBot(game);

        setUpTimeline();
    }

    /**
     * Sets up the AI action timeline.
     * @author Tadeas Topinka (xtopint00)
     */
    private void setUpTimeline() {
        aiTimeLine = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> bot.takeAction()));
        aiTimeLine.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Catches any incoming events and processes them if they are relevant for the AI control.
     * @param event Event caught by the update.
     * @author Tadeas Topinka (xtopint00)
     */
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
            case VictoryEvent e -> haltAI();

            default -> {}
        }
    }

    /**
     * Stops the AI from performing any further actions (presumably because a pause had been performed by the player.
     * @author Tadeas Topinka (xtopint00)
     */
    public void haltAI() {
        if (aiTimeLine != null) {
            aiTimeLine.stop();
        }
    }
}
