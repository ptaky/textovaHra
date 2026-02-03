package Engine;

import Data.*;

import java.util.List;

public class Game {
    private Player player;
    private int checkpoint;
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
        checkpoint = 0;
    }

    public void quitGame() {
        //TODO dodelat to
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
