package view;

import common.enums.Players;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetManager {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getSprite(String type, Players player) {
        String key = type.toUpperCase() + "_" + player.toString();
        System.out.println(player.toString());

        return cache.computeIfAbsent(key, k -> {
            String path = String.format("/sprites/%s_%s.png",
                    type.toLowerCase(),
                    player.toString().toLowerCase());

            System.out.println("Attempting to load: " + path);
            return new Image(Objects.requireNonNull(AssetManager.class.getResourceAsStream(path)));
        });
    }

    public static Image getSprite(String type) {
        String key = type.toUpperCase();

        return cache.computeIfAbsent(key, k -> {
            String path = String.format("/sprites/%s.png",
                    type.toLowerCase());

            System.out.println("Attempting to load: " + path);
            return new Image(Objects.requireNonNull(AssetManager.class.getResourceAsStream(path)));
        });
    }
}
