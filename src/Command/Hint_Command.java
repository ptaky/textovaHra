package Command;
import Engine.Game;

import java.util.Random;

/**
 * Command for giving player a hint.
 * @author Ondřej Ptáček
 */
public class Hint_Command implements Command {

    private final Game game;
    private final Random random = new Random();

    public Hint_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "napoveda".
     * @param command null
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.error(game.getInvalidCommand());

        int cp = game.getCheckpoint();

        return String.valueOf(game.getNPCs().get("spark").getHints().get(String.valueOf(cp)));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
