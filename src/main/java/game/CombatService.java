package game;

import common.enums.UnitTypes;
import common.terrain.Terrain;
import common.unit.Unit;

import java.util.EnumMap;
import java.util.Map;

/**
 * Defines a class used to resolve combat between units, namely, calculating damage done between them.
 * @author Tadeas Topinka (xtopint00)
 */
public class CombatService {
    private static final Map<UnitTypes, Map<UnitTypes, Integer>> damageMatrix = new EnumMap<>(UnitTypes.class);

    static {
        Map<UnitTypes, Integer> tankAttack = new EnumMap<>(UnitTypes.class);
        tankAttack.put(UnitTypes.Tank, 55);
        tankAttack.put(UnitTypes.Infantry, 75);
        tankAttack.put(UnitTypes.Artillery, 70);
        damageMatrix.put(UnitTypes.Tank, tankAttack);

        Map<UnitTypes, Integer> infantryAttack = new EnumMap<>(UnitTypes.class);
        infantryAttack.put(UnitTypes.Tank, 5);
        infantryAttack.put(UnitTypes.Infantry, 55);
        infantryAttack.put(UnitTypes.Artillery, 15);
        damageMatrix.put(UnitTypes.Infantry, infantryAttack);

        Map<UnitTypes, Integer> artilleryAttack = new EnumMap<>(UnitTypes.class);
        artilleryAttack.put(UnitTypes.Tank, 70);
        artilleryAttack.put(UnitTypes.Infantry, 90);
        artilleryAttack.put(UnitTypes.Artillery, 75);
        damageMatrix.put(UnitTypes.Artillery, artilleryAttack);
    }

    /**
     * Calculates the damage dealt by a unit.
     * @param attacker Attacking unit.
     * @param defender Defending unit.
     * @param terrain Terrain the defending unit is standing on, used to get the defence bonus.
     * @return Integer representing damage done to the defender.
     * @author Tadeas Topinka (xtopint00)
     */
    public static Integer calculateDamage(Unit attacker, Unit defender, Terrain terrain) {
        int baseAttack = damageMatrix.get(attacker.getUnitType()).getOrDefault(defender.getUnitType(), 0);

        double attackerHpFactor = (double) attacker.getHealth() / 100;
        double terrainFactor = 1 - (terrain.getDefenceBonus() * 0.1);

        return (int) (baseAttack * attackerHpFactor * terrainFactor);
    }
}
