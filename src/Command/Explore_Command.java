package Command;
import Engine.Game;

public class Explore_Command implements Command {
    private Game game;

    public Explore_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        game.getCurrentRoom().setExplored(false);
        return game.getCurrentRoom().getAdvancedDescription();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
