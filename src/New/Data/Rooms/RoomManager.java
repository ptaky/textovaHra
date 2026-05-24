package New.Data.Rooms;

import New.Engine.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoomManager {

    private Game game;
    private BufferedImage levelSprite;

    public RoomManager(Game game) {
        this.game = game;
        this.levelSprite = null;

    }

    public void draw(Graphics g) {
        g.drawImage(levelSprite, 0, 0, null);
    }
    public void update() {

    }
}
