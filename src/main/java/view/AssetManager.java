package view;

import common.enums.Players;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Defines a class used to get assets (sprites) from the resources folder.
 * @author Tadeas Topinka (xtopint00)
 */
public class AssetManager {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getSprite(String type, Players player) {
        String key = type.toUpperCase() + "_" + player.toString();

        return cache.computeIfAbsent(key, k -> {
            String path = String.format("/sprites/%s_%s.png",
                    type.toLowerCase(),
                    player.toString().toLowerCase());

            return new Image(Objects.requireNonNull(AssetManager.class.getResourceAsStream(path)));
        });
    }

    public static Image getSprite(String type) {
        String key = type.toUpperCase();

        return cache.computeIfAbsent(key, k -> {
            String path = String.format("/sprites/%s.png",
                    type.toLowerCase());

            return new Image(Objects.requireNonNull(AssetManager.class.getResourceAsStream(path)));
        });
    }
}
