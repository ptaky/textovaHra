package Command;
import Data.Inventory;
import Data.Item;
import Data.Room;
import Engine.Game;

public class Place_Command implements Command {
    private Game game;

    public Place_Command(Game game) {
        this.game = game;
    }

    /**
     * Provede příkaz "poloz".
     * @param command název předmětu
     * @return textová zpráva pro hráče
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();
        command = command.toLowerCase();
        Room room = game.getCurrentRoom();
        Item item = game.getItemById(command);
        Inventory inv = game.getPlayer().getInventory();

        if (!room.isExplored()) return "zkus to tu nejdřív prozkoumat";
        if (inv == null) return "není co položit";
        if (item != null) {
            room.addItem(command);
            inv.removeItem(item);
            return "polozil jsi " + item.getName();
        }
        return "není co položit";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
