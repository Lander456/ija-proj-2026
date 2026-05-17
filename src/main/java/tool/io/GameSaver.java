package tool.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import common.gameActions.GameAction;
import game.Game;
import tool.io.dto.GameConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Defines a class used to save games into a JSON format.
 * @author Tadeas Topinka (xtopint00)
 */
public class GameSaver {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Saves the game to the specified file.
     * @param targetFile File to save the game into.
     * @param game Game to be saved.
     * @author Tadeas Topinka (xtopint00)
     */
    public static void toFile(File targetFile, Game game) {
        GameConfig gameConfig = game.getGameConfig();

        Iterator<GameAction> gameActionIterator = game.gameJournal.getHistory().descendingIterator();

        gameConfig.metadata = new GameConfig.Metadata();
        gameConfig.metadata.redAI = game.getRedAI();
        gameConfig.metadata.blueAI = game.getBlueAI();
        gameConfig.journal = new ArrayList<>();

        while (gameActionIterator.hasNext()) {
            GameAction gameAction = gameActionIterator.next();

            gameConfig.journal.add(gameAction.toJson());
        }

        try {
            mapper.writeValue(targetFile, gameConfig);
        } catch (IOException e) {
            throw new RuntimeException("Failure while saving the game", e);
        }
    }
}
