package Command;
import Engine.Game;

public class Take_Command implements Command {
    private Game game;

    public Take_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        command = command.toLowerCase();
        if (!game.getCurrentRoom().isExplored()) return "není co sebrat";
        if (game.getCurrentRoom().getItems().contains(command)) {
            game.getPlayer().getInventory().addItem(game.getPlayer().getInventory().findItemByName(command));
            game.getCurrentRoom().getItems().remove(command);
            return game.getMap();
        }
        return "není co sebrat";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
