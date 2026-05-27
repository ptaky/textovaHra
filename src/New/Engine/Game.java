package New.Engine;

import New.Data.RoomManager;
import New.Entities.Player;
import New.Screens.EscMenu_Screen;
import New.Screens.Game_Screen;
import New.Screens.MainMenu_Screen;
import Old.Data.*;

import java.awt.*;
import java.awt.Color;
import java.util.HashMap;

import static New.Data.Constants.GameStates.*;

public class Game implements Runnable {

    public static final int DEFAULT_TILE_SIZE = 32;
    public static final float SCALE = 1.5f;
    public static final int TILES_WIDTH = 26;
    public static final int TILES_HEIGHT = 14;
    public static final int TILE_SIZE = (int)(DEFAULT_TILE_SIZE * SCALE);
    public static final int GAME_WIDTH = TILE_SIZE * TILES_WIDTH;
    public static final int GAME_HEIGHT = TILE_SIZE * TILES_HEIGHT;

    private GamePanel gamePanel;
    private Game_Screen gameScreen;
    private Thread thread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private RoomManager roomManager;

    private Inventory playerInventory;
    private int checkpoint;
    private int timeLeft;
    private HashMap<String, Room> rooms;
    private Room currentRoom;
    private HashMap<String, NPC> NPCs;
    private HashMap<String, Item> items;

    private int gameState;

    private String popupText;
    private int popupTimer;

    private boolean gameOver;
    private boolean playerWon;
    private boolean playerLost;

    private String introduction;
    private String winningText;
    private String losingText;

    public Game() {
        setupBoreasGameData();

        this.gamePanel = new GamePanel(this);
        this.gameScreen = new Game_Screen(gamePanel);
        this.gamePanel.requestFocus();

        startGameLoop();
    }

    private void setupBoreasGameData() {
        DataLoader dataLoader = new DataLoader();
        rooms = dataLoader.loadRoomsData();
        NPCs = dataLoader.loadNPCData();
        items = dataLoader.loadItemsData();

        currentRoom = rooms.get("kryokomora");
        playerInventory = new Inventory();
        gameOver = false;
        checkpoint = 0;
        timeLeft = 0;

        gameState = RUNNING;

        player = new Player(GAME_WIDTH / 2f - (TILE_SIZE / 2f), GAME_HEIGHT / 2f - (TILE_SIZE / 2f), this);
        roomManager = new RoomManager(this);

        setIntroduction();
        setWinningText();
        setLosingText();
    }

    public void update() {
        if (gameState == RUNNING) {
            roomManager.update();
            player.update();

            if (currentRoom != null && currentRoom.getId().equals("vysilaci_vez")) {
                if (playerInventory.getItemById("sifrovaci_karta") != null) {
                    setPlayerWon(true);
                }
            }
        }
    }

    public void render(Graphics g) {

        switch (gameState) {

            case RUNNING -> {
                roomManager.draw(g);
                player.render(g);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 14));

                if (currentRoom != null) {
                    g.drawString("MÍSTNOST: " + currentRoom.getName().toUpperCase(), 20, 35);
                }

                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString(getLeftTime(), 20, 55);
                g.drawString("Inventář: " + playerInventory.toString(), 20, 75);

                // popup text
                if (popupTimer > 0) {

                    g.setColor(new Color(0, 0, 0, 170));
                    g.fillRoundRect(GAME_WIDTH / 2 - 200, GAME_HEIGHT - 100, 400, 50, 20, 20);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 20));

                    FontMetrics fm = g.getFontMetrics();

                    int textWidth = fm.stringWidth(popupText);

                    g.drawString(popupText, GAME_WIDTH / 2 - textWidth / 2, GAME_HEIGHT - 68);

                    popupTimer--;
                }
            }

            case PAUSED -> {
                // OPTIONAL: můžeš vykreslit frozen frame + overlay
                roomManager.draw(g);
                player.render(g);

                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

                g.setColor(Color.CYAN);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("PAUSED", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
            }

            case INVENTORY -> {
                roomManager.draw(g);
                player.render(g);

                drawInventory(g);
            }

            case DEFEATED -> drawEndScreen(g, false);

            case VICTORY -> drawEndScreen(g, true);
        }
    }

    private void drawEndScreen(Graphics g, boolean win) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        int yOffset = GAME_HEIGHT / 2 - 120;

        if (win) {
            g.setColor(Color.GREEN);
            g.drawString("MISE SPLNĚNA!", GAME_WIDTH / 2 - 130, yOffset);

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.setColor(Color.WHITE);

            yOffset += 50;
            for (String line : winningText.split("\n")) {
                g.drawString(line, GAME_WIDTH / 2 - 350, yOffset);
                yOffset += 25;
            }

        } else {
            g.setColor(Color.RED);
            g.drawString("MISE SELHALA", GAME_WIDTH / 2 - 120, yOffset);

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.setColor(Color.WHITE);

            yOffset += 50;
            for (String line : losingText.split("\n")) {
                g.drawString(line, GAME_WIDTH / 2 - 350, yOffset);
                yOffset += 25;
            }
        }

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString("Press R to Restart", GAME_WIDTH / 2 - 100, GAME_HEIGHT - 120);
        g.drawString("Press ESC for Main Menu", GAME_WIDTH / 2 - 130, GAME_HEIGHT - 90);
    }

    public void restartGame() {

        gameState = LEAVE;

        gameScreen.dispose();

        new Game();
    }

    public void backToMenu() {

        gameState = LEAVE;

        gameScreen.dispose();

        new MainMenu_Screen();
    }

    public void pauseGame() {
        gameState = PAUSED;

        EscMenu_Screen escScreen = new EscMenu_Screen(
                () -> {
                    gameState = RUNNING;
                    gamePanel.requestFocus();
                },
                () -> {
                    gameState = LEAVE;
                    gameScreen.dispose();
                    new MainMenu_Screen();
                }

        );

        escScreen.setVisible(true);
    }

    public void showPopup(String text) {
        this.popupText = text;
        this.popupTimer = 180;
    }

    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void toggleInventory() {

        if (gameState == RUNNING) {

            gameState = INVENTORY;

        } else if (gameState == INVENTORY) {

            gameState = RUNNING;
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long lastUpdate = System.nanoTime();
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;
        int frames = 0;
        int updates = 0;

        while (gameState != LEAVE) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - lastUpdate) / timePerUpdate;
            deltaF += (currentTime - lastUpdate) / timePerFrame;
            lastUpdate = currentTime;

            while (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    private void drawInventory(Graphics g) {

        // dark overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // inventory window
        g.setColor(Color.BLACK);
        g.fillRoundRect(
                GAME_WIDTH / 2 - 250,
                GAME_HEIGHT / 2 - 180,
                500,
                360,
                25,
                25
        );

        // border
        g.setColor(Color.CYAN);
        g.drawRoundRect(
                GAME_WIDTH / 2 - 250,
                GAME_HEIGHT / 2 - 180,
                500,
                360,
                25,
                25
        );

        // title
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(
                "INVENTORY",
                GAME_WIDTH / 2 - 95,
                GAME_HEIGHT / 2 - 130
        );

        // items
        g.setFont(new Font("Arial", Font.PLAIN, 18));

        int y = GAME_HEIGHT / 2 - 80;

        if (playerInventory.isEmpty()) {
            g.drawString("Inventory is empty", GAME_WIDTH / 2 - 80, y);
        } else {
            for (Item item : playerInventory.getItems()) {
                g.drawString("- " + item.getName(), GAME_WIDTH / 2 - 180, y);
                y += 35;
            }
        }

        // controls
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        g.drawString(
                "Press TAB to close",
                GAME_WIDTH / 2 - 80,
                GAME_HEIGHT / 2 + 145
        );
    }

    public void windowsFocusLost() {
        player.resetDirBooleans();
    }

    public String getLine(boolean withNextLine) {
        String line = "__________________________________________________________________________________";
        if (withNextLine) return line + "\n";
        else return line;
    }

    public String getLeftTime() {
        String txt = "Čas do exploze: ";
        switch (timeLeft) {
            case 0 -> txt += "17h 32min 42s";
            case 1 -> txt += "13h 45min 53s";
            case 2 -> txt += "10h 10min 04s";
            case 3 -> txt += "06h 40min 42s";
            case 4 -> txt += "02h 15min 28s";
            default -> txt += "čas vypršel!";
        }
        return txt;
    }

    public int getGameState() {
        return gameState;
    }
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public boolean roomContains(String itemId) {
        return currentRoom != null && currentRoom.containsItem(itemId);
    }

    public Player getPlayer() { return player; }
    public Inventory getPlayerInventory() { return playerInventory; }

    public int getCheckpoint() { return checkpoint; }
    public void setCheckpoint(int cp) { this.checkpoint = cp; }
    public void decreaseTimeLeft() { timeLeft++; }

    public HashMap<String, Room> getRooms() { return rooms; }
    public Room getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(Room currentRoom) { this.currentRoom = currentRoom; }

    public HashMap<String, NPC> getNPCs() { return NPCs; }
    public HashMap<String, Item> getItems() { return items; }

    public boolean isGameOver() { return gameOver; }
    public void quitGame() { this.gameOver = true; }

    public boolean playerWon() { return playerWon; }
    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
        this.gameOver = true;
        gameState = VICTORY;
    }

    public RoomManager getRoomManager() { return roomManager; }

    public boolean playerLost() { return playerLost; }
    public void setPlayerLost(boolean playerLost) {
        this.playerLost = playerLost;
        this.gameOver = true;
    }

    public String getIntroduction() { return introduction; }
    private void setIntroduction() {
        this.introduction =
                "Ticho. Tma. A pak alarm.\n\n" +
                        "Probouzíš se z kryospánku dřív, než bylo plánováno. Nouzová světla blikají a počítač stanice Boreas chladně oznamuje: posádka – mrtvá. Stabilita jádra planety – kritická.\n" +
                        "Do rozpadu planety zbývá jen 17h 32min 42s.\n\n" +
                        "Stanice je bez energie, chodby jsou ponořené do temnoty a něco tu není v pořádku. Jsi tu sama.\n" +
                        "Nebo… skoro sama.\n\n" +
                        "Pokud se ti v čas nepodaří zprovoznit systémy a odeslat SOS signál, Boreas – i ty – zmizíte v explozi.\n" +
                        "Čas běží. Každé rozhodnutí se počítá.\n\n" +
                        "Vítej na stanici Boreas.";
    }

    public String getWinningText() { return winningText; }
    private void setWinningText() {
        this.winningText =
                "Signál odeslán.\n\n" +
                        "Anténa se probouzí k životu a stanice se po dlouhé době znovu rozzáří. Nouzový signál míří do hlubokého vesmíru – a tentokrát nezůstane bez odpovědi.\n" +
                        "Záchrana je na cestě.\n" +
                        "Stanice Boreas žije – díky tobě.";
    }

    public String getLosingText() { return losingText; }
    private void setLosingText() {
        this.losingText =
                "YOU DIED.\n\n" +
                        "Vešla jsi do místnosti s nebezpečným plynem bez plynové masky a nadýchala jses.\n" +
                        "Tvůj hlas už nikdy nikdo neuslyší.\n\n" +
                        "Ticho.\n" +
                        "Tma.\n\n" +
                        "Mise selhala.";
    }
}