package New.Inputs;

import New.Engine.Game;
import New.Engine.GamePanel;

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
            case KeyEvent.VK_W -> W_Pressed();
            case KeyEvent.VK_A -> A_Pressed();
            case KeyEvent.VK_S -> S_Pressed();
            case KeyEvent.VK_D -> D_Pressed();

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void W_Pressed() {
        gp.changeDy(-Game.deltaMoveValue);
    }
    public void A_Pressed() {
        gp.changeDx(-Game.deltaMoveValue);
    }
    public void S_Pressed() {
        gp.changeDy(Game.deltaMoveValue);
    }
    public void D_Pressed() {
        gp.changeDx(Game.deltaMoveValue);
    }
}
