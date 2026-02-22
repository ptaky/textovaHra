package Command;
import Data.Inventory;
import Data.Item;
import Data.Room;
import Engine.Game;

public class Take_Command implements Command {
    private Game game;

    public Take_Command(Game game) {
        this.game = game;
    }

    /**
     * Provede příkaz "vezmi".
     * @param command název předmětu
     * @return textová zpráva pro hráče
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        if (!game.getCurrentRoom().isExplored()) return "nejdřív to tu musíš prozkoumat";
        if (!game.roomContains(command)) return "takový předmět tu není";
        command = command.toLowerCase();
        Room room = game.getCurrentRoom();
        if (room.containsItem(command)) return game.getInvalidCommand();
        Item item = game.getItemById(command);
        Inventory inv = game.getPlayer().getInventory();

        if (room.getItems() == null) return "není co sebrat";
        if (inv.isFull()) return "plný invetnář";
        if (item != null) {
            inv.addItem(item);
            room.getItems().remove(command);
            return "sebral jsi " + item.getName();
        }
        return "není co sebrat";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
