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
        addKeyListener(new KeyboardInputs(this));
        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }

    public void updateGame() {

    }

    public void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("game size : " + GAME_WIDTH + " x " + GAME_HEIGHT);
    }

    // getters & setters
    public Game getGame() {
        return game;
    }
}
