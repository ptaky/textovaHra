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
    private boolean playerWon;
    private boolean playerLost;
    private String introduction;
    private String winningText;

    public void setup() {
        DataLoader dataLoader = new DataLoader();
        rooms = dataLoader.loadRoomsData();
        NPCs = dataLoader.loadNPCData();
        items = dataLoader.loadItemsData();

        gameOver = false;
        checkpoint = 0;
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

    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
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
