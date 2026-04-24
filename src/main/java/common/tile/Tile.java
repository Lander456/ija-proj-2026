package common.tile;

import common.terrain.Terrain;
import common.terrain.TerrainFactory;
import common.unit.Unit;

public class Tile {

    private final Terrain terrain;
    private Unit unit;

    public Terrain getTerrain() { return terrain; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public Tile(String terrainCode, String owner) {
        this.terrain = TerrainFactory.create(terrainCode, owner);
        this.unit = null;
    }
}
