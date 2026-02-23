import Data.Inventory;
import Data.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for inventory behavior and input validation.
 * @author Ondřej Ptáček
 */
class Inventory_Test {

    @Test
    void addItemIgnoresNullAndRespectsCapacity() {
        Inventory inventory = new Inventory();
        Item a = new Item(); a.setId("a");
        Item b = new Item(); b.setId("b");
        Item c = new Item(); c.setId("c");

        inventory.addItem(null);
        assertTrue(inventory.isEmpty());

        inventory.addItem(a);
        inventory.addItem(b);
        inventory.addItem(c);

        assertTrue(inventory.isFull());
        assertNull(inventory.getItemById("d"));
    }

    @Test
    void getItemByIdReturnsNullForMissingOrNullId() {
        Inventory inventory = new Inventory();
        Item battery = new Item();
        battery.setId("baterie");
        inventory.addItem(battery);

        assertNull(inventory.getItemById(null));
        assertNull(inventory.getItemById("filtr"));
        assertEquals(battery, inventory.getItemById("baterie"));
    }
}