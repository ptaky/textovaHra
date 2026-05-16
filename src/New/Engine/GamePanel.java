package New.Engine;

import New.Inputs.KeyboardInputs;
import New.Inputs.MouseInputs;

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
    private int xDelta = 0, yDelta = 0;
    private int rectWidth = 100, rectHeight = 80;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int idleAniLength = 14, runningRightAniLength, runningLeftAniLength;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false;

    public GamePanel() {

        importImg();
        loadAnimations();

        addKeyListener(new KeyboardInputs(this));
        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateAnimation();
        setAnimation();
        updatePosition();

        g.drawImage(animations[playerAction][aniIndex], xDelta, yDelta, 128, 128, null);

//        g.fillRect(((Game.width/2) - (rectWidth/2)) + xDelta, ((Game.height/2) - (rectHeight/2)) + yDelta, rectWidth, rectHeight);
    }

    // animations -----------------------------------------------
    private void loadAnimations() {

        animations = new BufferedImage[6][14];

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }
    private void updateAnimation() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction)) aniIndex = 0;
        }
    }
    private void setAnimation() {

        if (moving) {
            switch (playerDirection) {
                case LEFT -> playerAction = RUNNING_LEFT;
                case UP -> playerAction = RUNNING_UP;
                case RIGHT -> playerAction = RUNNING_RIGHT;
                case DOWN -> playerAction = RUNNING_DOWN;
            }
        } else {
            playerAction = IDLE;
        }
    }

    // importing -----------------------------------------------
    private void importImg() {

        InputStream inputStream = getClass().getResourceAsStream("/Imgs/playerSprites.png");

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

    // movement -----------------------------------------------
    public void setDirection(int direction) {
        this.playerDirection = direction;
        moving = true;
    }
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    public void updatePosition() {

        if (moving) {
            switch (playerDirection) {
                case LEFT -> xDelta -= 5;
                case UP -> yDelta -= 5;
                case RIGHT -> xDelta += 5;
                case DOWN -> yDelta += 5;
            }
        }
    }

}
