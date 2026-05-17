package view;

import common.Position;
import common.enums.Actions;
import common.enums.GameState;
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

/**
 * Defines the GameView showing the game board, units and buildings on it.
 * @author Tadeas Topinka (xtopint00)
 */
public class GameView extends GridPane {
    private final Game game;
    private final int TILE_SIZE = 32;
    private GameController controller;
    private TurnOverlay turnOverlay;
    private FundsDisplay fundsDisplay;
    private final Label turnLabel = new Label();
    private GameEndScreen gameEndScreen;

    public void setFundsDisplay(FundsDisplay fundsDisplay) {
        this.fundsDisplay = fundsDisplay;
    }

    public void setGameEndScreen(GameEndScreen gameEndScreen) {
        this.gameEndScreen = gameEndScreen;
    }

    public Label getTurnLabel() { return turnLabel; }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GameView(Game game) {
        this.game = game;
        GameObserver observer = new GameObserver(this);

        this.game.addObserver(observer);

        this.turnLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5 10; -fx-background-radius: 5;");
        initialRender();
    }

    public void setTurnOverlay(TurnOverlay turnOverlay) {
        this.turnOverlay = turnOverlay;
    }

    /**
     * Performs the initial render of the game map.
     * @author Tadeas Topinka (xtopint00)
     */
    private void initialRender() {
        Tile[][] mapData = game.getMap();

        for (int r = 0; r < mapData.length; r++) {
            for (int c = 0; c < mapData[r].length; c++) {
                StackPane tilePane = new StackPane();
                Tile tile = mapData[r][c];

                Terrain terrain = tile.getTerrain();
                Image terrainImg;

                final int finalR = r;
                final int finalC = c;
                tilePane.setOnMouseClicked(event -> onTileClicked(new Position(finalC, finalR), event));

                this.add(tilePane, c, r);

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

                    if (unit.getHealth() < 100) {
                        updateUnitHealth(unit.getPosition(), unit.getHealth());
                    }
                }
            }
        }
    }

    /**
     * Adds a unit sprite to the specified position on the map.
     * @param position Position to render the sprite to.
     * @param unit Unit to render on the given position.
     * @author Tadeas Topinka (xtopint00)
     */
    public void addUnitSprite(Position position, Unit unit) {
        StackPane tilePane = getTilePane(position.y(), position.x());
        Image unitImg = AssetManager.getSprite(unit.getUnitType().toString(), unit.getOwnedBy());

        ImageView unitView = createImageView(unitImg, TILE_SIZE);
        unitView.setId("unit");
        tilePane.getChildren().add(unitView);
    }

    /**
     * Creates an image view for an image inside the window (used to render sprites)
     * @param img Image to create the view for.
     * @param size Size of the resulting ImageView.
     * @return ImageView of the image.
     * @author Tadeas Topinka (xtopint00)
     */
    private ImageView createImageView(Image img, int size) {
        ImageView iv = new ImageView(img);
        iv.setFitHeight(size);
        iv.setFitWidth(size);
        iv.setPreserveRatio(true);
        return iv;
    }

    /**
     * Handles a tile click.
     * @param position Position of the clicked tile.
     * @param event Mouse event (button) that happened during this click.
     * @author Tadeas Topinka (xtoptin00)
     */
    private void onTileClicked(Position position, MouseEvent event) {
        controller.handleClick(position, event.getButton());
    }

    /**
     * Highlights all tiles reachable by a clicked unit in blue.
     * @param positions All the reachable tiles' positions.
     * @author Tadeas Topinka (xtopint00)
     */
    public void highlightReachableTiles(List<Position> positions) {
        for  (Position p : positions) {
            StackPane tilePane = getTilePane(p.y(), p.x());

            if (tilePane != null) {
                Rectangle highlight = new Rectangle(TILE_SIZE, TILE_SIZE);
                highlight.setFill(Color.web("#3498db", 0.5));
                highlight.setMouseTransparent(true);
                highlight.setId("highlight");

                tilePane.getChildren().add(highlight);
            }
        }
    }

    /**
     * Clears all the highlights.
     * @author Tadeas Topinka (xtopint00)
     */
    public void clearHighlights() {
        this.getChildren().forEach(node -> {
            if (node instanceof StackPane pane) {
                pane.getChildren().removeIf(child -> "highlight".equals(child.getId()));
            }
        });
    }

    /**
     * Gets a TilePane from the specified position.
     * @param row Row of the requested TilePane.
     * @param col Column of the requested TilePane.
     * @return StackPane of the tile.
     * @author Tadeas Topinka (xtopint00)
     */
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

    /**
     * Displays an action menu at the given position.
     * @param position Position to display the action menu at.
     * @param availableActions Actions available at that position (to be displayed in the menu.
     * @author Tadeas Topinka (xtopint00)
     */
    public void showActionMenu(Position position, List<Actions> availableActions) {
        ContextMenu menu = new ContextMenu();

        for (Actions action : availableActions) {
            switch (action) {
                case MOVE -> {
                    MenuItem move = new MenuItem("Wait");
                    move.setOnAction(event -> controller.moveUnit(position));
                    menu.getItems().add(move);
                }

                case ATTACK -> {
                    MenuItem attack = new MenuItem("Attack");
                    attack.setOnAction(event -> controller.attack(position));
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

                    buildInfantry.setOnAction(event -> controller.buildUnit(position, UnitTypes.Infantry));

                    buildArtillery.setOnAction(event -> controller.buildUnit(position, UnitTypes.Artillery));

                    buildTank.setOnAction(event -> controller.buildUnit(position, UnitTypes.Tank));

                    build.getItems().addAll(buildInfantry, buildTank, buildArtillery);

                    menu.getItems().add(build);
                }
            }
        }

        if (!menu.getItems().isEmpty()) {
            MenuItem cancel = new MenuItem("Cancel");
            cancel.setOnAction(event -> clearHighlights());
            menu.getItems().add(cancel);
        }

        StackPane tilePane = getTilePane(position.y(), position.x());

        menu.show(tilePane, Side.RIGHT, 0, 0);
    }

    /**
     * Moves a unit sprite from one tile to another.
     * @param startPosition Starting position of the sprite.
     * @param endPosition Destination for the sprite
     * @author Tadeas Topinka (xtopint00)
     */
    public void moveUnitSprite(Position startPosition, Position endPosition) {
        StackPane startPane = getTilePane(startPosition.y(), startPosition.x());
        StackPane endPane = getTilePane(endPosition.y(), endPosition.x());

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

    /**
     * Removes a sprite from the map.
     * @param position Postion of the sprite to be removed.
     * @author Tadeas Topinka (xtopint00)
     */
    public void removeSprite(Position position) {
        StackPane tilePane = getTilePane(position.y(), position.x());

        tilePane.getChildren().removeIf(node ->
                "unit".equals(node.getId()) || "health".equals(node.getId())
        );
    }

    /**
     * Updates a unit's displayed health.
     * @param position Position of the unit receiving the update.
     * @param unitHealth Unit health to display.
     * @author Tadeas Topinka (xtopint00)
     */
    public void updateUnitHealth(Position position, Integer unitHealth) {
        StackPane tilePane = getTilePane(position.y(), position.x());

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

    /**
     * Creates a health label for a unit.
     * @param health Health to display in the label.
     * @return A newly created label.
     * @author Tadeas Topinka (xtopint00)
     */
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

    /**
     * Performs a turnChange changing the turn labels text and showing the banner that it is a player's turn.
     * @param player Player who is receiving control.
     * @author Tadeas Topinka (xtopint00)
     */
    public void turnChange(Players player) {
        if (turnOverlay != null) {
            Color color = (player == Players.RED) ? Color.RED : Color.BLUE;

            turnOverlay.showTurn(player.toString(), color);
        }

        if (player == Players.RED) {
            turnLabel.setText("CURRENT PLAYER: RED");
            turnLabel.setStyle("-fx-text-fill: #ff0e0b; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-color: #fadbd8; -fx-padding: 5 10; -fx-background-radius: 5;");
        } else {
            turnLabel.setText("CURRENT PLAYER: BLUE");
            turnLabel.setStyle("-fx-text-fill: #0daeff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-color: #fadbd8; -fx-padding: 5 10; -fx-background-radius: 5;");
        }
    }

    /**
     * Updates a terrain sprite, usually due to control of teh sprite changing.
     * @param position Position of the sprite to be updated.
     * @param owner New owner of the tile.
     * @author Tadeas Topinka (xtopint00)
     */
    public void updateTerrainSprite(Position position, Players owner) {
        StackPane tilePane = getTilePane(position.y(), position.x());
        String terrainType = controller.getTileType(position);

        if (tilePane != null) {
            Image newTerrainImg = AssetManager.getSprite(terrainType, owner);

            tilePane.getChildren().removeIf(node -> "terrain".equals(node.getId()));

            ImageView terrainView = createImageView(newTerrainImg, TILE_SIZE);
            terrainView.setId("terrain");

            tilePane.getChildren().addFirst(terrainView);
        }
    }

    /**
     * Shows the game end screen.
     * @param victor Player who won the game.
     * @author Tadeas Topinka (xtopint00)
     */
    public void gameEnd(Players victor) {
        controller.setGameState(GameState.PAUSE);
        this.setDisable(true);

        gameEndScreen.showGameEndScreen(victor.toString());
    }

    /**
     * Updates the funds display to show the amount specified.
     * @param amount Amount to show in the funds display.
     * @author Tadeas Topinka (xtopint00)
     */
    public void updateFundsDisplay(int amount) {
        fundsDisplay.updateFunds(amount);
    }
}
