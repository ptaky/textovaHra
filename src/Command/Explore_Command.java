package Command;
import Engine.Game;

public class Explore_Command implements Command {
    private Game game;

    public Explore_Command(Game game) {
        this.game = game;
    }

    /**
     * Provede příkaz "prozkoumej".
     * @param command název předmětu
     * @return textová zpráva pro hráče
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.getInvalidCommand();
        game.getCurrentRoom().setExplored(true);
        return game.getCurrentRoom().getAdvancedDescription();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
