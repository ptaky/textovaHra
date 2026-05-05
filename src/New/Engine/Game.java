package New.Engine;

import New.Screens.GameScreen;

public class Game {
    GamePanel gp;
    GameScreen gs;

    public Game() {
        gp = new GamePanel();
        gs = new GameScreen(gp);

        gp.requestFocus();

        //TODO ISSUE nefunguje keylistener
    }
}
