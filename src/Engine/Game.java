package Engine;

import Data.*;

import java.util.HashMap;
import java.util.List;

/**
 * Game logics.
 * @author Ondřej Ptáček
 */
public class Game {
    private Player player = new Player();
    private int checkpoint;
    private HashMap<String, Room> rooms;
    private Room currentRoom;
    private HashMap<String, NPC> NPCs;
    private HashMap<String, Item> items;
    private boolean gameOver;
    private boolean playerWon;
    private boolean playerLost;
    private String introduction;
    private String winningText;
    private String losingText;

    /**
     * This method prepares and sets up the whole game.
     */
    public void setup() {
        DataLoader dataLoader = new DataLoader();
        rooms = dataLoader.loadRoomsData();
        NPCs = dataLoader.loadNPCData();
        items = dataLoader.loadItemsData();

        currentRoom = rooms.get("kryokomora");
        gameOver = false;
        checkpoint = 0;
        setIntroduction();
        setWinningText();
        setLosingText();
    }

    /**
     * Just a line between texts in the terminal.
     * @param withNextLine if a new line should be added
     * @return the line
     */
    public String getLine(boolean withNextLine) {
        String line = "________________________________________________________________________________________________________________________________________________________________________________________________________";
        if (withNextLine) return line + "\n";
        else return line;
    }

    /**
     * @return error command message
     */
    public String getInvalidCommand() {
        return "neplatny prikaz";
    }

    public String error(String message) {
        return "ERROR: " + message;
    }

    /**
     * Returns an ASCII map of the station.
     * @param withHelpCommand if the help text should be shown
     * @return map of the station
     */
    public String getMap(boolean withHelpCommand) {
        if (!withHelpCommand) return
                "\n        [ VYSÍLACÍ VĚŽ ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "         [ KARANTÉNA ]-------[ SERVEROVNA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "          [ CHODBA ]------[ BOTANICKÁ ZAHRADA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "      [ LÉKAŘSKÝ TRAKT ]------[ DÍLNA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "        [ KRYOKOMORA ]\n";

        return
                "        [ VYSÍLACÍ VĚŽ ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "         [ KARANTÉNA ]-------[ SERVEROVNA ]            Můžeš použít tyto příkazy:\n" +
                "               |                                       - jdi 'mistnost', prozkoumej\n" +
                "               |                                       - vezmi 'predmet', poloz 'predmet', pouzij 'predmet'\n" +
                "               |                                       - mluv 'postava', napoveda (od Sparka)\n" +
                "          [ CHODBA ]------[ BOTANICKÁ ZAHRADA ]        - pomoc, konec\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "      [ LÉKAŘSKÝ TRAKT ]------[ DÍLNA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "        [ KRYOKOMORA ]";
    }

    /**
     * @return inventory
     */
    public String getInventory() {
        if (!player.getInventory().isEmpty()) {
            return "= neseš: " + player.getInventory();
        } else {
            return "= nic neneseš";
        }
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    /**
     * Returns remaining time text based on the current checkpoint.
     * @return remaining time string for the current checkpoint
     */
    public String getTimeAtCheckpoint() {
        int cp = getCheckpoint();
        String txt = "Zbývá ještě ";

        switch (cp) {
            case 0 :
                txt += "17h 32min 42s";
            case 1:
                txt += "13h 45min 53s";
            case 2:
                txt += "10h 10min 04s";
            case 3:
                txt += "06h 40min 42s";
            case 4:
                txt += "02h 15min 28s";
            default:
                txt += "už došel čas";
        }
        return txt;
    }

    /**
     * Increases the checkpoint value by 1.
     */
    public void setAnotherCheckpoint() {
        checkpoint++;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public HashMap<String,Room> getRooms() {
        return rooms;
    }
    public void setRooms(HashMap<String,Room> rooms) {
        this.rooms = rooms;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public HashMap<String,NPC> getNPCs() {
        return NPCs;
    }
    public void setNPCs(HashMap<String,NPC> NPCs) {
        this.NPCs = NPCs;
    }

    public HashMap<String, Item> getItems() {
        return items;
    }
    public void setItems(HashMap<String, Item> items) {
        this.items = items;
    }

    /**
     * Finds an item in the loaded items by its ID.
     * @param id item ID
     * @return the item if found, otherwise null
     */
    public Item getItemById(String id) {
        for (Item item : items.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Checks if the current room contains an item with the given ID.
     * @param itemId item ID
     * @return true if the item is in the current room, otherwise false
     */
    public boolean roomContains(String itemId) {
        return currentRoom.containsItem(itemId);
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void quitGame() {
        this.gameOver = true;
    }

    public boolean playerWon() {
        return playerWon;
    }
    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
    }

    public boolean playerLost() {
        return playerLost;
    }
    public void setPlayerLost(boolean playerLost) {
        this.playerLost = playerLost;
    }

    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction() {
        this.introduction =
                getLine(true) +
                "Ticho. Tma. A pak alarm.\n" +
                "\n" +
                "Probouzíš se z kryospánku dřív, než bylo plánováno. Nouzová světla blikají a počítač stanice Boreas chladně oznamuje: posádka – mrtvá. Stabilita jádra planety – kritická.\n" +
                "Do rozpadu planety zbývá jen 17h 32min 42s.\n" +
                "\n" +
                "Stanice je bez energie, chodby jsou ponořené do temnoty a něco tu není v pořádku. Jsi tu sama.\n" +
                "Nebo… skoro sama.\n" +
                "\n" +
                "Pokud se ti v čas nepodaří zprovoznit systémy a odeslat SOS signál, Boreas – i ty – zmizíte v explozi.\n" +
                "Čas běží. Každé rozhodnutí se počítá.\n" +
                "\n" +
                "Vítej na stanici Boreas.\n" +
                "\n" +
                "prikazy pis ve tvaru: 'prikaz popis'\n" +
                "  - mistnosti pis ve tvaru: 'velka_loznice'";
    }

    public String getWinningText() {
        return winningText;
    }
    public void setWinningText() {
        this.winningText =
                getLine(true) +
                "Signál odeslán.\n" +
                "\n" +
                "Anténa se probouzí k životu a stanice se po dlouhé době znovu rozzáří. Nouzový signál míří do hlubokého vesmíru – a tentokrát nezůstane bez odpovědi.\n" +
                "Záchrana je na cestě.\n" +
                "Stanice Boreas žije – díky tobě.\n";
    }

    public String getLosingText() {
        return losingText;
    }
    public void setLosingText() {
        this.losingText =
                getLine(true) +
                "Čas vypršel.\n" +
                "\n" +
                "Varovné systémy jeden po druhém umlkají. Jádro planety se hroutí a stanice Boreas se rozpadá v oslepujícím záblesku.\n" +
                "Tvůj signál už nikdo neuslyší.\n" +
                "\n" +
                "Ticho.\n" +
                "Tma.\n" +
                "\n" +
                "Mise selhala.\n";
    }
}