package tool.io.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public class GameConfig {
    public String[][] terrain;
    public List<OwnershipEntry> ownership;
    public List<UnitEntry> units;
    public List<JournalEntry> journal;
    public Metadata metadata;

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

    public static class Metadata {
        public boolean redAI;
        public boolean blueAI;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "actionType"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = MoveActionJson.class, name = "MOVE"),
            @JsonSubTypes.Type(value = AttackActionJson.class, name = "ATTACK"),
            @JsonSubTypes.Type(value = BuildActionJson.class, name = "BUILD"),
            @JsonSubTypes.Type(value = CaptureActionJson.class, name = "CAPTURE"),
            @JsonSubTypes.Type(value = EndTurnActionJson.class, name = "END_TURN"),
    })
    public static abstract class JournalEntry {
        public String actionType;
    }

    public static class AttackActionJson extends JournalEntry {
        public int attackerX;
        public int attackerY;
        public int defenderX;
        public int defenderY;
        public int attackerPrevHp;
        public int defenderPrevHp;
    }

    public static class BuildActionJson extends JournalEntry {
        public int unitCost;
        public int x;
        public int y;
        public String unitType;
    }

    public static class CaptureActionJson extends JournalEntry {
        public int x;
        public int y;
        public String prevOwner;
        public int prevResistance;
    }

    public static class EndTurnActionJson extends JournalEntry {
        public int prevFunds;
        public String prevActive;
    }

    public static class MoveActionJson extends JournalEntry {
        public int startX;
        public int startY;
        public int endX;
        public int endY;
    }
}
