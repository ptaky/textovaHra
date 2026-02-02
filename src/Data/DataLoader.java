package Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class DataLoader {
    ObjectMapper parser;

    public void loadRoomsData(){
         parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/rooms.json");
            List<Room> rooms = parser.readValue(input, new TypeReference<List<Room>>() {});

        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadNPCData(){
        parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/NPC.json");
            List<NPC> NPCs = parser.readValue(input, new TypeReference<List<NPC>>() {});

        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadItemsData(){
        parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/items.json");
            List<Item> Items = parser.readValue(input, new TypeReference<List<Item>>() {});

        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
