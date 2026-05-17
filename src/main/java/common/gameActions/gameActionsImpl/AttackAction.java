package common.gameActions.gameActionsImpl;

import common.Position;
import common.gameActions.GameAction;
import common.gameEvents.eventTypes.UpdateHealthEvent;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

/**
 * Defines a class used to contain a singular attack action performed during the game, mainly used for journaling
 * purposes.
 * @author Tadeas Topinka (xtopint00)
 */
public class AttackAction implements GameAction {
    private final Game game;
    private final Unit attacker, defender;
    private final int attackerX, attackerY, defenderX, defenderY;
    private final int attackerPrevHp, defenderPrevHp;
    private final Boolean attackerMoved;
    private Boolean attackerKilled = false;
    private Boolean defenderKilled = false;

    /**
     * Constructor for the AttackAction class.
     * @param game Game instance where this action happened to have a reference to the units involved.
     * @param attacker Attacking unit.
     * @param defender Defending unit.
     * @param attackerPrevHp Hp of the attacker from before the attack occurred, used for journaling and undo/redo actions.
     * @param defenderPrevHp Hp of the defender from before the attack occurred used for journaling and undo/redo acitons.
     * @author Tadeas Topinka (xtopint00)
     */
    public AttackAction(Game game, Unit attacker, Unit defender, int attackerPrevHp, int defenderPrevHp) {
        this.game = game;
        this.attacker = attacker;
        this.defender = defender;
        this.attackerX = attacker.getPosition().x();
        this.attackerY = attacker.getPosition().y();
        this.defenderX = defender.getPosition().x();
        this.defenderY = defender.getPosition().y();
        this.attackerPrevHp = attackerPrevHp;
        this.defenderPrevHp = defenderPrevHp;
        this.attackerMoved = attacker.getHasMoved();
    }

    /**
     * Method used to execute the action.
     */
    @Override
    public void execute() {
        game.attack(attacker, defender);

        if (attacker.getHealth() < 0) {
            attackerKilled = true;
        }

        if (defender.getHealth() < 0) {
            defenderKilled = true;
        }
    }

    /**
     * Method used to rewind the action.
     */
    @Override
    public void undo() {
        attacker.setHealth(attackerPrevHp);
        game.notifyObservers(new UpdateHealthEvent(attacker.getPosition(), attacker.getHealth()));

        defender.setHealth(defenderPrevHp);
        game.notifyObservers(new UpdateHealthEvent(defender.getPosition(), defender.getHealth()));

        attacker.setHasMoved(attackerMoved);
        attacker.setHasAttacked(false);

        if (attackerKilled) {
            Position attackerPos = attacker.getPosition();
            game.restoreUnit(attacker, attackerPos);
        }

        if (defenderKilled) {
            Position defenderPos = defender.getPosition();
            game.restoreUnit(defender, defenderPos);
        }
    }

    /**
     * Serializes the action data into a JSON format to be able to save the action.
     * @return JournalEntry of the action, which is a JSONifiable datatype.
     * @author Tadeas Topinka (xtopint00)
     */
    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.AttackActionJson attackActionJson = new GameConfig.AttackActionJson();

        attackActionJson.attackerX = attackerX;
        attackActionJson.attackerY = attackerY;
        attackActionJson.attackerPrevHp = attackerPrevHp;

        attackActionJson.defenderX = defenderX;
        attackActionJson.defenderY = defenderY;
        attackActionJson.defenderPrevHp = defenderPrevHp;

        return attackActionJson;
    }
}
