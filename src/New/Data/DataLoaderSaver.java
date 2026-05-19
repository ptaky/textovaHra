package New.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class DataLoaderSaver {

    public static final String PLAYER_SPRITES = "playerSprites.png";
    public static final String ROOM_SPRITES = "roomSprites.png";

    public static BufferedImage loadImage(String filename) {
        BufferedImage img;
        img = null;

        InputStream inputStream = DataLoaderSaver.class.getResourceAsStream("/Imgs/" + filename);

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

        return img;
    }
}
