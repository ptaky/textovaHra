package Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class DataLoader {
    ObjectMapper parser;

    public List<Room> loadRoomsData(){
         parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/rooms.json");
            List<Room> rooms = parser.readValue(input, new TypeReference<List<Room>>() {});
            return rooms;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<NPC> loadNPCData(){
        parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/NPC.json");
            List<NPC> NPCs = parser.readValue(input, new TypeReference<List<NPC>>() {});
            return NPCs;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> loadItemsData(){
        parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/items.json");
            List<Item> Items = parser.readValue(input, new TypeReference<List<Item>>() {});
            return Items;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
