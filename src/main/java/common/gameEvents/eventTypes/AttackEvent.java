package common.gameEvents.eventTypes;

import common.Position;
import common.gameEvents.GameEvent;
import common.unit.Unit;

public record AttackEvent(Position from,
                          Position to,
                          Unit attacker,
                          Unit defender) implements GameEvent {
}
