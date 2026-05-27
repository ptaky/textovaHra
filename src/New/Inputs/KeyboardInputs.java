package New.Inputs;

import New.Engine.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static New.Data.Constants.GameStates.*;

public class KeyboardInputs implements KeyListener {

    private GamePanel gp;

    public KeyboardInputs(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gp.getGame().getPlayer().setUp(true);
            case KeyEvent.VK_A -> gp.getGame().getPlayer().setLeft(true);
            case KeyEvent.VK_S -> gp.getGame().getPlayer().setDown(true);
            case KeyEvent.VK_D -> gp.getGame().getPlayer().setRight(true);

            case KeyEvent.VK_E -> gp.getGame().getCurrentRoom().setExplored(true);
            case KeyEvent.VK_SPACE -> gp.getGame().getRoomManager().tryTransition();
            case KeyEvent.VK_ESCAPE -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().setGameState(PAUSED);
                    gp.getGame().pauseGame();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gp.getGame().getPlayer().setUp(false);
            case KeyEvent.VK_A -> gp.getGame().getPlayer().setLeft(false);
            case KeyEvent.VK_S -> gp.getGame().getPlayer().setDown(false);
            case KeyEvent.VK_D -> gp.getGame().getPlayer().setRight(false);
        }
    }
}