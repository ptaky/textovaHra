package New.Inputs;

import New.Engine.Game;
import New.Engine.GamePanel;

import static New.Engine.Constants.Directions.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    public GamePanel gp;

    public KeyboardInputs(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gp.getGame().getPlayer().setUp(true);
            case KeyEvent.VK_A -> gp.getGame().getPlayer().setLeft(true);
            case KeyEvent.VK_S -> gp.getGame().getPlayer().setDown(true);
            case KeyEvent.VK_D -> gp.getGame().getPlayer().setRight(true);

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
