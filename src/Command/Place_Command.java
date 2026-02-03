package Command;
import Engine.Game;

public class Place_Command implements Command {
    private Game game;

    public Place_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        //TODO dodelat
        return "";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
