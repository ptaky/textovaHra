package Command;
import Data.Room;
import Engine.Game;

/**
 * Command for go from current room to the next one.
 * @author Ondřej Ptáček
 */
public class Go_Command implements Command {
    private Game game;

    public Go_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "jdi".
     * @param command room that player wants to go in
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.error(game.getInvalidCommand());
        if (!game.getRooms().containsKey(command)) return game.error(game.getInvalidCommand());
        command = command.toLowerCase();
        Room room = game.getCurrentRoom();

        if (room.getId().equals(command)) return game.error("to nejde, tam se ted nachazis");
        if (game.getRooms().get(command).isLocked()) return game.error("tahle místnost je zamčená");
        if (game.getRooms().containsKey(command) && !room.getNextRooms().contains(command)) return game.error("tam to teď nepůjde, musíš tam dojít postupně");
        if ((command.equals("vysilaci_vez") || command.equals("serverovna")) && game.getNPCs().get("viktor").isHostile()) return game.error("Viktor te tam nepusti.");

        if (command.equals("karantena") && game.getPlayer().getInventory().getItemById("plynova_maska") == null) {
            game.setPlayerLost(true);
            return "";
        }

        if (room.getNextRooms().contains(command)) {
            game.setCurrentRoom(game.getRooms().get(command));
            return
                    game.roomInfo() + '\n' + game.getMap(false, game.getCurrentRoom().getId());
        }
        return game.error(game.getInvalidCommand());
    }

    @Override
    public boolean exit() {
        return false;
    }
}
