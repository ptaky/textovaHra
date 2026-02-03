package Command;
import Engine.Game;

public class End_Command implements Command {
    private Game game;

    public End_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        game.quitGame();
        return "konec hry";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
