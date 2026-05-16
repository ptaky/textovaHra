package New.Screens;

import New.Engine.Game;
import New.Engine.GamePanel;

import javax.swing.*;

public class GameScreen {
    private JFrame frame;

    public GameScreen(GamePanel gp) {
        frame = new JFrame();

        init(gp);
    }

    public void init(GamePanel gp) {
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gp);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
