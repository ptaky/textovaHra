package Command;
import Engine.Game;

/**
 * Command for giving player all possible commands available.
 * @author Ondřej Ptáček
 */
public class Help_Command implements Command {
    private Game game;

    public Help_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "pomoc".
     * @param command null
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.error(game.getInvalidCommand());
        return
                "Můžeš použít tyto příkazy:\n" +
                "  - jdi 'mistnost', prozkoumej\n" +
                "  - vezmi 'predmet', poloz 'predmet', pouzij 'predmet'\n" +
                "  - mluv 'postava', napoveda (od Sparka)\n" +
                "  - pomoc, konec\n";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
