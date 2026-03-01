package Command;
import Engine.Game;

/**
 * Command for explore current room.
 * @author Ondřej Ptáček
 */
public class Explore_Command implements Command {
    private Game game;

    public Explore_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "prozkoumej".
     * @param command null
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.error(game.getInvalidCommand());
        game.getCurrentRoom().setExplored(true);
        return game.getCurrentRoom().toString();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
