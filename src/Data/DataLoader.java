package Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class DataLoader {
    ObjectMapper parser;

    /**
     * Načte data o místnostech ze souboru rooms.json.
     * @return HashMap s místnostmi, kde klíč je ID místnosti
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
     * Načte data o NPC ze souboru NPC.json.
     * @return HashMap s NPC, kde klíč je ID NPC
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
     * Načte data o předmětech ze souboru items.json.
     * @return List s předměty ve hře
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