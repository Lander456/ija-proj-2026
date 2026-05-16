package common.gameActions;

import tool.io.dto.GameConfig;

public interface GameAction {
    void execute();
    void undo();
    GameConfig.JournalEntry toJson();
}
