package Command;
import Data.Room;
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
        Room room = game.getCurrentRoom();

        if (command.equals("karantena") && game.getPlayer().getInventory().findItemById("plynova_maska") == null) return "Bez plynové masky sem nemůžeš vstoupit!";
        if (room.getId().equals(command)) return "to nejde, tam se ted nachazis";
        if (room.getNextRooms().contains(command)) {
            game.setCurrentRoom(game.getRooms().get(command));
            return game.getMap(false);
        }
        return game.getInvalidCommand();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
