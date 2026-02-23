package Command;
import Data.Inventory;
import Data.Item;
import Data.Room;
import Engine.Game;

/**
 * Command for placing item in current room.
 * @author Ondřej Ptáček
 */
public class Place_Command implements Command {
    private Game game;

    public Place_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "poloz".
     * @param command item that player wants to place in the current room
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        if (!game.getCurrentRoom().isExplored()) return game.error("nejdřív to tu musíš prozkoumat");
        command = command.toLowerCase();
        Room room = game.getCurrentRoom();
        Item item = game.getItemById(command);
        Inventory inv = game.getPlayer().getInventory();

        if (inv == null || !inv.contains(item)) return game.error("není co položit");
        if (item != null) {
            room.addItem(command);
            inv.removeItem(item);
            return "polozil jsi " + item.getName();
        }
        return game.error("není co položit");
    }

    @Override
    public boolean exit() {
        return false;
    }
}
