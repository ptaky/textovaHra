import Engine.Game;
import Engine.UserInterface;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for user input validation in command parsing.
 * @author Ondřej Ptáček
 */
class UserInterface_Test {

    static class CapturingUI extends UserInterface {
        String lastPrinted;

        @Override
        public void print(String input) {
            lastPrinted = input;
        }
    }

    private Game getGame(UserInterface ui) throws Exception {
        Field gameField = UserInterface.class.getDeclaredField("game");
        gameField.setAccessible(true);
        return (Game) gameField.get(ui);
    }

    @Test
    void commandExecuteRejectsNullAndEmptyInputs() throws Exception {
        CapturingUI ui = new CapturingUI();
        ui.hashmapsLoader();
        getGame(ui).setup();

        ui.commandExecute(null);
        assertEquals("ERROR: prikaz je prazdny", ui.lastPrinted);

        ui.commandExecute("   ");
        assertEquals("ERROR: prikaz je prazdny", ui.lastPrinted);
    }

    @Test
    void commandExecuteChecksArgumentCount() throws Exception {
        CapturingUI ui = new CapturingUI();
        ui.hashmapsLoader();
        getGame(ui).setup();

        ui.commandExecute("jdi");
        assertTrue(ui.lastPrinted.contains("vyzaduje presne 1 parametr"));

        ui.commandExecute("konec ted");
        assertTrue(ui.lastPrinted.contains("nebere zadny parametr"));
    }
}