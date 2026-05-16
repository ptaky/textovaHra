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
            case KeyEvent.VK_W -> W_Pressed();
            case KeyEvent.VK_A -> A_Pressed();
            case KeyEvent.VK_S -> S_Pressed();
            case KeyEvent.VK_D -> D_Pressed();

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                setIdle();

        }
    }

    // helpers -----------------------------------------------
    public void W_Pressed() {
        gp.setDirection(UP);
    }
    public void A_Pressed() {
        gp.setDirection(LEFT);
    }
    public void S_Pressed() {
        gp.setDirection(DOWN);
    }
    public void D_Pressed() {
        gp.setDirection(RIGHT);
    }
    public void setIdle() {
        gp.setMoving(false);
    }
}
