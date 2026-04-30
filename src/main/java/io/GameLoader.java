package io;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.Game;
import io.dto.MapConfig;

import java.io.IOException;
import java.io.InputStream;

public class GameLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Game fromJson(String resourcePath) {
        try (InputStream is = GameLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) throw new RuntimeException("File not found: " + resourcePath);

            MapConfig mapConf = mapper.readValue(is, MapConfig.class);

            Game game = new Game(mapConf.terrain);

            for (var entry : mapConf.ownership) {
                game.updateTerrainOwner(entry.x, entry.y, entry.player);
            }

            for (var unit : mapConf.units) {
                game.createUnit(unit.type, unit.player, unit.x, unit.y);
            }

            return game;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
