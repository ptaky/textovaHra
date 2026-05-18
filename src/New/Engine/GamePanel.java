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
    private int xDelta = 0, yDelta = 0;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false;

    public GamePanel() {

        importAnimationImg();
        loadAnimationsFromImg();

        addKeyListener(new KeyboardInputs(this));
        mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage subImg = animations[playerAction][aniIndex];
        g.drawImage(subImg, xDelta, yDelta, 128, 128, null);

//      ((WIDTH/2) - (subImg.getWidth()/2)) + xDelta, ((HEIGHT/2) - (subImg.getHeight()/2)) + yDelta
    }

    public void updateGame() {

        animationLoop();
        chooseAnimation();
        movePosition();
    }

    // animations -----------------------------------------------
    private void loadAnimationsFromImg() {

        animations = new BufferedImage[6][14];

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }
    private void animationLoop() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction)) aniIndex = 0;
        }
    }
    private void chooseAnimation() {

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
    private void importAnimationImg() {

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
    public void movePosition() {

        if (moving) {
            switch (playerDirection) {
                case LEFT -> xDelta -= DELTA_MOVE_VALUE;
                case UP -> yDelta -= DELTA_MOVE_VALUE;
                case RIGHT -> xDelta += DELTA_MOVE_VALUE;
                case DOWN -> yDelta += DELTA_MOVE_VALUE;
            }
        }
    }

}
