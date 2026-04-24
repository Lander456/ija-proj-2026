package game;

/**
 * This is the GameFactory, used to create new Game objects
 * @author Tadeas Topinka (xtopint00)
 */
public abstract class GameFactory {
    /**
     * Creates a new game object
     * @param mapDef a 2D array representing the map to use for the Game
     * @return new Game object
     * @author Tadeas Topinka (xtopint00)
     */
    public static Game createGame(String[] mapDef) {
        return new Game(mapDef);
    }
}
