package io.dto;

import java.util.List;

public class MapConfig {
    public String[][] terrain;
    public List<OwnershipEntry> ownership;
    public List<UnitEntry> units;

    public static class OwnershipEntry {
        public int x, y;
        public String player;
    }

    public static class UnitEntry {
        public String type;
        public String player;
        public int x;
        public int y;
    }
}
