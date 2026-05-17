package common.enums;

/**
 * Defines an enum with the two different players that can be present in each game along with their String labels.
 * @author Tadeas Topinka (xtopint00)
 */
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

    public String getLabel() { return label; }
}
