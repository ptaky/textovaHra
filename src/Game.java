import Command.Command;
import java.util.HashMap;

public class Game {
    private HashMap<String, Command> commands;

    public void setup() {
        DataLoader dataLoader = new DataLoader();
        dataLoader.loadRoomsData();

    }

    public void CommandManager() {
        commands = new HashMap<>();
//        commands.put("end", new End_Command());
//        commands.put("explore", new Explore_Command());
//        commands.put("go", new Go_Command());
    }

}
