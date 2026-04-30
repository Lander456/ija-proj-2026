package view;

import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.MoveEvent;
import common.terrain.Capturable;
import common.terrain.Terrain;
import common.tile.Tile;
import common.unit.Unit;
import game.Game;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import tool.GameObserver;

import java.util.Objects;

public class GameView extends GridPane implements GameObserver {
    private final Game game;
    private final int TILE_SIZE = 32;

    public GameView(Game game) {
        this.game = game;
        this.game.addObserver(this);
        initialRender();
    }

    private void initialRender() {
        Tile[][] mapData = game.getMap();

        for (int r = 0; r < mapData.length; r++) {
            for (int c = 0; c < mapData[r].length; c++) {
                Tile tile = mapData[r][c];
                Terrain terrain = tile.getTerrain();
                Image terrainImg;

                if (terrain instanceof Capturable) {
                    System.out.println("owner: " + ((Capturable) terrain).getOwner());
                    terrainImg = AssetManager.getSprite(terrain.getTerrainType(), ((Capturable) terrain).getOwner());
                } else {
                    terrainImg = AssetManager.getSprite(terrain.getTerrainType());
                }

                ImageView terrainView = createImageView(terrainImg, TILE_SIZE);

                this.add(terrainView, c, r);

                if (tile.getUnit() != null) {
                    Unit unit = tile.getUnit();

                    Image unitImg = AssetManager.getSprite(unit.getUnitType(), unit.getOwnedBy());

                    ImageView unitView = createImageView(unitImg, TILE_SIZE);

                    this.add(unitView, c, r);
                }
            }
        }
    }

    @Override
    public void update(GameEvent event) {
        if (event instanceof MoveEvent move) {
            handleMove(move);
        }
    }

    private void handleMove(MoveEvent move) {

    }

    private ImageView createImageView(Image img, int size) {
        ImageView iv = new ImageView(img);
        iv.setFitHeight(size);
        iv.setFitWidth(size);
        iv.setPreserveRatio(true);
        return iv;
    }
}
