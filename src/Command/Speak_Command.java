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
     * @param command who does plauer want to talk with
     * @return text msg for player
     */
    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();

        String npcId = command.toLowerCase().trim();
        Room room = game.getCurrentRoom();

        // NPC musí být v místnosti
        if (room.getNpcs() == null || !room.getNpcs().contains(npcId)) {
            return game.error("Nikdo takový tu není.");
        }

        NPC npc = game.getNPCs().get(npcId);
        if (npc == null) {
            return game.error("Chyba dat: NPC '" + npcId + "' neexistuje.");
        }

        // Speciální questové interakce při mluvení (Milan, babička)
        // 1) Milan: pokud máš filtr, vyrobí masku
        if ("milan".equals(npcId)) {
            return speakMilan(npc);
        }

        // 2) Babička: po UV lampě může dát uspávací lektvar (pokud ještě není dostupný)
        if ("babicka".equals(npcId)) {
            return speakBabicka(npc);
        }

        // Ostatní NPC: vyber dialog podle stavu
        String dialogueKey = chooseDialogueKey(npc);
        String line = pickDialogue(npc, dialogueKey, "default");

        // Formát výpisu
        String speaker = npc.getNickname() != null && !npc.getNickname().isBlank()
                ? npc.getNickname()
                : npc.getName();

        return speaker + ": " + line;
    }

    // ---------- Milan ----------
    private String speakMilan(NPC milan) {
        // Pokud hráč má filtr, Milan nabídne vyrobit masku
        Item filtr = game.getPlayer().getInventory().getItemById("filtr");

        boolean hasMask = game.getPlayer().getInventory().getItemById("plynova_maska") != null;

        // Pokud už má masku, jen default dialog
        if (hasMask) {
            String line = pickDialogue(milan, "afterMask", "default");
            return milan.getName() + ": " + line;
        }

        // Pokud nemá filtr, default dialog
        if (filtr == null) {
            String line = pickDialogue(milan, "default", null);
            return milan.getName() + ": " + line;
        }

        // Má filtr a ještě nemá masku -> vyrob masku
        Item maska = game.getItemById("plynova_maska");
        if (maska == null) {
            // item chybí v items.json
            String line = pickDialogue(milan, "errorMaskMissing", "default");
            return milan.getName() + ": " + line;
        }

        // Odeber filtr, přidej masku
        game.getPlayer().getInventory().removeItem(filtr);
        game.getPlayer().getInventory().addItem(maska);

        // checkpoint: po pojistkách (cp2) -> maska (cp3)
        if (game.getCheckpoint() == 2) {
            game.setAnotherCheckpoint();
        }

        String line = pickDialogue(milan, "afterFilter", "default");
        return milan.getName() + ": " + line;
    }

    // ---------- Babička ----------
    private String speakBabicka(NPC babicka) {
        Room room = game.getCurrentRoom();

        boolean lektvarInInventory = game.getPlayer().getInventory().getItemById("uspavaci_lektvar") != null;
        boolean lektvarInRoom = room.getItems() != null && room.getItems().contains("uspavaci_lektvar");

        // Pokud je pořád potřeba světlo -> default dialog
        if (babicka.isPlantNeedsLight()) {
            String line = pickDialogue(babicka, "default", null);
            return babicka.getName() + ": " + line;
        }

        // Má světlo -> afterUVLamp dialog
        String line = pickDialogue(babicka, "afterUVLamp", "default");

        // Pokud lektvar ještě není nikde, přidej ho do místnosti jako odměnu
        if (!lektvarInInventory && !lektvarInRoom) {
            if (room.getItems() != null) {
                room.getItems().add("uspavaci_lektvar");
            }
        }

        return babicka.getName() + ": " + line;
    }

    // ---------- Dialogue logic ----------
    private String chooseDialogueKey(NPC npc) {
        String id = npc.getId() == null ? "" : npc.getId().toLowerCase();

        // Spark: broken / repaired
        if ("spark".equals(id)) {
            return npc.isRepaired() ? "repaired" : "broken";
        }

        // Viktor: aggressive / asleep / confused
        if ("viktor".equals(id)) {
            // Pokud hostile -> aggressive
            if (npc.isHostile()) return "aggressive";
            // Pokud není hostile -> asleep je bezpečný default stav po lektvaru
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
