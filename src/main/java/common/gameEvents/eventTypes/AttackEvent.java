package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;
import common.unit.Unit;

public record AttackEvent(Position attackerPosition,
                          Position defenderPosition,
                          Integer attackerHealth,
                          Integer defenderHealth) implements GameEvent {
}
