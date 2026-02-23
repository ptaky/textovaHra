import Data.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for room safety and formatting behavior.
 * @author Ondřej Ptáček
 */
class Room_Test {

    @Test
    void containsItemIsSafeForNullCollectionsAndIds() {
        Room room = new Room();

        room.setItems(null);
        assertFalse(room.containsItem("baterie"));

        room.setItems(new ArrayList<>(List.of("baterie")));
        assertFalse(room.containsItem(null));
        assertTrue(room.containsItem("baterie"));
    }

    @Test
    void toStringShowsOnlyDescriptionWhenNotExplored() {
        Room room = new Room();
        room.setDescription("Zakladni popis");
        room.setItems(new ArrayList<>(List.of("baterie")));
        room.setNpcs(new ArrayList<>(List.of("spark")));
        room.setExplored(false);

        assertEquals("Zakladni popis", room.toString());
    }
}