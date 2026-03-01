package Engine;

import Command.*;

import java.util.HashMap;
import java.util.Scanner;

/**
 * This class makes it all readable and communicates with the player.
 * @author Ondřej Ptáček
 */
public class UserInterface {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final Game game;
    Scanner scn = new Scanner(System.in);

    public UserInterface() {
        this.game = new Game();
    }

    /**
     * The main game loop.
     */
    public void play() {
        game.setup();
        hashmapsLoader();
        print(game.getIntroduction());
        print(game.getLine(true));
        print(game.getMap(true, game.getCurrentRoom().getId()));
        print(game.roomInfo());

        do {
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
            playAgain();
        }
    }

    public void print(String input) {
        System.out.println(input);
    }

    /**
     * This method takes the input and checks if it is usable.
     * If it is valid, it calls cmd.execute, otherwise it prints an error message.
     * @param input user input given by the scanner
     */
    public void commandExecute(String input) {
        input = input.trim().toLowerCase();

        if (input.isEmpty()) {
            print(game.error("prikaz je prazdny"));
            return;
        }

        String[] parts = input.split("\\s+", 2);

        String cmdName = parts[0];
        String param = null;
        if (parts.length > 1) {
            param = parts[1];
        }

        Command cmd = commands.get(cmdName);

        if (cmd == null) {
            print(game.getInvalidCommand());
            return;
        }

        print(cmd.execute(param));
    }

    /**
     * Loads the hash maps with commands and their expected arguments.
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
    }

    public void playAgain() {
        print("chces jeste jednou ? [ano/ne]");
        System.out.println(">> ");
        String input = scn.nextLine();

        switch (input) {
            case "ano":
                play();
            case "ne":
                return;
            default:
                print(game.getInvalidCommand());
        }
    }
}