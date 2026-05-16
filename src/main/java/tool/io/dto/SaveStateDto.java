package tool.io.dto;

import java.util.List;

public class SaveStateDto {
    public String activePlayer;
    public List<PlayerDataDto> players;
    public List<TileDataDto> tileData;
    public List<CommandDataDto> gameJournal;

    public static class PlayerDataDto {
        public String side;
        public int funds;
    }

    public static class TileDataDto {
        public int x, y;
        public String terrainType;
        public String propertyOwner;
        public UnitDataDto unit;
    }

    public static class UnitDataDto {
        public String Type;
        public int health;
        public String owner;
        public boolean hasMoved;
        public boolean hasAttacked;
    }

    public static class CommandDataDto {
        public String type;
        public int startX, startY;
        public int endX, endY;
    }
}
