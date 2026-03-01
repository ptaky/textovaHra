package Command;

import Data.Item;
import Data.NPC;
import Data.Room;
import Engine.Game;

import java.util.List;
import java.util.Random;

/**
 * Command for talking with NPC in current room.
 * @author Ondřej Ptáček
 */
public class Speak_Command implements Command {

    private final Game game;
    private final Random rnd = new Random();

    public Speak_Command(Game game) {
        this.game = game;
    }

    /**
     * Executes command "mluv".
     * @param command who does player want to talk with
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();

        String npcId = command.toLowerCase().trim();
        Room room = game.getCurrentRoom();

        if (room.getNpcs() == null || !room.getNpcs().contains(npcId)) {
            return game.error("Nikdo takový tu není.");
        }

        NPC npc = game.getNPCs().get(npcId);
        if (npc == null) {
            return game.error("Chyba dat: NPC '" + npcId + "' neexistuje.");
        }

        if ("milan".equals(npcId)) {
            return speakMilan(npc);
        }

        if ("babicka".equals(npcId)) {
            return speakBabicka(npc);
        }

        String key = chooseDialogueKey(npc);
        String line = pickDialogue(npc, key, "default");

        String speaker;
        if (npc.getNickname() != null && !npc.getNickname().isBlank()) {
            speaker = npc.getNickname();
        } else {
            speaker = npc.getName();
        }

        return speaker + ": " + line;
    }

    // ---------- Milan ----------
    private String speakMilan(NPC milan) {
        Item filtr = game.getPlayer().getInventory().getItemById("filtr");
        boolean hasMask = game.getPlayer().getInventory().getItemById("plynova_maska") != null;

        if (hasMask) {
            String line = pickDialogue(milan, "afterMask", "default");
            return milan.getName() + ": " + line;
        }

        if (filtr == null) {
            String line = pickDialogue(milan, "default", null);
            return milan.getName() + ": " + line;
        }

        Item maska = game.getItemById("plynova_maska");
        if (maska == null) {
            String line = pickDialogue(milan, "errorMaskMissing", "default");
            return milan.getName() + ": " + line;
        }

        game.getPlayer().getInventory().removeItem(filtr);
        game.getPlayer().getInventory().addItem(maska);
        game.setCheckpoint(3);

        String line = pickDialogue(milan, "afterFilter", "default");
        return milan.getName() + ": " + line;
    }

    // ---------- Babička ----------
    private String speakBabicka(NPC babicka) {
        Room room = game.getCurrentRoom();

        if (babicka.isPlantNeedsLight()) {
            String line = pickDialogue(babicka, "default", null);
            return babicka.getName() + ": " + line;
        }

        boolean lektvarInInventory = game.getPlayer().getInventory().getItemById("uspavaci_lektvar") != null;
        boolean lektvarInRoom = room.getItems() != null && room.getItems().contains("uspavaci_lektvar");

        if (!lektvarInInventory && !lektvarInRoom) {
            if (room.getItems() != null) {
                room.getItems().add("uspavaci_lektvar");
            }
        }

        String line = pickDialogue(babicka, "afterUVLamp", "default");
        return babicka.getName() + ": " + line;
    }

    // ---------- Dialogue logic ----------
    private String chooseDialogueKey(NPC npc) {
        String id = npc.getId();

        if (id == null) return "default";

        id = id.toLowerCase();

        if ("spark".equals(id)) {
            if (npc.isRepaired()) {
                return "repaired";
            } else {
                return "broken";
            }
        }

        if ("viktor".equals(id)) {
            if (npc.isHostile()) return "aggressive";
            return "asleep";
        }

        return "default";
    }

    private String pickDialogue(NPC npc, String key, String fallbackKey) {
        if (npc.getDialogues() == null) return "...";

        List<String> lines = npc.getDialogues().get(key);
        if (lines == null || lines.isEmpty()) {
            if (fallbackKey == null) return "...";
            lines = npc.getDialogues().get(fallbackKey);
        }

        if (lines == null || lines.isEmpty()) return "...";

        return lines.get(rnd.nextInt(lines.size()));
    }

    @Override
    public boolean exit() {
        return false;
    }
}