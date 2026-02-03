package Command;
import Data.*;

public class Explore_Command implements Command {
    private Game game;

    public Explore_Command(Game game) {
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
