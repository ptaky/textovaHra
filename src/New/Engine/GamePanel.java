package New.Engine;

import New.Inputs.KeyboardInputs;
import New.Inputs.MouseInputs;

import static New.Engine.Game.*;
import static New.Engine.Constants.PlayerConstants.*;
import static New.Engine.Constants.Directions.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {
        this.game = game;

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

    // getters & setters
    public Game getGame() {
        return game;
    }
}
