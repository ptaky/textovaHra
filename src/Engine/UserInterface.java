package Engine;

import Command.*;

import java.util.HashMap;
import java.util.Scanner;

public class UserInterface {
    private final HashMap<String, Command> commands;
    private final Game game;
    Scanner scn = new Scanner(System.in);

    public UserInterface() {
        commands = new HashMap<String, Command>();
        this.game = new Game();
    }

    public void play() {
        game.setup();
        commandLoader();
        print(game.getIntroduction());
        print(game.getLine(true));
        print(game.getMap());
        do {
            print(game.getLine(false));
            print(game.getCurrentRoom().toString());
            System.out.print(">> ");
            String input = scn.nextLine();
            if (checkCommand(input)) {
                commandExecute(input);
            }

        } while (!game.isGameOver());

        if (game.playerWon()) {
            print(game.getWinningText());
        }
        if (game.playerLost()) {
            print(game.getLosingText());
        }
    }

    public void print(String input) {
        System.out.println(input);
    }

    public boolean checkCommand(String input) {
        if (input.isEmpty()) {
            print("prikaz je prazdny");
            return false;
        };

        String[] commandInput = input.split(" ");
        String command = commandInput[0];

        if (!commands.containsKey(command)) {
            print(game.getInvalidCommand());
            return false;
        }
        return true;
    }

    public void commandExecute(String input) {
        String command = null;
        String commandParam = null;
        if (input.contains(" ")) {
            String[] commandInput = input.split(" ");
            command = commandInput[0];
            commandParam = commandInput[1];
        } else {
            command = input;
        }

        print(commands.get(command).execute(commandParam));
    }

    public void commandLoader() {
        commands.put("konec", new End_Command(game));
        commands.put("prozkoumej", new Explore_Command(game));
        commands.put("jdi", new Go_Command(game));
        commands.put("pomoc", new Help_Command(game));
        commands.put("napoveda", new Hint_Command(game));
        commands.put("poloz", new Place_Command(game));
        commands.put("mluv", new Speak_Command(game));
        commands.put("vezmi", new Take_Command(game));
        commands.put("pouzij", new Use_Command(game));
    }
}
