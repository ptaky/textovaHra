import Command.Go_Command;
import Engine.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for move command validation and room transitions.
 * @author Ondřej Ptáček
 */
class GoCommand_Test {

    @Test
    void executeMovesToAdjacentRoom() {
        Game game = new Game();
        game.setup();

        Go_Command go = new Go_Command(game);
        String result = go.execute("lekarsky_trakt");

        assertFalse(result.startsWith("ERROR:"));
        assertEquals("lekarsky_trakt", game.getCurrentRoom().getId());
    }
}