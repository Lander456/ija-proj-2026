package view;

public class PlayerSetup {
    private final boolean redAI;
    private final boolean blueAI;

    public PlayerSetup(boolean redAI, boolean blueAI) {
        this.redAI = redAI;
        this.blueAI = blueAI;
    }

    public boolean getRedAI() { return redAI; }
    public boolean getBlueAI() { return blueAI; }
}
