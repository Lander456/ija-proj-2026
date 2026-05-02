package view;

import common.Position;
import common.gameEvents.GameEvent;
import common.gameEvents.eventTypes.MoveEvent;
import common.terrain.Capturable;
import common.terrain.Terrain;
import common.tile.Tile;
import common.unit.Unit;
import game.Game;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tool.gameObserver.GameObserver;

import java.util.List;

public class GameView extends GridPane {
    private final Game game;
    private final GameObserver observer;
    private final int TILE_SIZE = 32;

    public GameView(Game game) {
        this.game = game;
        this.observer = new GameObserver(this);

        this.game.addObserver(this.observer);
        initialRender();
    }

    private void initialRender() {
        Tile[][] mapData = game.getMap();

        for (int r = 0; r < mapData.length; r++) {
            for (int c = 0; c < mapData[r].length; c++) {
                StackPane tilePane = new StackPane();
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
                terrainView.setId("terrain");
                tilePane.getChildren().add(terrainView);

                if (tile.getUnit() != null) {
                    Unit unit = tile.getUnit();

                    Image unitImg = AssetManager.getSprite(unit.getUnitType(), unit.getOwnedBy());

                    ImageView unitView = createImageView(unitImg, TILE_SIZE);
                    unitView.setId("unit");
                    tilePane.getChildren().add(unitView);
                }

                final int finalR = r;
                final int finalC = c;
                tilePane.setOnMouseClicked(event -> {
                    onTileClicked(finalR, finalC, event);
                });

                this.add(tilePane, c, r);
            }
        }
    }

    private ImageView createImageView(Image img, int size) {
        ImageView iv = new ImageView(img);
        iv.setFitHeight(size);
        iv.setFitWidth(size);
        iv.setPreserveRatio(true);
        return iv;
    }

    private void onTileClicked(int x, int y, MouseEvent event) {
        game.handleInput(x, y, event.getButton());
    }

    public void highlightReachableTiles(List<Position> positions) {
        for  (Position p : positions) {
            StackPane tilePane = getTilePane(p.getX(), p.getY());

            if (tilePane != null) {
                Rectangle highlight = new Rectangle(TILE_SIZE, TILE_SIZE);
                highlight.setFill(Color.web("#3498db", 0.5));
                highlight.setMouseTransparent(true);
                highlight.setId("highlight");

                tilePane.getChildren().add(highlight);
            }
        }
    }

    public void clearHighlights() {
        this.getChildren().forEach(node -> {
            if (node instanceof StackPane pane) {
                pane.getChildren().removeIf(child -> "highlight".equals(child.getId()));
            }
        });
    }

    public StackPane getTilePane(int row, int col) {
        for (Node node: this.getChildren()) {
            Integer rowIdx = GridPane.getRowIndex(node);
            Integer colIdx = GridPane.getColumnIndex(node);

            int r = (rowIdx == null) ? 0 : rowIdx;
            int c = (colIdx == null) ? 0 : colIdx;

            if (r == row && c == col && node instanceof StackPane) {
                return (StackPane) node;
            }
        }
        return null;
    }

    public void showActionMenu(Position position) {
        ContextMenu menu = new ContextMenu();

        MenuItem wait = new MenuItem("Wait");
        wait.setOnAction(event -> {
            game.confirmMove(position);
        });

        MenuItem cancel = new MenuItem("Cancel");
        cancel.setOnAction(event -> clearHighlights());

        menu.getItems().addAll(wait, cancel);

        StackPane tilePane = getTilePane(position.getX(), position.getY());

        menu.show(tilePane, Side.RIGHT, 0, 0);
    }

    public void moveUnitSprite(Position startPosition, Position endPosition) {
        StackPane startPane = getTilePane(startPosition.getX(), startPosition.getY());
        StackPane endPane = getTilePane(endPosition.getX(), endPosition.getY());

        if (startPane != null && endPane != null) {
            Node unitNode = null;
            for (Node node : startPane.getChildren()) {
                if (node instanceof ImageView && node.getId().equals("unit")) {
                    unitNode = node;
                    break;
                }
            }

            if (unitNode != null) {
                startPane.getChildren().remove(unitNode);
                endPane.getChildren().add(unitNode);
            }
        }
    }
}
