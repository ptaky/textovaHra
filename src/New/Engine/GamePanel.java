package New.Engine;

import New.Inputs.KeyboardInputs;
import New.Inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private int Dx = 0;
    private int Dy = 0;

    public GamePanel() {

        addKeyListener(new KeyboardInputs(this));
        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeDx(int value) {
        this.Dx += value;
    }
    public void changeDy(int value) {
        this.Dy += value;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(350 + Dx, 100 + Dy, 60, 80);

        repaint();
    }
}
