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

    /**
     * Provede příkaz "pouzij".
     * Zkontroluje předmět a podle jeho akce provede správnou metodu.
     * @param command název předmětu
     * @return textová zpráva pro hráče
     */
    @Override
    public String execute(String command) {

        if (command == null) return game.getInvalidCommand();

        String itemId = command.toLowerCase();
        Room room = game.getCurrentRoom();
        Item item = game.getPlayer().getInventory().getItemById(itemId);

        if (item == null) return game.error("Tenhle předmět nemáš v inventáři.");
        if (!item.isUsable()) return game.error("Tenhle předmět se teď nedá použít.");

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
                result = game.error("Tohle použití ještě není implementované: " + action);
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

    /**
     * Opravní dronu.
     */
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

    /**
     * Obnoví elektřinu v chodbě.
     */
    private String useRestorePower(Room room, Map<String, Object> effects) {

        boolean restore = effectBoolean(effects, "restoreElectricity");
        if (!restore) return fail("Item neumí obnovit elektřinu.");

        if (!"chodba".equals(room.getId())) {
            return fail("Pojistky dávají smysl použít v chodbě u rozvaděče.");
        }

        game.setAnotherCheckpoint();

        return "Vyměnil/a jsi pojistky. Nouzové osvětlení zesílí a stanice částečně ožije.";
    }

    /**
     * Nainstaluje UV lampu v botanické zahradě.
     */
    private String useInstallUVLamp(Room room, Map<String, Object> effects) {

        String npcId = normalizeNpcId(effectString(effects, "satisfyNpc"));
        if (npcId == null) return fail("Item nemá nastavené satisfyNpc.");

        if (!"botanicka_zahrada".equals(room.getId())) {
            return fail("UV lampu je nejlepší použít v botanické zahradě.");
        }

        if (!roomHasNpc(room, npcId)) {
            return fail("Nikdo tu není, komu by to pomohlo.");
        }

        NPC babicka = game.getNPCs().get(npcId);
        if (babicka == null) return fail("Chyba dat: NPC '" + npcId + "' neexistuje.");

        if (!babicka.isPlantNeedsLight()) {
            return babicka.getName() + ": " + pickDialogue(babicka, "default", null);
        }

        babicka.setPlantNeedsLight(false);
        game.setAnotherCheckpoint();

        return babicka.getName() + ": " + pickDialogue(babicka, "afterUVLamp", "default");
    }

    /**
     * Odemkne serverovnu pomocí karty.
     */
    private String useUnlockServerRoom(Room room, Map<String, Object> effects) {

        String locationId = effectString(effects, "unlockLocation");
        if (locationId == null) return fail("Item nemá nastavené unlockLocation.");

        if (!"karantena".equals(room.getId())) {
            return fail("Kartu musíš použít u dveří do serverovny (v karanténě).");
        }

        Room target = game.getRooms().get(locationId);
        if (target == null) return fail("Chyba dat: místnost '" + locationId + "' neexistuje.");

        if (!target.isLocked()) return "Serverovna už je odemčená.";

        target.setIsLocked(false);
        return "Píp. Dveře do serverovny se odemknou.";
    }

    /**
     * Uspí nebo zmate Viktora.
     */
    private String useAffectTarget(Room room, Map<String, Object> effects, String mode) {

        if (!"karantena".equals(room.getId())) {
            return fail("Tohle dává smysl použít v karanténě.");
        }

        String targetId = normalizeNpcId(effectString(effects, "targetNpc"));
        double chance = effectDouble(effects, "successChance", 1.0);

        if (targetId == null) return fail("Item nemá nastavené targetNpc.");
        if (!roomHasNpc(room, targetId)) return fail("Cíl tu není.");

        NPC viktor = game.getNPCs().get(targetId);
        if (viktor == null) return fail("Chyba dat: NPC '" + targetId + "' neexistuje.");

        if (!viktor.isHostile()) {
            String key;
            if ("sleep".equals(mode)) {
                key = "asleep";
            } else {
                key = "confused";
            }
            return viktor.getName() + ": " + pickDialogue(viktor, key, "default");
        }

        boolean success = rnd.nextDouble() <= chance;
        if (!success) {
            return viktor.getName() + ": " + pickDialogue(viktor, "aggressive", "default");
        }

        viktor.setHostile(false);
        game.setAnotherCheckpoint();

        if ("sleep".equals(mode)) {
            return viktor.getName() + ": " + pickDialogue(viktor, "asleep", "confused");
        } else {
            return viktor.getName() + ": " + pickDialogue(viktor, "confused", "aggressive");
        }
    }

    /**
     * Aktivuje vysílání a vyhraje hru.
     */
    private String useActivateSignal(Room room, Map<String, Object> effects) {

        boolean win = effectBoolean(effects, "winGame");
        if (!win) return fail("Tenhle předmět neumí spustit vysílání.");

        if (!"vysilaci_vez".equals(room.getId())) {
            return fail("Šifrovací kartu musíš použít ve vysílací věži.");
        }

        game.setPlayerWon(true);
        game.quitGame();

        return "Vložíš šifrovací kartu do terminálu. Antény ožívají a SOS signál je odeslán.";
    }

    // ---------------- helpers ----------------

    /**
     * Zjistí, jestli je v místnosti dané NPC.
     */
    private boolean roomHasNpc(Room room, String npcId) {
        List<String> npcs = room.getNpcs();
        return npcs != null && npcs.contains(npcId);
    }

    /**
     * Vybere hlášku z dialogů NPC.
     */
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

    /**
     * Vrátí hodnotu z mapy jako String.
     */
    private String effectString(Map<String, Object> effects, String key) {
        if (effects == null) return null;

        Object v = effects.get(key);
        if (v == null) {
            return null;
        } else {
            return String.valueOf(v);
        }
    }

    /**
     * Vrátí hodnotu z mapy jako boolean.
     */
    private boolean effectBoolean(Map<String, Object> effects, String key) {
        if (effects == null) return false;

        Object v = effects.get(key);
        if (v instanceof Boolean) return (Boolean) v;

        return v != null && Boolean.parseBoolean(String.valueOf(v));
    }

    /**
     * Vrátí hodnotu z mapy jako double.
     */
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

    /**
     * Upraví ID NPC na malá písmena a ořeže mezery.
     */
    private String normalizeNpcId(String npcId) {
        if (npcId == null) return null;
        npcId = npcId.toLowerCase().trim();
        return npcId;
    }

    /**
     * Zjistí, jestli akce proběhla úspěšně.
     */
    private boolean isSuccess(String msg) {
        return msg != null && !msg.startsWith("ERROR: ");
    }

    /**
     * Vytvoří chybovou hlášku.
     */
    private String fail(String text) {
        return "ERROR: " + text;
    }
}