package Command;
import Data.*;

public class Help_Command implements Command {
    private Game game;

    public Help_Command(Game game) {
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
