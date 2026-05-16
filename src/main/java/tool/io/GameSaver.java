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

public class GameSaver {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

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
