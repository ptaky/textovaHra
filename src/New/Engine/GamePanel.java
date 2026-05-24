package New.Engine;

import New.Inputs.KeyboardInputs;
import New.Inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static New.Engine.Game.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {
        this.game = game;

        setPanelSize();

        // Zajištění focusu pro zpracování klávesových vstupů
        setFocusable(true);
        addKeyListener(new KeyboardInputs(this));

        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vyčištění obrazovky černou barvou (vesmírná stanice Boreas)
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Vykreslení celé hry prostřednictvím Game
        game.render(g);
    }

    public void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        System.out.println("Herní okno inicializováno: " + GAME_WIDTH + " x " + GAME_HEIGHT);
    }

    public Game getGame() {
        return game;
    }
}