package Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Loader for all data.
 * @author Ondřej Ptáček
 */
public class DataLoader {

    ObjectMapper parser;

    public static final String PLAYER_SPRITES = "playerSprites.png";
    public static final String SPARK_SPRITES = "sparkSprites.png";
    public static final String MILAN_SPRITES = "milanSprites.png";
    public static final String BABICKA_SPRITES = "babickaSprites.png";
    public static final String VIKTOR_SPRITES = "viktorSprites.png";


    public static BufferedImage loadImage(String filename) {
        BufferedImage img;
        img = null;

        InputStream inputStream = DataLoader.class.getResourceAsStream("/Imgs/" + filename);

        if (inputStream == null) {
            System.out.println("obrazek " + filename + " se nenacetl");
            return null;
        }

        try {
            img = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return img;
    }

    /**
     * Loads room data from the rooms.json file.
     * @return HashMap with rooms, where the key is the room ID
     */
    public HashMap<String, Room> loadRoomsData() {
        parser = new ObjectMapper();

        try {
            InputStream input = DataLoader.class.getClassLoader().getResourceAsStream("Jsons/rooms.json");
            HashMap<String, Room> rooms = parser.readValue(input, new TypeReference<HashMap<String, Room>>() {});
            return rooms;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads NPC data from the NPC.json file.
     * @return HashMap with NPCs, where the key is the NPC ID
     */
    public HashMap<String, NPC> loadNPCData() {
        parser = new ObjectMapper();

        try {
            InputStream input = DataLoader.class.getClassLoader().getResourceAsStream("Jsons/NPC.json");
            HashMap<String, NPC> NPCs = parser.readValue(input, new TypeReference<HashMap<String, NPC>>() {});
            return NPCs;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads item data from the items.json file.
     * @return HashMap with items in the game
     */
    public HashMap<String, Item> loadItemsData() {
        parser = new ObjectMapper();

        try {
            InputStream input =  DataLoader.class.getClassLoader().getResourceAsStream("Jsons/items.json");
            HashMap<String, Item> items = parser.readValue(input, new TypeReference<HashMap<String, Item>>() {});
            return items;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
