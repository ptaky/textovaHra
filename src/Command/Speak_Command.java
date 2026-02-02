package Command;

import Data.Game;

public class Speak_Command implements Command {
    private Game game;

    public Speak_Command(Game game) {
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
