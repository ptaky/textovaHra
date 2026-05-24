package New.Entities;

import New.Engine.Game;
import New.Data.DataLoader;
import Old.Data.Item;
import Old.Data.NPC;
import Old.Data.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

import static New.Data.Constants.Directions.*;
import static New.Data.Constants.PlayerConstants.*;
import static New.Engine.Game.*;

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

    public void update() {
        updatePosition();
        updateAniTick();
        setAnimation();
        checkHazardousZones();
    }

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

    private void checkHazardousZones() {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom != null && currentRoom.getId().equals("karantena")) {
            Item mask = game.getPlayerInventory().getItemById("plynova_maska");
            if (mask == null) {
                game.setPlayerLost(true);
            }
        }
    }

    public void tryPickUpItem() {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom == null || currentRoom.getItems().isEmpty()) {
            return;
        }

        if (game.getPlayerInventory().isFull()) {
            return;
        }

        String itemId = currentRoom.getItems().get(0);
        Item worldItem = game.getItems().get(itemId);

        if (worldItem != null) {
            game.getPlayerInventory().addItem(worldItem);
            currentRoom.removeItem(itemId);
        }
    }

    public void tryInteractWithNPC() {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom == null || currentRoom.getNpcs().isEmpty()) {
            return;
        }

        String npcId = currentRoom.getNpcs().get(0);
        NPC npc = game.getNPCs().get(npcId);

        if (npc != null) {
            if (npc.getId().equals("milan")) {
                Item pojistky = game.getPlayerInventory().getItemById("pojistky");
                if (pojistky != null) {
                    game.getPlayerInventory().removeItem(pojistky);
                    game.decreaseTimeLeft();
                    game.setCheckpoint(1);
                }
            }
        }
    }

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

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > Game.GAME_WIDTH - 32) x = Game.GAME_WIDTH - 32;
        if (y > Game.GAME_HEIGHT - 32) y = Game.GAME_HEIGHT - 32;
    }

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