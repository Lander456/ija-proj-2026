package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;

/**
 * Defines an Attack event used to communicate to the observers that an attack has taken place and its results should be
 * displayed.
 * @param attackerPosition Position of the attacking unit.
 * @param defenderPosition Position of the defending unit.
 * @param attackerHealth Health of the attacker after the attack.
 * @param defenderHealth Health of the defender after the attack.
 * @author Tadeas Topinka (xtopint00)
 */
public record AttackEvent(Position attackerPosition,
                          Position defenderPosition,
                          Integer attackerHealth,
                          Integer defenderHealth) implements GameEvent {
}
