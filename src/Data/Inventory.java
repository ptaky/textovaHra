package Data;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int size;
    private List<Item> items;

    public Inventory() {
        this.size = 3;
        this.items = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
    public void removeItem(Item item) {
        this.items.remove(item);
    }
    public boolean contains(Item item) {
        return items.contains(item);
    }
    public boolean isFull() {
        return items.size() == size;
    }
}
