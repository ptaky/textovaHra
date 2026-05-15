package New.Engine;

import New.Inputs.KeyboardInputs;
import New.Inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private int xDelta = 0, yDelta = 0;
    private int rectWidth = 100, rectHeight = 80;
    private BufferedImage img;

    public GamePanel() {

        importImg();

        addKeyListener(new KeyboardInputs(this));
        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImg() {

        InputStream inputStream = getClass().getResourceAsStream("/Imgs/character.png");

        try {
            img = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeDx(int value) {
        this.xDelta += value;
    }
    public void changeDy(int value) {
        this.yDelta += value;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

//        g.fillRect(((Game.width/2) - (rectWidth/2)) + xDelta, ((Game.height/2) - (rectHeight/2)) + yDelta, rectWidth, rectHeight);

        g.drawImage(img.getSubimage(0, 0,96, 96), xDelta, yDelta, 128, 128, null);

    }
}
