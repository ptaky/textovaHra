package Command;
import Engine.Game;

public class Explore_Command implements Command {
    private Game game;

    public Explore_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        if (command != null) return game.getInvalidCommand();
        game.getCurrentRoom().setExplored(true);
        return game.getCurrentRoom().getAdvancedDescription();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
