import Data.Item;
import Engine.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for core game state and utility methods.
 * @author Ondřej Ptáček
 */
class Game_Test {

    @Test
    void setupInitializesExpectedState() {
        Game game = new Game();
        game.setup();

        assertNotNull(game.getRooms());
        assertNotNull(game.getCurrentRoom());
        assertEquals("kryokomora", game.getCurrentRoom().getId());
        assertFalse(game.isGameOver());
        assertEquals(0, game.getCheckpoint());
    }

    @Test
    void getItemByIdHandlesNullAndUnknownItem() {
        Game game = new Game();
        game.setup();

        assertNull(game.getItemById(null));
        assertNull(game.getItemById("neexistuje"));

        Item known = game.getItemById("baterie");
        assertNotNull(known);
        assertEquals("baterie", known.getId());
    }
}