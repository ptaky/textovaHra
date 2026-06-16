package Engine;

import Inputs.KeyboardInputs;

import javax.swing.*;
import java.awt.*;

import static Engine.Game.*;

/**
 * Handles the game canvas rendering and user input capturing.
 * Inherits from JPanel to support double-buffered painting operations.
 * * @author Ondřej Ptáček
 */
public class GamePanel extends JPanel {

    private Game game;

    /**
     * Initializes the game panel, presets window dimensions and registers inputs.
     * * @param game main game engine coordinator reference
     */
    public GamePanel(Game game) {
        this.game = game;

        setPanelSize();

        setFocusable(true);
        addKeyListener(new KeyboardInputs(this));
        setFocusTraversalKeysEnabled(false);
    }

    /**
     * Standard paint component loop overriden from JPanel.
     * Clears background and triggers main entity rendering.
     * * @param g the Graphics context instance to protect and draw with
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        game.render(g);
    }

    /**
     * Sets preferred, minimum and maximum sizes for layout container constraints.
     */
    public void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        System.out.println("Herní okno inicializováno: " + GAME_WIDTH + " x " + GAME_HEIGHT);
    }

    /**
     * Gets the main core game state controller class wrapper instance.
     * * @return active Game session engine instance
     */
    public Game getGame() {
        return game;
    }
}