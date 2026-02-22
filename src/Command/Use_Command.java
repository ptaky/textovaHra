package Command;
import Data.Item;
import Data.NPC;
import Data.Room;
import Engine.Game;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Use_Command implements Command {
    private Game game;
    private Random rnd = new Random();

    public Use_Command(Game game) {
        this.game = game;
    }

    @Override
    public String execute(String command) {
        if (command == null) return game.getInvalidCommand();

        String itemId = command.toLowerCase();
        Room room = game.getCurrentRoom();
        Item item = game.getPlayer().getInventory().findItemById(itemId);
            if (item == null) return "Tenhle předmět nemáš v inventáři.";
            if (!item.isUsable()) return "Tenhle předmět se teď nedá použít.";

        String action = item.getUseAction();
        Map<String, Object> effects = item.getEffects();

        String result;

        switch (action) {

            case "repair_drone":
                result = useRepairDrone(room, effects);
                break;

            case "restore_power":
                result = useRestorePower(room, effects);
                break;

            case "install_uv_lamp":
                result = useInstallUVLamp(room, effects);
                break;

            case "unlock_server_room":
                result = useUnlockServerRoom(room, effects);
                break;

            case "sleep_target":
                result = useAffectTarget(room, effects, "sleep");
                break;

            case "confuse_target":
                result = useAffectTarget(room, effects, "confuse");
                break;

            case "activate_signal":
                result = useActivateSignal(room, effects);
                break;

            default:
                result = "Tohle použití ještě není implementované: " + action;
                break;
        }

        if (isSuccess(result) && (item.isConsumable() || item.isSingleUse())) {
            game.getPlayer().getInventory().removeItem(item);
        }

        return result;
    }

    @Override
    public boolean exit() {
        return false;
    }

    private String useRepairDrone(Room room, Map<String, Object> effects) {
        String npcId = normalizeNpcId(effectString(effects, "repairNpc"));
        if (npcId == null) return fail("Item nemá nastavené repairNpc.");

        if (!roomHasNpc(room, npcId)) {
            return fail("Tady to nemáš do čeho zapojit.");
        }

        NPC npc = game.getNPCs().get(npcId);
        if (npc == null) return fail("Chyba dat: NPC '" + npcId + "' neexistuje.");

        if (npc.isRepaired()) {
            return npc.getNickname() + " už je opravený.";
        }

        npc.setIsRepaired(true);

        game.setAnotherCheckpoint();

        String line = pickDialogue(npc, "repaired", "broken");
        return npc.getNickname() + ": " + line;
    }

    private String useRestorePower(Room room, Map<String, Object> effects) {

        boolean restore = effectBoolean(effects, "restoreElectricity");

        if (!"chodba".equals(room.getId())) {
            return fail("Pojistky dávají smysl použít v chodbě u rozvaděče.");
        }

        game.setAnotherCheckpoint();

        return "Vyměnil/a jsi pojistky. Nouzové osvětlení zesílí a stanice částečně ožije.";
    }

    private String useInstallUVLamp(Room room, Map<String, Object> effects) {

        String npcId = effectString(effects, "satisfyNpc");
        npcId = normalizeNpcId(npcId);

        if (npcId == null) {
            return fail("Item nemá nastavené satisfyNpc.");
        }

        if (room.getId().equals("botanicka_zahrada") == false) {
            return fail("UV lampu je nejlepší použít v botanické zahradě.");
        }

        if (roomHasNpc(room, npcId) == false) {
            return fail("Nikdo tu není, komu by to pomohlo.");
        }

        NPC babicka = game.getNPCs().get(npcId);
        if (babicka == null) {
            return fail("Chyba dat: NPC '" + npcId + "' neexistuje.");
        }

        if (babicka.isPlantNeedsLight() == false) {
            String line = pickDialogue(babicka, "default", null);
            return babicka.getName() + ": " + line;
        }

        babicka.setPlantNeedsLight(false);

        game.setAnotherCheckpoint();

        String line = pickDialogue(babicka, "afterUVLamp", "default");
        return babicka.getName() + ": " + line;
    }

    // TODO nejspis to nefunguje, neni potreba kartu pouzit i kdyz by melo, podivat se na to
    private String useUnlockServerRoom(Room room, Map<String, Object> effects) {

        String locationId = effectString(effects, "unlockLocation");

        if (locationId == null) {
            return fail("Item nemá nastavené unlockLocation.");
        }

        if (room.getId().equals("karantena") == false) {
            return fail("Kartu musíš použít u dveří do serverovny (v karanténě).");
        }

        Room target = game.getRooms().get(locationId);
        if (target == null) {
            return fail("Chyba dat: místnost '" + locationId + "' neexistuje.");
        }

        if (target.isLocked() == false) {
            return "Serverovna už je odemčená.";
        }

        target.setIsLocked(false);
        return "Píp. Dveře do serverovny se odemknou.";
    }

    private String useAffectTarget(Room room, Map<String, Object> effects, String mode) {
        if (!"karantena".equals(room.getId())) {
            return fail("Tohle dává smysl použít v karanténě.");
        }

        String targetId = effectString(effects, "targetNpc");
        targetId = normalizeNpcId(targetId);

        double chance = effectDouble(effects, "successChance", 1.0);

        if (targetId == null) {
            return fail("Item nemá nastavené targetNpc.");
        }

        if (roomHasNpc(room, targetId) == false) {
            return fail("Cíl tu není.");
        }

        NPC viktor = game.getNPCs().get(targetId);
        if (viktor == null) {
            return fail("Chyba dat: NPC '" + targetId + "' neexistuje.");
        }

        if (viktor.isHostile() == false) {
            String key = "";
            if (mode.equals("sleep")) {
                key = "asleep";
            } else {
                key = "confused";
            }

            String line = pickDialogue(viktor, key, "default");
            return viktor.getName() + ": " + line;
        }

        double roll = rnd.nextDouble();
        boolean success = false;

        if (roll <= chance) {
            success = true;
        } else {
            success = false;
        }

        if (success == false) {
            String line = pickDialogue(viktor, "aggressive", "default");
            return viktor.getName() + ": " + line;
        }

        viktor.setHostile(false);

        game.setAnotherCheckpoint();

        if (mode.equals("sleep")) {
            String line = pickDialogue(viktor, "asleep", "confused");
            return viktor.getName() + ": " + line;
        } else {
            String line = pickDialogue(viktor, "confused", "aggressive");
            return viktor.getName() + ": " + line;
        }
    }

    private String useActivateSignal(Room room, Map<String, Object> effects) {

        boolean win = effectBoolean(effects, "winGame");

        if (win == false) {
            return fail("Tenhle předmět neumí spustit vysílání.");
        }

        if (room.getId().equals("vysilaci_vez") == false) {
            return fail("Šifrovací kartu musíš použít ve vysílací věži.");
        }

        game.setPlayerWon(true);
        game.quitGame();

        return "Vložíš šifrovací kartu do terminálu. Antény ožívají a SOS signál je odeslán.";
    }

    // ---------------- helpers ----------------

    private boolean roomHasNpc(Room room, String npcId) {
        List<String> npcs = room.getNpcs();

        if (npcs == null) {
            return false;
        }

        if (npcs.contains(npcId)) {
            return true;
        }

        return false;
    }

    private String pickDialogue(NPC npc, String key, String fallbackKey) {

        if (npc.getDialogues() == null) {
            return "...";
        }

        List<String> lines = npc.getDialogues().get(key);

        if (lines == null || lines.isEmpty()) {
            if (fallbackKey == null) {
                return "...";
            }
            lines = npc.getDialogues().get(fallbackKey);
        }

        if (lines == null || lines.isEmpty()) {
            return "...";
        }

        int index = rnd.nextInt(lines.size());
        return lines.get(index);
    }

    private String effectString(Map<String, Object> effects, String key) {
        if (effects == null) return null;
        Object v = effects.get(key);
        return v == null ? null : String.valueOf(v);
    }

    private boolean effectBoolean(Map<String, Object> effects, String key) {
        if (effects == null) return false;
        Object v = effects.get(key);
        if (v instanceof Boolean) return (Boolean) v;
        return v != null && Boolean.parseBoolean(String.valueOf(v));
    }

    private double effectDouble(Map<String, Object> effects, String key, double fallback) {
        if (effects == null) return fallback;
        Object v = effects.get(key);
        if (v == null) return fallback;
        try {
            return Double.parseDouble(String.valueOf(v));
        } catch (Exception e) {
            return fallback;
        }
    }

    private String normalizeNpcId(String npcId) {
        if (npcId == null) return null;
        npcId = npcId.toLowerCase().trim();

        return npcId;
    }

    private boolean isSuccess(String msg) {
        return msg != null && !msg.startsWith("ERROR: ");
    }

    private String fail(String text) {
        return "ERROR: " + text;
    }
}