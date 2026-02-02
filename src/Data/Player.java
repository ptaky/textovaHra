package Data;

public class Player {
    private Inventory inventory;

    public Player() {
        this.inventory = new Inventory();
    }

    public Inventory getInventory() {
        return inventory;
    }
}
