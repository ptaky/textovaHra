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

    public void play() {
        game.setup();
        game.getIntroduction();
        do {
            System.out.print(">> ");
            String input = scn.nextLine();
            if (checkCommand(input)) {
                commandExecute(input);
            } else return;

        } while (!game.isGameOver());

        if (game.playerWon()) {
            print(game.getWinningText());
        }
        if (game.playerLost()) {
            //TODO make lost text
        }
    }

    public void print(String input) {
        System.out.println(input);
    }

    public boolean checkCommand(String input) {
        if (input.isEmpty()) return false;

        String[] commandInput = input.split(" ");
        String command = commandInput[0];

        if (!commands.containsKey(command)) {
            print("neplatny prikaz");
            return false;
        }
        return true;
    }

    public void commandExecute(String input) {
        String[] commandInput = input.split(" ");
        String command = commandInput[0];
        String commandParam = commandInput[1];

        print(commands.get(command).execute(commandParam));
    }

    public void commandLoader() {
        commands.put("konec", new End_Command(game));
        commands.put("prozkoumej", new Explore_Command(game));
        commands.put("jdi", new Go_Command(game));
        commands.put("help", new Help_Command(game));
        commands.put("napoveda", new Hint_Command(game));
        commands.put("poloz", new Place_Command(game));
        commands.put("mluv", new Speak_Command(game));
        commands.put("vezmi", new Take_Command(game));
        commands.put("pouzij", new Use_Command(game));
    }
}
