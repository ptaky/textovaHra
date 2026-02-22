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
        if (command == null) return game.error(game.getInvalidCommand());
        if (!game.getRooms().containsKey(command)) return game.error(game.getInvalidCommand());
        command = command.toLowerCase();
        Room room = game.getCurrentRoom();

        //TODO blbne karantena
        if (room.getId().equals(command)) return game.error("to nejde, tam se ted nachazis");
        if (command.equals("karantena") && game.getPlayer().getInventory().getItemById("plynova_maska") == null) return game.error("tam to teď nepůjde, musíš tam dojít postupně");
        if (game.getRooms().get(command).isLocked()) return game.error("tahle místnost je zamčená");
        if (game.getRooms().containsKey(command) && !room.getNextRooms().contains(command)) return game.error("tam to teď nepůjde, musíš tam dojít postupně");
        if (room.getNextRooms().contains(command)) {
            game.setCurrentRoom(game.getRooms().get(command));
            return game.getMap(false);
        }
        return game.error(game.getInvalidCommand());
    }

    @Override
    public boolean exit() {
        return false;
    }
}
