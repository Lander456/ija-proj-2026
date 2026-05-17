package tool.ai;

import common.Player;
import common.Position;
import common.enums.UnitTypes;
import common.gameActions.GameAction;
import common.gameActions.gameActionsImpl.AttackAction;
import common.gameActions.gameActionsImpl.BuildAction;
import common.gameActions.gameActionsImpl.CaptureAction;
import common.gameActions.gameActionsImpl.MoveAction;
import common.terrain.Capturable;
import common.terrain.Terrain;
import common.tile.Tile;
import common.unit.Unit;
import common.unit.UnitRegistry;
import game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines a random dummy bot that just performs random actions with its units.
 * @author Tadeas Topinka (xtopint00)
 */
public class DummyBot {
    private final Game game;

    /**
     * Constructor for the DummyBot.
     * @param game Game this DummyBot will be used in.
     * @author Tadeas Topinka (xtopint00)
     */
    public DummyBot(Game game) {
        this.game = game;
    }

    /**
     * Instructs the bot to evaluate which actions are available to it and takes a random one.
     * @author Tadeas Topinka (xtopint00)
     */
    public void takeAction() {
        List<GameAction> availableActions = new ArrayList<>();
        Player activePlayer = game.getActivePlayer();

        for (Unit unit : activePlayer.getUnits()) {

            Position unitPos = unit.getPosition();

            if (game.getTile(unitPos).getTerrain() instanceof Capturable capturable) {
                if (unit.canCapture() && !unit.getHasMoved() && !unit.getHasAttacked() && capturable.getOwner() != activePlayer.getSide()) {
                    availableActions.add(new CaptureAction(game, unitPos, unit, capturable.getOwner(), capturable.getResistance()));
                }
            }

            if (!unit.getHasMoved()) {
                List<Position> reachableTiles = game.getReachableTiles(unitPos);

                for (Position reachableTile : reachableTiles) {
                    availableActions.add(new MoveAction(game, unitPos, reachableTile, unit));
                }
            }

            if (!unit.getHasAttacked()) {
                List<Position> targets = game.getAttackReach(unitPos, unit.minAttackRange(), unit.maxAttackRange());

                for (Position target : targets) {
                    Unit defender = game.getTile(target).getUnit();

                    availableActions.add(new AttackAction(game, unit, defender, unit.getHealth(), defender.getHealth()));
                }
            }
        }

        for (Capturable property : activePlayer.getProperties()) {
            if (property.spawnsUnits()) {

                Position propertyPos = null;
                //this is absolutely disgusting, I do not like it but do not have the time to rework how terrain works
                Tile[][] map = game.getMap();

                for (int y = 0; y < map.length; y++) {
                    for (int x = 0; x < map[y].length; x++) {
                        Terrain terrain = map[y][x].getTerrain();
                        Unit unit = map[y][x].getUnit();
                        if (terrain == property && unit == null) {
                            propertyPos = new Position(x, y);
                            break;
                        }
                    }
                    if (propertyPos != null) {
                        break;
                    }
                }

                if (propertyPos == null) {
                    continue;
                }

                int infantryCost = UnitRegistry.getCost(UnitTypes.Infantry);
                int artilleryCost = UnitRegistry.getCost(UnitTypes.Artillery);
                int tankCost = UnitRegistry.getCost(UnitTypes.Tank);

                if (activePlayer.getFunds() >= infantryCost) {
                    availableActions.add(new BuildAction(game, infantryCost, propertyPos, UnitTypes.Infantry));
                }
                if (activePlayer.getFunds() >= artilleryCost) {
                    availableActions.add(new BuildAction(game, artilleryCost, propertyPos, UnitTypes.Artillery));
                }
                if (activePlayer.getFunds() >= tankCost) {
                    availableActions.add(new BuildAction(game, tankCost, propertyPos, UnitTypes.Tank));
                }
            }
        }

        if (availableActions.isEmpty()) {
            game.handleEndTurn();
        } else {
            Collections.shuffle(availableActions);
            GameAction chosenAction = availableActions.getFirst();
            game.gameJournal.addAndExecute(chosenAction);
        }
    }
}
