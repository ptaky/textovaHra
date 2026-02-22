package Engine;

import Command.*;

import java.util.HashMap;
import java.util.Scanner;

public class UserInterface {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final HashMap<String, Integer> expectedArgs = new HashMap<>();
    private final Game game;
    Scanner scn = new Scanner(System.in);

    public UserInterface() {
        this.game = new Game();
    }

    /**
     * whole game loop
     */
    public void play() {
        game.setup();
        hashmapsLoader();
        print(game.getIntroduction());
        print(game.getLine(false));
        print(game.getMap(true));
        do {
            print(game.getLine(false));
            print(game.getCurrentRoom().toString());
            print(game.getInventory());
            System.out.print(">> ");
            String input = scn.nextLine();
            print("");

            commandExecute(input);

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

    /**
     * This metod takes input and checks, if is usable -> calls cmd.execute, if not -> prints error message
     * @param input user input, which is given by scanner
     */
    public void commandExecute(String input) {
        input = input.trim();
        if (input.isEmpty()) {
            print(game.error("prikaz je prazdny"));
            return;
        }

        String[] parts = input.split("\\s+");
        String cmdName = parts[0];

        Command cmd = commands.get(cmdName);
        if (cmd == null) {
            print(game.getInvalidCommand());
            return;
        }

        int expected = expectedArgs.getOrDefault(cmdName, 0);
        int actual = parts.length - 1;

        if (actual != expected) {
            if (expected == 0) {
                print(game.error("Prikaz '" + cmdName + "' nebere zadny parametr."));
            } else {
                print(game.error("Prikaz '" + cmdName + "' vyzaduje presne " + expected + " parametr."));
            }
            return;
        }

        String param;
        if (expected == 1) {
            param = parts[1];
        } else {
            param = null;
        }

        print(cmd.execute(param));
    }

    /**
     * load hashmaps
     */
    public void hashmapsLoader() {
        commands.put("konec", new End_Command(game));
        commands.put("prozkoumej", new Explore_Command(game));
        commands.put("jdi", new Go_Command(game));
        commands.put("pomoc", new Help_Command(game));
        commands.put("napoveda", new Hint_Command(game));
        commands.put("poloz", new Place_Command(game));
        commands.put("mluv", new Speak_Command(game));
        commands.put("vezmi", new Take_Command(game));
        commands.put("pouzij", new Use_Command(game));

        expectedArgs.put("konec", 0);
        expectedArgs.put("prozkoumej", 0);
        expectedArgs.put("jdi", 1);
        expectedArgs.put("pomoc", 0);
        expectedArgs.put("napoveda", 0);
        expectedArgs.put("poloz", 1);
        expectedArgs.put("mluv", 1);
        expectedArgs.put("vezmi", 1);
        expectedArgs.put("pouzij", 1);
    }
}
