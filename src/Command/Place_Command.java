package Command;
import Engine.Game;

public class Place_Command implements Command {
    private Game game;

    public Place_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        command = command.toLowerCase();
        if (game.getPlayer().getInventory().findItemByName(command) != null) {
            game.getCurrentRoom().addItem(command);
            game.getPlayer().getInventory().removeItem(game.getPlayer().getInventory().findItemByName(command));
            return game.getMap();
        }
        return game.getInvalidCommand();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
