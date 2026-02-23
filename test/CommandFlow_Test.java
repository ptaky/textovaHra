import Command.Take_Command;
import Command.Use_Command;
import Data.Item;
import Engine.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for command execution paths that are critical for game progression.
 * @author Ondřej Ptáček
 */
class CommandFlow_Test {

    @Test
    void takeCommandRejectsPickupInUnexploredRoom() {
        Game game = new Game();
        game.setup();
        game.setCurrentRoom(game.getRooms().get("lekarsky_trakt"));

        Take_Command take = new Take_Command(game);
        String result = take.execute("zrcatko");

        assertTrue(result.startsWith("ERROR:"));
        assertTrue(result.contains("prozkoumat"));
    }

    @Test
    void takeCommandAddsItemWhenRoomExplored() {
        Game game = new Game();
        game.setup();
        game.setCurrentRoom(game.getRooms().get("lekarsky_trakt"));
        game.getCurrentRoom().setExplored(true);

        Take_Command take = new Take_Command(game);
        String result = take.execute("zrcatko");

        assertTrue(result.contains("sebral jsi"));
        assertNotNull(game.getPlayer().getInventory().getItemById("zrcatko"));
        assertFalse(game.getCurrentRoom().containsItem("zrcatko"));
    }

    @Test
    void useBatteryRepairsSparkAndAdvancesCheckpoint() {
        Game game = new Game();
        game.setup();

        Item battery = game.getItemById("baterie");
        assertNotNull(battery);
        game.getPlayer().getInventory().addItem(battery);
        game.setCurrentRoom(game.getRooms().get("lekarsky_trakt"));

        Use_Command use = new Use_Command(game);
        String result = use.execute("baterie");

        assertFalse(result.startsWith("ERROR:"));
        assertTrue(game.getNPCs().get("spark").isRepaired());
        assertEquals(1, game.getCheckpoint());
        assertNull(game.getPlayer().getInventory().getItemById("baterie"));
    }
}