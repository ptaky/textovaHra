package Engine;

import Data.*;

import java.util.HashMap;
import java.util.List;

public class Game {
    private Player player;
    private int checkpoint;
    private HashMap<String,Room> rooms;
    private Room currentRoom;
    private List<NPC> NPCs;
    private List<Item> items;
    private boolean gameOver;
    private boolean playerWon;
    private boolean playerLost;
    private String introduction;
    private String winningText;
    private String losingText;

    public void setup() {
        DataLoader dataLoader = new DataLoader();
        rooms = dataLoader.loadRoomsData();
        NPCs = dataLoader.loadNPCData();
        items = dataLoader.loadItemsData();

        currentRoom = rooms.get("kryokomora");
        gameOver = false;
        checkpoint = 0;
        setIntroduction();
    }

    public String getLine(boolean withNextLine) {
        String line = "________________________________________________________________________________________________________________________________________________________________________________________________________";
        if (withNextLine) return line + "\n";
        else return line;
    }

    public String getInvalidCommand() {
        return "neplatny prikaz";
    }

    public String getMap() {
        return
                "\n        [ VYSÍLACÍ VĚŽ ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "         [ KARANTÉNA ]-------[ SERVEROVNA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "        [ CHODBA ALFA ]------[ BOTANICKÁ ZAHRADA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "      [ LÉKAŘSKÝ TRAKT ]------[ DÍLNA ]\n" +
                "               |\n" +
                "               |\n" +
                "               |\n" +
                "        [ KRYOKOMORA ]\n";
    }

    public String getCheckpoint(int checkpoint) {
        switch (checkpoint) {
            case 0 :
                return "17h 32min 42s";
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            default:
                return "error";
        }
    }
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

    public List<NPC> getNPCs() {
        return NPCs;
    }
    public void setNPCs(List<NPC> NPCs) {
        this.NPCs = NPCs;
    }

    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
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
