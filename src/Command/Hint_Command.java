package Command;
import Data.NPC;
import Data.Room;
import Engine.Game;

import java.util.List;
import java.util.Random;

/**
 * Command for giving player a hint.
 * @author Ondřej Ptáček
 */
public class Hint_Command implements Command {

    private final Game game;
    private final Random random = new Random();

    public Hint_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "napoveda".
     * @param command null
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command != null) return game.error(game.getInvalidCommand());

        NPC spark = game.getNPCs().get("spark");
        if (spark == null) {
            return game.error("Spark není k dispozici.");
        }

        int cp = getCurrentHintCheckpoint();
        List<String> hints = spark.getHints().get(String.valueOf(cp));

        if (hints == null || hints.isEmpty()) {
            hints = spark.getHints().get("default");
        }
        if (hints == null || hints.isEmpty()) {
            return "Spark analyzuje situaci, ale nic konkrétního teď neví.";
        }

        return hints.get(random.nextInt(hints.size()));
    }

    private int getCurrentHintCheckpoint() {
        NPC spark = game.getNPCs().get("spark");
        NPC babicka = game.getNPCs().get("babicka");
        NPC viktor = game.getNPCs().get("viktor");
        Room serverovna = game.getRooms().get("serverovna");

        if (spark != null && !spark.isRepaired()) {
            return 0;
        }

        if (game.getCheckpoint() < 2) {
            return 1;
        }

        boolean hasMask = game.getPlayer().getInventory().getItemById("plynova_maska") != null;
        if (!hasMask && babicka != null && babicka.isPlantNeedsLight()) {
            return 2;
        }

        if (viktor != null && viktor.isHostile()) {
            return 3;
        }

        boolean serverUnlocked = serverovna != null && !serverovna.isLocked();
        if (!serverUnlocked) {
            return 3;
        }

        return 4;
    }

    @Override
    public boolean exit() {
        return false;
    }
}