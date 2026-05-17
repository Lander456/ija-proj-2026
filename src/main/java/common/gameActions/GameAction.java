package common.gameActions;

import tool.io.dto.GameConfig;

/**
 * Serves as an interface for all the GameActions.
 * @author Tadeas Topinka (xtopint00)
 */
public interface GameAction {
    /**
     * Executes any given action, performing its effects.
     */
    void execute();

    /**
     * Rewinds any given action, undoing its effects.
     */
    void undo();

    /**
     * Converts any action into a JSON serializable datatype.
     * @return JournalEntry, a datatype that can be serialized.
     */
    GameConfig.JournalEntry toJson();
}
