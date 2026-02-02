package Data;

import java.sql.Time;
import java.util.List;

public class Game {
    private Player player;
    private Time timeleft;
    private List<Room> rooms;
    private Room currentRoom;
    private List<NPC> NPCs;
    private List<Item> items;
    private boolean gameOver;
    private String introduction;
    private String winningText;

    public void setup() {
        DataLoader dataLoader = new DataLoader();
        rooms = dataLoader.loadRoomsData();
        NPCs = dataLoader.loadNPCData();
        items = dataLoader.loadItemsData();
    }

    public void quitGame() {
        //TODO dodelat to
    }

    public void shorterTime() {
        //TODO dodelat to
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getWinningText() {
        return winningText;
    }
    public void setWinningText(String winningText) {
        this.winningText = winningText;
    }
}
