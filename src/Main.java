import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        ObjectMapper parser = new ObjectMapper();

        try {
            InputStream input = new FileInputStream("resource/NPC.json");
            NPC npc = parser.readValue(input, NPC.class);
            System.out.println(npc);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
        System.out.println("Hello world!");
    }
}