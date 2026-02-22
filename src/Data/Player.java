package Data;

public class Player {
    private Inventory inventory;

    public Player() {
        this.inventory = new Inventory();
    }

    // getter
    public Inventory getInventory() {
        return inventory;
    }
}
