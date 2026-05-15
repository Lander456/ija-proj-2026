package view;

import common.Position;
import common.enums.Actions;
import common.enums.Players;
import common.enums.UnitTypes;
import common.terrain.Capturable;
import common.terrain.Terrain;
import common.tile.Tile;
import common.unit.Unit;
import game.Game;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tool.gameController.gameControllerImpl.GameController;
import tool.gameObserver.gameObserverImpl.GameObserver;

import java.util.List;

public class GameView extends GridPane {
    private final Game game;
    private final GameObserver observer;
    private final int TILE_SIZE = 32;
    private GameController controller;
    private TurnOverlay turnOverlay;
    private FundsDisplay fundsDisplay;

    public void setFundsDisplay(FundsDisplay fundsDisplay) {
        this.fundsDisplay = fundsDisplay;
    }

    public GameView(Game game) {
        this.game = game;
        this.observer = new GameObserver(this);

        this.game.addObserver(this.observer);
        initialRender();
    }

    public void setTurnOverlay(TurnOverlay turnOverlay) {
        this.turnOverlay = turnOverlay;
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
                    terrainImg = AssetManager.getSprite(terrain.getTerrainType(), ((Capturable) terrain).getOwner());
                } else {
                    terrainImg = AssetManager.getSprite(terrain.getTerrainType());
                }

                ImageView terrainView = createImageView(terrainImg, TILE_SIZE);
                terrainView.setId("terrain");
                tilePane.getChildren().add(terrainView);

                if (tile.getUnit() != null) {
                    Unit unit = tile.getUnit();

                    Image unitImg = AssetManager.getSprite(unit.getUnitType().toString(), unit.getOwnedBy());

                    ImageView unitView = createImageView(unitImg, TILE_SIZE);
                    unitView.setId("unit");
                    tilePane.getChildren().add(unitView);
                }

                final int finalR = r;
                final int finalC = c;
                tilePane.setOnMouseClicked(event -> {
                    onTileClicked(new Position(finalC, finalR), event);
                });

                this.add(tilePane, c, r);
            }
        }
    }

    public void addUnitSprite(Position position, Unit unit) {
        StackPane tilePane = getTilePane(position.getY(), position.getX());
        Image unitImg = AssetManager.getSprite(unit.getUnitType().toString(), unit.getOwnedBy());

        ImageView unitView = createImageView(unitImg, TILE_SIZE);
        unitView.setId("unit");
        tilePane.getChildren().add(unitView);
    }

    private ImageView createImageView(Image img, int size) {
        ImageView iv = new ImageView(img);
        iv.setFitHeight(size);
        iv.setFitWidth(size);
        iv.setPreserveRatio(true);
        return iv;
    }

    private void onTileClicked(Position position, MouseEvent event) {
        controller.handleClick(position, event.getButton());
    }

    public void highlightReachableTiles(List<Position> positions) {
        for  (Position p : positions) {
            StackPane tilePane = getTilePane(p.getY(), p.getX());

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

    public void showActionMenu(Position position, List<Actions> availableActions) {
        ContextMenu menu = new ContextMenu();

        for (Actions action : availableActions) {
            switch (action) {
                case MOVE -> {
                    MenuItem move = new MenuItem("Wait");
                    move.setOnAction(event -> {
                        controller.moveUnit(position);
                    });
                    menu.getItems().add(move);
                }

                case ATTACK -> {
                    MenuItem attack = new MenuItem("Attack");
                    attack.setOnAction(event -> {
                        controller.attack(position);
                    });
                    menu.getItems().add(attack);
                }

                case CAPTURE -> {
                    MenuItem capture = new MenuItem("Capture");
                    capture.setOnAction(event -> {
                        controller.capture(position);
                        clearHighlights();
                    });
                    menu.getItems().add(capture);
                }

                case BUILD -> {
                    Menu build = new Menu("Build");
                    MenuItem buildInfantry = new MenuItem(UnitTypes.Infantry.toString());
                    MenuItem buildTank = new MenuItem(UnitTypes.Tank.toString());
                    MenuItem buildArtillery = new MenuItem(UnitTypes.Artillery.toString());

                    buildInfantry.setOnAction(event -> {
                        controller.buildUnit(position, UnitTypes.Infantry);
                    });

                    buildArtillery.setOnAction(event -> {
                        controller.buildUnit(position, UnitTypes.Artillery);
                    });

                    buildTank.setOnAction(event -> {
                        controller.buildUnit(position, UnitTypes.Tank);
                    });

                    build.getItems().addAll(buildInfantry, buildTank, buildArtillery);

                    menu.getItems().add(build);
                }
            }
        }

        if (!menu.getItems().isEmpty()) {
            MenuItem cancel = new MenuItem("Cancel");
            cancel.setOnAction(event -> {
                clearHighlights();
            });
            menu.getItems().add(cancel);
        }

        StackPane tilePane = getTilePane(position.getY(), position.getX());

        menu.show(tilePane, Side.RIGHT, 0, 0);
    }

    public void moveUnitSprite(Position startPosition, Position endPosition) {
        StackPane startPane = getTilePane(startPosition.getY(), startPosition.getX());
        StackPane endPane = getTilePane(endPosition.getY(), endPosition.getX());

        if (startPane != null && endPane != null) {
            Node unitNode = null;
            Node healthNode = null;
            for (Node node : startPane.getChildren()) {
                if ("unit".equals(node.getId())) {
                    unitNode = node;
                }
                if ("health".equals(node.getId())) {
                    healthNode = node;
                }
            }

            if (unitNode != null) {
                startPane.getChildren().remove(unitNode);
                endPane.getChildren().add(unitNode);
            }

            if (healthNode != null) {
                startPane.getChildren().remove(healthNode);
                endPane.getChildren().add(healthNode);
            }
        }
    }

    public void removeSprite(Position position) {
        StackPane tilePane = getTilePane(position.getY(), position.getX());

        tilePane.getChildren().removeIf(node ->
                "unit".equals(node.getId()) || "health".equals(node.getId())
        );
    }

    public void updateUnitHealth(Position position, Integer unitHealth) {
        StackPane tilePane = getTilePane(position.getY(), position.getX());

        tilePane.getChildren().removeIf(node -> "health".equals(node.getId()));

        Label healthLabel = createHealthLabel(unitHealth);

        if (healthLabel == null) {
            return;
        }

        StackPane.setAlignment(healthLabel, Pos.BOTTOM_RIGHT);
        healthLabel.setTranslateX(-2);
        healthLabel.setTranslateY(-2);

        tilePane.getChildren().add(healthLabel);
    }

    private Label createHealthLabel(Integer health) {
        if (health < 100) {
            int displayVal = (int) Math.ceil(health / 10.0);

            Label healthLabel = new Label(String.valueOf(displayVal));

            healthLabel.setId("health");
            healthLabel.setTextFill(Color.WHITE);
            healthLabel.setMouseTransparent(true);
            healthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10px; -fx-effect: dropshadow(one-pass-box, black, 2, 1, 0, 0);");

            return healthLabel;
        }

        return null;
    }

    public void turnChange(Players player, int funds) {
        if (turnOverlay != null) {
            Color color = (player == Players.RED) ? Color.RED : Color.BLUE;

            turnOverlay.showTurn(player.toString(), color);
        }
    }

    public void updateTerrainSprite(Position position, Players owner) {
        StackPane tilePane = getTilePane(position.getY(), position.getX());
        String terrainType = controller.getTileType(position);

        if (tilePane != null) {
            Image newTerrainImg = AssetManager.getSprite(terrainType, owner);

            tilePane.getChildren().removeIf(node -> "terrain".equals(node.getId()));

            ImageView terrainView = createImageView(newTerrainImg, TILE_SIZE);
            terrainView.setId("terrain");

            tilePane.getChildren().addFirst(terrainView);
        }
    }

    public void updateFundsDisplay(int amount) {
        fundsDisplay.updateFunds(amount);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }
}
