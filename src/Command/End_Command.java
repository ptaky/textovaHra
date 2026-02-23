package Command;
import Engine.Game;

/**
 * Command for exit game.
 * @author Ondřej Ptáček
 */
public class End_Command implements Command {
    private Game game;

    public End_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "konec".
     * @param command null
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.error(game.getInvalidCommand());
        game.quitGame();
        return "konec hry";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
