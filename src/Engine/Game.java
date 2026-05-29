package Engine;

import Data.*;
import Entities.NPCEntity;
import Entities.Player;
import Screens.EscMenu_Screen;
import Screens.Game_Screen;
import Screens.MainMenu_Screen;

import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Data.Constants.Colors.*;
import static Data.Constants.GameStates.*;
import static Data.DataLoader.*;

/**
 * Hlavní herní logika a řízení hry Boreas.
 * Spojuje původní textovou logiku s novým grafickým enginem.
 */
public class Game implements Runnable {

    // --- Rozměry a nastavení okna ---
    public static final int DEFAULT_TILE_SIZE = 32;
    public static final float SCALE = 1.5f;
    public static final int TILES_WIDTH = 26;
    public static final int TILES_HEIGHT = 14;
    public static final int TILE_SIZE = (int)(DEFAULT_TILE_SIZE * SCALE);
    public static final int GAME_WIDTH = TILE_SIZE * TILES_WIDTH;
    public static final int GAME_HEIGHT = TILE_SIZE * TILES_HEIGHT;

    private final GamePanel gamePanel;
    private final Game_Screen gameScreen;
    private Thread thread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    // --- Herní objekty a entity ---
    private Player player;
    private RoomManager roomManager;
    private Inventory playerInventory;
    private int checkpoint;
    private int timeLeft;

    private HashMap<String, Room> rooms;
    private Room currentRoom;
    private HashMap<String, NPC> NPCs;
    private List<NPCEntity> npcEntities;
    private HashMap<String, Item> items;

    private int gameState;

    // --- UI a Popups ---
    private String popupText;
    private int popupTimer;

    // --- Výběr v inventáři ---
    private int inventorySelection;
    private int groundSelection;
    private boolean selectingInventory;

    // --- Stavy konce hry ---
    private boolean gameOver;
    private boolean playerWon;
    private boolean playerLost;

    // --- Příběhové texty ---
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

    /**
     * Inicializace herních dat (místnosti, předměty, NPC, hráč).
     */
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

        loadNPCs();

        inventorySelection = 0;
        groundSelection = 0;
        selectingInventory = true;

        player = new Player(GAME_WIDTH / 2f - (TILE_SIZE / 2f), GAME_HEIGHT / 2f - (TILE_SIZE / 2f), this);
        roomManager = new RoomManager(this);

        setIntroduction();
        setWinningText();
        setLosingText();
    }

    // ---------- GAME LOOP ----------

    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
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

    // ---------- AKTUALIZACE A VYKRESLOVÁNÍ ----------

    public void update() {
        if (gameState == RUNNING) {
            roomManager.update();
            player.update();
            for (NPCEntity npc : npcEntities) {
                npc.update();
            }

            // Podmínka vítězství při vstupu do vysílací věže se šifrovací kartou
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
                drawNPCs(g);
                player.render(g);
                drawHUD(g);
            }
            case PAUSED -> {
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

        drawPopup(g);
    }

    // ---------- POMOCNÉ METODY PRO ŘÍZENÍ HRY ----------

    public void windowsFocusLost() {
        player.resetDirBooleans();
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
        this.popupTimer = 120;
    }

    public void drawPopup(Graphics g) {
        if (popupTimer <= 0) return;

        g.setColor(new Color(0, 0, 0, 170));
        g.fillRoundRect(GAME_WIDTH / 2 - 200, GAME_HEIGHT - 100, 400, 50, 20, 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(popupText);

        g.drawString(popupText, GAME_WIDTH / 2 - textWidth / 2, GAME_HEIGHT - 68);
        popupTimer--;
    }

    public void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        if (currentRoom != null) {
            g.drawString("MÍSTNOST: " + currentRoom.getName().toUpperCase(), 20, 35);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(getLeftTime(), 20, 55);
        g.drawString("Inventář: " + playerInventory.toString(), 20, 75);
    }

    private void drawNPCs(Graphics g) {
        for (NPCEntity npcEntity : npcEntities) {
            if (!npcEntity.getNpc().getLocation().equals(currentRoom.getId())) {
                continue;
            }
            npcEntity.render(g);
        }
    }

    // ---------- INVENTÁŘ LOGIKA A VYKRESLOVÁNÍ ----------

    public void toggleInventory() {
        if (gameState == RUNNING) {
            gameState = INVENTORY;
        } else if (gameState == INVENTORY) {
            gameState = RUNNING;
        }
    }

    private void drawInventory(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        int panelWidth = 250;
        int panelHeight = 300;
        int leftX = GAME_WIDTH / 2 - 300;
        int rightX = GAME_WIDTH / 2 + 50;
        int panelY = GAME_HEIGHT / 2 - panelHeight / 2;

        // LEVÝ PANEL (Inventář)
        g.setColor(Color.BLACK);
        g.fillRoundRect(leftX, panelY, panelWidth, panelHeight, 20, 20);
        g.setColor(selectingInventory ? Color.CYAN : Color.GRAY);
        g.drawRoundRect(leftX, panelY, panelWidth, panelHeight, 20, 20);

        // PRAVÝ PANEL (Zem)
        g.setColor(Color.BLACK);
        g.fillRoundRect(rightX, panelY, panelWidth, panelHeight, 20, 20);
        g.setColor(!selectingInventory ? Color.CYAN : Color.GRAY);
        g.drawRoundRect(rightX, panelY, panelWidth, panelHeight, 20, 20);

        // NADPISY
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("Inventory", leftX + 55, panelY + 40);
        g.drawString("Ground", rightX + 70, panelY + 40);

        // PŘEDMĚTY V INVENTÁŘI HRAČE
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        int y = panelY + 80;

        for (int i = 0; i < playerInventory.getItems().size(); i++) {
            Item item = playerInventory.getItems().get(i);
            if (item == null) continue;

            if (selectingInventory && i == inventorySelection) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.WHITE);
            }

            g.drawString(item.getName(), leftX + 30, y);
            y += 30;
        }

        // PŘEDMĚTY NA ZEMI
        y = panelY + 80;
        if (currentRoom.isExplored()) {
            for (int i = 0; i < currentRoom.getItems().size(); i++) {
                String itemId = currentRoom.getItems().get(i);
                Item realItem = items.get(itemId);
                if (realItem == null) continue;

                if (!selectingInventory && i == groundSelection) {
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.drawString(realItem.getName(), rightX + 30, y);
                y += 30;
            }
        } else {
            g.setColor(Color.GRAY);
            g.drawString("Room not explored", rightX + 30, y);
        }

        // NÁPOVĚDA OVLÁDÁNÍ
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString(
                "W/S = MOVE | A/D = SWITCH | SPACE = TRANSFER | TAB = CLOSE",
                GAME_WIDTH / 2 - 210,
                panelY + panelHeight + 40
        );
    }

    public void inventoryUp() {
        if (gameState != INVENTORY) return;

        if (selectingInventory) {
            if (inventorySelection > 0) inventorySelection--;
        } else {
            if (groundSelection > 0) groundSelection--;
        }
    }

    public void inventoryDown() {
        if (gameState != INVENTORY) return;

        if (selectingInventory) {
            if (inventorySelection < playerInventory.getItems().size() - 1) inventorySelection++;
        } else {
            if (groundSelection < currentRoom.getItems().size() - 1) groundSelection++;
        }
    }

    public void selectInventory() { selectingInventory = true; }
    public void selectGround() { selectingInventory = false; }

    public void moveSelectedItem() {
        if (gameState != INVENTORY) return;

        // Z INVENTÁŘE NA ZEM
        if (selectingInventory) {
            if (playerInventory.getItems().isEmpty()) return;

            if (!currentRoom.isExplored()) {
                showPopup("Explore room first!");
                return;
            }

            Item item = playerInventory.getItems().get(inventorySelection);
            playerInventory.removeItem(item);
            currentRoom.addItem(item.getId());

            if (inventorySelection > 0) inventorySelection--;
        }
        // ZE ZEMĚ DO INVENTÁŘE
        else {
            if (!currentRoom.isExplored()) {
                showPopup("Explore room first!");
                return;
            }

            if (currentRoom.getItems().isEmpty()) return;

            if (playerInventory.isFull()) {
                showPopup("Inventory full!");
                return;
            }

            String itemId = currentRoom.getItems().get(groundSelection);
            currentRoom.removeItem(itemId);

            Item realItem = items.get(itemId);
            playerInventory.addItem(realItem);

            if (groundSelection > 0) groundSelection--;
        }
    }

    // ---------- NPC LOGIKA ----------

    public void loadNPCs() {
        npcEntities = new ArrayList<>();

        // --- KONTROLNÍ VÝPISY OBRÁZKŮ ---
        java.awt.image.BufferedImage imgSpark = loadImage(SPARK_SPRITES);
        java.awt.image.BufferedImage imgMilan = loadImage(MILAN_SPRITES);

        System.out.println("[IMG INFO] Spark obrázek: " + (imgSpark != null ? imgSpark.getWidth() + "x" + imgSpark.getHeight() : "NULL!"));
        System.out.println("[IMG INFO] Milan obrázek: " + (imgMilan != null ? imgMilan.getWidth() + "x" + imgMilan.getHeight() : "NULL!"));
        // ---------------------------------

        // SPARK
        NPCEntity spark = new NPCEntity(300, 220, NPCs.get("spark"), loadImage(SPARK_SPRITES));
        npcEntities.add(spark);

        // MILAN
        NPCEntity milan = new NPCEntity(500, 260, NPCs.get("milan"), loadImage(MILAN_SPRITES));
        npcEntities.add(milan);

        // ANIČKA
        NPCEntity anicka = new NPCEntity(420, 250, NPCs.get("babicka"), loadImage(BABICKA_SPRITES));
        npcEntities.add(anicka);

        // VIKTOR
        NPCEntity viktor = new NPCEntity(600, 240, NPCs.get("viktor"), loadImage(VIKTOR_SPRITES));
        npcEntities.add(viktor);
    }

    public NPCEntity getNearbyNPC() {
        for (NPCEntity npc : npcEntities) {
            if (!npc.getNpc().getLocation().equals(currentRoom.getId())) continue;

            float dx = player.getX() - npc.getX();
            float dy = player.getY() - npc.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < 70) {
                return npc;
            }
        }
        return null;
    }

    public void interactWithNPC(NPC npc) {
        switch (npc.getId()) {
            case "spark" -> interactSpark(npc);
            case "milan" -> interactMilan(npc);
            case "babicka" -> interactBabicka(npc);
            case "viktor" -> interactViktor(npc);
        }
    }

    private void interactSpark(NPC npc) {
        if (!playerInventory.hasItem("baterie")) {
            showPopup("Potřebuji novou baterii...");
            return;
        }
        checkpoint = 1;
        showPopup("Systémy drona obnoveny.");
    }

    private void interactMilan(NPC npc) {
        if (playerInventory.hasItem("filtr")) {
            playerInventory.removeItemById("filtr");
            playerInventory.addItem(items.get("plynova_maska"));
            checkpoint = 3;
            showPopup("Vyrobil jsem ti plynovou masku.");
            return;
        }

        if (!playerInventory.hasItem("karta_serverovna")) {
            playerInventory.addItem(items.get("karta_serverovna"));
            showPopup("Tady máš kartu od serverovny.");
            return;
        }

        showPopup("Potřebuješ ještě něco?");
    }

    private void interactBabicka(NPC npc) {
        if (playerInventory.hasItem("uv_lampa")) {
            playerInventory.removeItemById("uv_lampa");
            playerInventory.addItem(items.get("uspavaci_lektvar"));
            showPopup("Děkuji za světlo pro kytičky.");
            return;
        }
        showPopup("Moje kytičky potřebují více světla...");
    }

    private void interactViktor(NPC npc) {
        if (playerInventory.hasItem("uspavaci_lektvar")) {
            showPopup("Viktor usnul.");
            npc.setHostile(false);
            return;
        }

        if (playerInventory.hasItem("rucni_zrcatko")) {
            if (Math.random() < 0.7f) {
                showPopup("Podařilo se ho zmást.");
                npc.setHostile(false);
            } else {
                showPopup("Zrcátko nefungovalo!");
            }
            return;
        }

        showPopup("Viktor tě nepustí dál.");
    }

    // ---------- TEXTOVÉ A FORMÁTOVACÍ FUNKCE (Z KLASICKÉ VERZE) ----------

    public String getLine(boolean withNextLine) {
        String line = "__________________________________________________________________________________";
        if (withNextLine) return line + "\n";
        else return line;
    }

    public String green(String txt) {
        return ANSI_GREEN + txt + ANSI_RESET;
    }

    public String error(String message) {
        return ANSI_RED + "ERROR: " + message + ANSI_RESET;
    }

    public String getInvalidCommand() {
        return error("neplatny prikaz");
    }

    public String roomInfo() {
        if (currentRoom == null) return error("Žádná aktuální místnost!");
        return getLine(false) + '\n' +
                ANSI_GREEN + currentRoom.getName().toUpperCase() + ANSI_RESET + '\n' +
                currentRoom.toString() + '\n' +
                getLeftTime() + '\n' +
                getInventoryText();
    }

    public String getInventoryText() {
        return "= tvuj inv: " + playerInventory.toString();
    }

    public Item getItemById(String id) {
        for (Item item : items.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    // ---------- GETTERY & SETTERY ----------

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

    public int getGameState() { return gameState; }
    public void setGameState(int gameState) { this.gameState = gameState; }

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
        this.gameState = VICTORY;
    }

    public RoomManager getRoomManager() { return roomManager; }

    public boolean playerLost() { return playerLost; }
    public void setPlayerLost(boolean playerLost) {
        this.playerLost = playerLost;
        this.gameOver = true;
        this.gameState = DEFEATED; // Nastaví herní stav na prohru pro vykreslení
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