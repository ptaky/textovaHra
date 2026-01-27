import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class DataLoader {
    public void loadWorldData(){
        ObjectMapper parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/world.json");
            List<Room> rooms = parser.readValue(input, new TypeReference<List<Room>>() {});

        } catch (FileNotFoundException e) {
            throw new RuntimeException("file was not found");
        } catch (Exception e) {
            throw new RuntimeException("error ");
        }
    }
}
