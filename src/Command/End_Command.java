package Command;
import Engine.Game;

public class End_Command implements Command {
    private Game game;

    public End_Command(Game game) {
        this.game = game;
    }

    /**
     * Provede příkaz "konec".
     * @param command název předmětu
     * @return textová zpráva pro hráče
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.getInvalidCommand();
        game.quitGame();
        return "konec hry";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
