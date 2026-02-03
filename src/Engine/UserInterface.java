package Engine;

import Command.*;

import java.util.HashMap;
import java.util.Scanner;

public class UserInterface {
    private HashMap<String, Command> commands;
    private final Game game;
    Scanner scn = new Scanner(System.in);

    public UserInterface() {
        commands = new HashMap<String, Command>();
        this.game = new Game();
    }

    public void print(String input) {
        System.out.println(input);
    }

    public void commandExecute(String input) {
        String[] commandInput = input.split(" ");
        String command = commandInput[0];
        String commandParam = commandInput[1];

        if (!commands.containsKey(command)) {
            print("Invalid command");
            return;
        }

        print(commands.get(command).execute(commandParam));
    }

    public void commandLoader() {
        commands.put("end", new End_Command(game));
        commands.put("explore", new Explore_Command(game));
        commands.put("go", new Go_Command(game));
        commands.put("help", new Help_Command(game));
        commands.put("hint", new Hint_Command(game));
        commands.put("place", new Place_Command(game));
        commands.put("speak", new Speak_Command(game));
        commands.put("take", new Take_Command(game));
        commands.put("use", new Use_Command(game));
    }
}
