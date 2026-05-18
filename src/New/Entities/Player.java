package New.Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static New.Engine.Constants.Directions.*;
import static New.Engine.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false;
    private boolean up, down, left, right;

    public Player(float x, float y) {
        super(x, y);
        loadAnimationsFromImg();
    }

    public void update() {

        updatePosition();
        updateAniTick();
        setAnimation();
    }
    public void render(Graphics g) {
        BufferedImage subImg = animations[playerAction][aniIndex];
        g.drawImage(subImg, (int) x, (int) y, 128, 128, null);
    }

    // animation setters
    private void loadAnimationsFromImg() {

        InputStream inputStream = getClass().getResourceAsStream("/Imgs/playerSprites.png");
        BufferedImage img;

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

        animations = new BufferedImage[6][14];

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }
    private void updateAniTick() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction)) aniIndex = 0;
        }
    }
    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            switch (playerDirection) {
                case LEFT -> playerAction = RUNNING_LEFT;
                case UP -> playerAction = RUNNING_UP;
                case RIGHT -> playerAction = RUNNING_RIGHT;
                case DOWN -> playerAction = RUNNING_DOWN;
                default -> playerAction = RUNNING_RIGHT;
            }
        } else {
            playerAction = IDLE;
        }

//        if (napr pressButton) playerDirection = PRESS_BUTTON;

        if (startAni != playerAction) resetAniTick();
    }

    // moving
    public void updatePosition() {

        moving = false;

        if (up && !down) {
            y -= DELTA_MOVE_VALUE;
            playerDirection = UP;
            moving = true;
        }
        if (down && !up) {
            y += DELTA_MOVE_VALUE;
            playerDirection = DOWN;
            moving = true;
        }

        if (left && !right) {
            x -= DELTA_MOVE_VALUE;
            playerDirection = LEFT;
            moving = true;
        }
        if (right && !left) {
            x += DELTA_MOVE_VALUE;
            playerDirection = RIGHT;
            moving = true;
        }
    }

    // resets
    public void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }
    public void resetDirBooleans() {
        left = false;
        up = false;
        down = false;
        right = false;
    }

    // getters & setters
    public boolean isUp() {
        return up;
    }
    public void setUp(boolean up) {
        this.up = up;
    }
    public boolean isDown() {
        return down;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
