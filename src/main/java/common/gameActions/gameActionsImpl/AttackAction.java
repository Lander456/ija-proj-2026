package common.gameActions.gameActionsImpl;

import common.Position;
import common.enums.Actions;
import common.gameActions.GameAction;
import common.gameEvents.eventTypes.UpdateHealthEvent;
import common.unit.Unit;
import game.Game;
import tool.io.dto.GameConfig;

public class AttackAction implements GameAction {
    private final Game game;
    private final Unit attacker, defender;
    private final int attackerPrevHp, defenderPrevHp;
    private final Boolean attackerMoved;
    private Boolean attackerKilled = false;
    private Boolean defenderKilled = false;

    public AttackAction(Game game, Unit attacker, Unit defender, int attackerPrevHp, int defenderPrevHp) {
        this.game = game;
        this.attacker = attacker;
        this.defender = defender;
        this.attackerPrevHp = attackerPrevHp;
        this.defenderPrevHp = defenderPrevHp;
        this.attackerMoved = attacker.getHasMoved();
    }

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

    @Override
    public GameConfig.JournalEntry toJson() {
        GameConfig.AttackActionJson attackActionJson = new GameConfig.AttackActionJson();

        attackActionJson.actionType = Actions.ATTACK.toString();
        attackActionJson.attackerX = attacker.getPosition().getX();
        attackActionJson.attackerY = attacker.getPosition().getY();
        attackActionJson.attackerPrevHp = attackerPrevHp;

        attackActionJson.defenderX = defender.getPosition().getX();
        attackActionJson.defenderY = defender.getPosition().getY();
        attackActionJson.defenderPrevHp = defenderPrevHp;

        return attackActionJson;
    }
}
