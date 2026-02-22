package Command;
import Data.Room;
import Engine.Game;

public class Go_Command implements Command {
    private Game game;

    public Go_Command(Game game) {
        this.game = game;
    }

    /**
     * Provede příkaz "jdi".
     * @param command název předmětu
     * @return textová zpráva pro hráče
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        command = command.toLowerCase();
        Room room = game.getCurrentRoom();

        if (room.getId().equals(command)) return "to nejde, tam se ted nachazis";
        if (game.getRooms().containsKey(command) && !room.getNextRooms().contains(command)) return "tam to teď nepůjde, musíš tam dojít postupně";
        if (command.equals("karantena") && game.getPlayer().getInventory().getItemById("plynova_maska") == null) return "Bez plynové masky sem nemůžeš vstoupit!";
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
