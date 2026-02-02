package Data;

public class Player {
    private int id;
    private Inventory inventory;

    public Player(int id) {
        this.id = id;
        this.inventory = new Inventory();
    }

    public Inventory getInventory() {
        return inventory;
    }
}
