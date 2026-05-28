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

            // movement & inventory
            case KeyEvent.VK_W -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().getPlayer().setUp(true);
                } else if (gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().inventoryUp();
                }
            }
            case KeyEvent.VK_A -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().getPlayer().setLeft(true);
                } else if (gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().selectInventory();
                }
            }
            case KeyEvent.VK_S -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().getPlayer().setDown(true);
                } else if (gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().inventoryDown();
                }
            }
            case KeyEvent.VK_D -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().getPlayer().setRight(true);
                } else if (gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().selectGround();
                }
            }
            case KeyEvent.VK_SPACE -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().getRoomManager().tryTransition();
                } else if (gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().moveSelectedItem();
                }
            }

            // inventory
            case KeyEvent.VK_TAB -> gp.getGame().toggleInventory();
            case KeyEvent.VK_UP -> gp.getGame().inventoryUp();
            case KeyEvent.VK_DOWN -> gp.getGame().inventoryDown();
            case KeyEvent.VK_LEFT -> gp.getGame().selectInventory();
            case KeyEvent.VK_RIGHT -> gp.getGame().selectGround();

            // others
            case KeyEvent.VK_E -> {
                gp.getGame().getCurrentRoom().setExplored(true);
                gp.getGame().showPopup("prozkoumano");
            }
            case KeyEvent.VK_ESCAPE -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().pauseGame();
                } else if (gp.getGame().getGameState() == PAUSED || gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().setGameState(RUNNING);
                } else if (gp.getGame().getGameState() == DEFEATED || gp.getGame().getGameState() == VICTORY) {
                    gp.getGame().backToMenu();
                }
            }
            case KeyEvent.VK_R -> {
                if (gp.getGame().getGameState() == DEFEATED || gp.getGame().getGameState() == VICTORY) {
                    gp.getGame().restartGame();
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