package Command;
import Engine.Game;

public class Go_Command implements Command {
    private Game game;

    public Go_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        command = command.toLowerCase();
        if (game.getCurrentRoom().getId().equals(command)) return "to nejde, tam se ted nachazis";
        if (game.getCurrentRoom().getNextRooms().contains(command)) {
            game.setCurrentRoom(game.getRooms().get(command));
            return game.getMap();
        }
        return game.getInvalidCommand();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
