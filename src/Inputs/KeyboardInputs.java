package Inputs;

import Engine.GamePanel;
import Entities.NPCEntity;
import Data.NPC;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static Data.Constants.GameStates.*;

public class KeyboardInputs implements KeyListener {

    private GamePanel gp;

    public KeyboardInputs(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // --- ABSOLUTNÍ STOPKA PRO LOADING SCREEN ---
        if (gp.getGame().getGameState() == LOADING) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (gp.getGame().isLoadingFinished()) {
                    gp.getGame().setGameState(RUNNING);
                }
            }
            return; // Dokud se načítá, ostatní klávesy kompletně ignorujeme
        }

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
            case KeyEvent.VK_TAB -> {
                if (gp.getGame().getGameState() == RUNNING || gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().toggleInventory();
                }
            }
            case KeyEvent.VK_UP -> gp.getGame().inventoryUp();
            case KeyEvent.VK_DOWN -> gp.getGame().inventoryDown();
            case KeyEvent.VK_LEFT -> gp.getGame().selectInventory();
            case KeyEvent.VK_RIGHT -> gp.getGame().selectGround();

            // others
            case KeyEvent.VK_E -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().getCurrentRoom().setExplored(true);
                    gp.getGame().showPopup("prozkoumano");
                } else if (gp.getGame().getGameState() == INVENTORY) {
                    gp.getGame().useItem();
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().pauseGame();
                } else if (gp.getGame().getGameState() == PAUSED || gp.getGame().getGameState() == INVENTORY || gp.getGame().getGameState() == HELP) {
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
            case KeyEvent.VK_F -> {
                if (gp.getGame().getGameState() == RUNNING) {

                    NPCEntity nearbyEntity = gp.getGame().getNearbyNPC();

                    if (nearbyEntity == null) {
                        gp.getGame().showPopup("Nikdo tu není.");
                        return;
                    }

                    NPC npc = nearbyEntity.getNpc();

                    if (npc != null) {
                        gp.getGame().interactWithNPC(npc);
                    } else {
                        System.out.println("Chyba: NPCEntity existuje, ale chybí jí data z JSONu!");
                    }
                }
            }

            // --- KLÁVESA PRO POMOC / OVLÁDÁNÍ ---
            case KeyEvent.VK_H -> {
                if (gp.getGame().getGameState() == RUNNING) {
                    gp.getGame().setGameState(HELP);
                } else if (gp.getGame().getGameState() == HELP) {
                    gp.getGame().setGameState(RUNNING);
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