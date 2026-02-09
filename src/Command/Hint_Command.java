package Command;
import Data.NPC;
import Engine.Game;

import java.util.List;
import java.util.Random;

public class Hint_Command implements Command {

    private final Game game;
    private final Random random = new Random();

    public Hint_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        if (command != null) return game.getInvalidCommand();

        int cp = game.getCheckpoint();

        return String.valueOf(game.getNPCs().get("spark").getHints().get(String.valueOf(cp)));

        // 0 = ještě není opravený Spark (nebo aspoň nemáš splněný 1. checkpoint)

        // 1 = Spark opraven, další krok: elektřina/pojistky

        // 2 = elektřina hotová, další: zdroje na karanténu (filtr → maska)

        // 3 = maska hotová, další: obejít Viktora (zrcátko / uspání)

        // 4 = věž připravená / serverovna / finále


        // fallback
        // return "Analyzuji situaci… drž se hlavního cíle: odeslat SOS signál.";
    }

    private String randomFrom(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
