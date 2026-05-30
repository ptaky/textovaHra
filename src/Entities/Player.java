package Entities;

import Engine.Game;
import Data.DataLoader;
import Data.Item;
import Data.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Data.Constants.Directions.*;
import static Data.Constants.PlayerConstants.*;
import static Engine.Game.SCALE;

/**
 * Represents the playable astronaut character Elara.
 * Manages character spatial positioning, sub-image animation frames parsing,
 * directional state updates, viewport edge constraints, and hazardous room checks.
 * @author Ondřej Ptáček
 */
public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false;
    private boolean up, down, left, right;

    public static final int PLAYER_SIZE = 128*(int)SCALE;

    private final float DELTA_MOVE_VALUE = 2.0f*(int)SCALE;
    private Game game;

    /**
     * Initializes the player avatar container with starting spawn coordinates.
     * Allocates memory for sprite coordinates array matrices.
     * @param x global horizontal layout spawn point coordinates
     * @param y global vertical layout spawn point coordinates
     * @param game reference hook to the running state engine coordinator instance
     */
    public Player(float x, float y, Game game) {
        super(x, y);

        this.game = game;
        this.animations = new BufferedImage[6][14];

        try {
            loadAnimationsFromImg();
        } catch (Exception e) {
            System.err.println("Fallback entity activation.");
        }
    }

    /**
     * Updates player components layer sequentially.
     * Dispatches spatial delta recalculations, ticks visual state indexes, and runs environmental checks.
     */
    public void update() {
        updatePosition();
        updateAniTick();
        setAnimation();
        checkHazardousZones();
    }

    /**
     * Draws the active frame model buffer segment onto the visible graphics context workspace panel.
     * Displays a distinct vector oval outline preview container if the spreadsheet resource is missing.
     * @param g current Graphics context container instance
     */
    public void render(Graphics g) {
        if (animations == null || animations[playerAction][aniIndex] == null) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.RED);
            g2d.fillOval((int) x, (int) y, 48, 48);

            g2d.setColor(Color.CYAN);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval((int) x, (int) y, 48, 48);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Courier New", Font.BOLD, 14));
            g2d.drawString("ELARA (" + (int)x + "," + (int)y + ")", (int) x - 20, (int) y - 10);

        } else {
            BufferedImage subImg = animations[playerAction][aniIndex];
            g.drawImage(subImg, (int) x, (int) y, PLAYER_SIZE, PLAYER_SIZE, null);

        }
    }

    /**
     * Clamp function wrapper testing if bounding boundaries are crossing window frame edge dimensions.
     */
    private void checkHitbox() {

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + PLAYER_SIZE > Game.GAME_WIDTH) x = Game.GAME_WIDTH - PLAYER_SIZE;
        if (y + PLAYER_SIZE > Game.GAME_HEIGHT) y = Game.GAME_HEIGHT - PLAYER_SIZE;
    }

    /**
     * Verifies if the astronaut entered quarantine zones without the required gas mask equipped.
     * Triggers defeat game state immediately if criteria match.
     */
    private void checkHazardousZones() {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom != null && currentRoom.getId().equals("karantena")) {
            Item mask = game.getPlayerInventory().getItemById("plynova_maska");
            if (mask == null) {
                game.setPlayerLost(true);
            }
        }
    }

    /**
     * Slices the master raw asset spreadsheet into a separate multi-row animation frame buffer database.
     */
    private void loadAnimationsFromImg() {
        try {
            BufferedImage img = DataLoader.loadImage(DataLoader.PLAYER_SPRITES);
            if (img != null) {
                int imgWidth = img.getWidth();
                int imgHeight = img.getHeight();

                for (int j = 0; j < animations.length; j++) {
                    for (int i = 0; i < animations[j].length; i++) {
                        int startX = i * 32;
                        int startY = j * 32;

                        if (startX + 32 <= imgWidth && startY + 32 <= imgHeight) {
                            animations[j][i] = img.getSubimage(startX, startY, 32, 32);
                        } else {
                            animations[j][i] = null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Animations slice failed.");
        }
    }

    /**
     * Steps internal numerical timing variables and cycles row sequence indices when threshold targets are met.
     */
    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction)) {
                aniIndex = 0;
            }
        }
    }

    /**
     * Selects and flags the operational active running loop action category based on move key bindings.
     */
    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            switch (playerDirection) {
                case UP -> playerAction = RUNNING_UP;
                case DOWN -> playerAction = RUNNING_DOWN;
                case LEFT -> playerAction = RUNNING_LEFT;
                case RIGHT -> playerAction = RUNNING_RIGHT;
            }
        } else {
            playerAction = IDLE;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    /**
     * Computes raw coordinate adjustments based on state flags and active velocity constants.
     */
    public void updatePosition() {
        moving = false;
        if (!up && !down && !left && !right) return;

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

        checkHitbox();

    }

    /**
     * Clears frame sequencing counters to prevent rendering skips across transitions.
     */
    public void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Enforces explicit false variable assignment on directional control states to stop ongoing physics displacement.
     */
    public void resetDirBooleans() {
        left = false;
        up = false;
        down = false;
        right = false;
    }

    public boolean isUp() { return up; }
    public void setUp(boolean up) { this.up = up; }
    public boolean isDown() { return down; }
    public void setDown(boolean down) { this.down = down; }
    public boolean isLeft() { return left; }
    public void setLeft(boolean left) { this.left = left; }
    public boolean isRight() { return right; }
    public void setRight(boolean right) { this.right = right; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}