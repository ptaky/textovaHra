package New.Data.Rooms;

import New.Engine.Game;

import java.awt.image.BufferedImage;

public class RoomManager {

    private Game game;
    private BufferedImage levelSprite;

    public RoomManager(Game game) {
        this.game = game;
        this.levelSprite = null;

    }
}
