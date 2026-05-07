package common.enums;

public enum Players {
    RED("Player1"),
    BLUE("Player2"),
    NEUTRAL("neutral");

    private final String label;
    Players(String label) { this.label = label; }

    public static Players fromString(String text) {
        for (Players p : Players.values()) {
            if (p.label.equalsIgnoreCase(text) || p.toString().equalsIgnoreCase(text)) {
                return p;
            }
        }
        return NEUTRAL;
    }
}
