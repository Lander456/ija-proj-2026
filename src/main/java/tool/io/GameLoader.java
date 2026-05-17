package tool.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.Player;
import common.Position;
import common.enums.GameState;
import common.enums.Players;
import common.enums.UnitTypes;
import common.gameActions.gameActionsImpl.*;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class used to define how a Game should be loaded from a JSON.
 * @author Tadeas Topinka (xtopint00)
 */
public class GameLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Loads a game from a compatible JSON file.
     * @param resourcePath Path to the JSON file.
     * @return Game instance with everything loaded in from said JSON file.
     * @author Tadeas Topinka (xtopint00)
     */
    public static Game fromJson(String resourcePath) {
        InputStream inputStream;
        try {
            File externalFile = new File(resourcePath);
            if (externalFile.exists() && externalFile.isFile()) {
                inputStream = new FileInputStream(externalFile);
            } else {
                inputStream = GameLoader.class.getResourceAsStream(resourcePath);
            }

            if (inputStream == null) throw new RuntimeException("File not found: " + resourcePath);

            GameConfig gameConfig = mapper.readValue(inputStream, GameConfig.class);

            Game game = new Game(gameConfig.terrain);

            game.setGameConfig(gameConfig);

            for (var entry : gameConfig.ownership) {
                game.updateTerrainOwner(entry.x, entry.y, entry.player);
            }

            for (var unit : gameConfig.units) {
                game.createUnit(unit.type, unit.player, unit.x, unit.y);
            }

            if (gameConfig.journal != null && !gameConfig.journal.isEmpty()) {
                executeJournal(game, gameConfig);
            }

            if (gameConfig.metadata != null) {
                game.configurePlayerRoles(gameConfig.metadata.redAI, gameConfig.metadata.blueAI);
            }

            game.setGameState(GameState.PAUSE);

            return game;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * If the JSON contained a journal of actions, executes this journal and brings the game to its end.
     * @param game Game to perform the journal actions on.
     * @param mapConf Map configuration for said game.
     * @author Tadeas Topinka (xtopint00)
     */
    private static void executeJournal(Game game, GameConfig mapConf) {
        for (GameConfig.JournalEntry entry : mapConf.journal) {
            switch (entry) {
                case GameConfig.MoveActionJson move -> {
                    Position startPos = new Position(move.startX, move.startY);
                    Position endPos = new Position(move.endX, move.endY);

                    Unit unit = game.getTile(startPos).getUnit();

                    MoveAction moveAction = new MoveAction(game, startPos, endPos, unit);

                    game.gameJournal.addAndExecute(moveAction);
                }

                case GameConfig.AttackActionJson attack -> {
                    Position attackerPos = new Position(attack.attackerX, attack.attackerY);
                    Position defenderPos = new Position(attack.defenderX, attack.defenderY);

                    Unit attacker = game.getTile(attackerPos).getUnit();
                    Unit defender = game.getTile(defenderPos).getUnit();

                    AttackAction attackAction = new AttackAction(game, attacker, defender, attack.attackerPrevHp, attack.defenderPrevHp);

                    game.gameJournal.addAndExecute(attackAction);
                }

                case GameConfig.BuildActionJson build -> {
                    Position buildPos = new Position(build.x, build.y);

                    UnitTypes unitType = UnitTypes.valueOf(build.unitType);

                    BuildAction buildAction = new BuildAction(game, build.unitCost, buildPos, unitType);

                    game.gameJournal.addAndExecute(buildAction);
                }

                case GameConfig.CaptureActionJson capture -> {
                    Position capturePos = new Position(capture.x, capture.y);
                    Players prevOwner = Players.fromString(capture.prevOwner);
                    Unit capturer = game.getTile(capturePos).getUnit();

                    CaptureAction captureAction = new CaptureAction(game, capturePos, capturer, prevOwner, capture.prevResistance);
                    game.gameJournal.addAndExecute(captureAction);
                }

                case GameConfig.EndTurnActionJson endTurn -> {
                    Player prevActive = game.getPlayer(Players.fromString(endTurn.prevActive));

                    EndTurnAction endTurnAction = new EndTurnAction(game, endTurn.prevFunds, prevActive);
                    game.gameJournal.addAndExecute(endTurnAction);
                }

                default -> throw new IllegalStateException("Unexpected value: " + entry);
            }
        }
    }
}
