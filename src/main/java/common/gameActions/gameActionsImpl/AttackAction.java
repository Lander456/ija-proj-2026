package common.gameActions.gameActionsImpl;

import common.Position;
import common.gameActions.GameAction;
import common.gameEvents.eventTypes.UpdateHealthEvent;
import common.unit.Unit;
import game.Game;

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
}
