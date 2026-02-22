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

    // Gettery a settery
    public void addItem(Item item) {
        this.items.add(item);
    }
    public void removeItem(Item item) {
        this.items.remove(item);
    }
    public boolean contains(Item item) {
        return this.items.contains(item);
    }
    public boolean isFull() {
        return items.size() == size;
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Najde předmět v inventáři podle jeho ID.
     * @param itemId ID hledaného předmětu
     * @return nalezený předmět nebo null, pokud neexistuje
     */
    public Item getItemById(String itemId) {
        for (Item item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (items.size() == 1) return "[" + items.get(0) + ", - , - ]";
        if (items.size() == 2) return "[" + items.get(0) + ", " + items.get(1) +", - ]";
        if (items.size() == 3) return "[" + items.get(0) + ", " + items.get(1) + ", " + items.get(2) + "]";
        return items.toString();
    }
}
