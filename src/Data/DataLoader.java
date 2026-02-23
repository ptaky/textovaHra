package Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Loader for all data.
 * @author Ondřej Ptáček
 */
public class DataLoader {
    ObjectMapper parser;

    /**
     * Loads room data from the rooms.json file.
     * @return HashMap with rooms, where the key is the room ID
     */
    public HashMap<String, Room> loadRoomsData() {
        parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/rooms.json");
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
            InputStream input = new FileInputStream("resource/NPC.json");
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
            InputStream input = new FileInputStream("resource/items.json");
            HashMap<String, Item> items = parser.readValue(input, new TypeReference<HashMap<String, Item>>() {});
            return items;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}