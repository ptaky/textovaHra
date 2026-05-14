package New.Engine;

import New.Inputs.KeyboardInputs;
import New.Inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private int xDelta = 0, yDelta = 0;
    private int rectWidth = 100, rectHeight = 80;

    public GamePanel() {

        addKeyListener(new KeyboardInputs(this));
        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeDx(int value) {
        this.xDelta += value;
    }
    public void changeDy(int value) {
        this.yDelta += value;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(((Game.width/2) - (rectWidth/2)) + xDelta, ((Game.height/2) - (rectHeight/2)) + yDelta, rectWidth, rectHeight);

    }
}
