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
import java.util.Map;

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

    private GamePanel gamePanel;
    private Game_Screen gameScreen;
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

    // --- Nacitani hry ---
    private int loadingProgress = 0;
    private boolean loadingFinished = false;

    // --- Příběhové texty ---
    private String introduction;
    private String winningText;
    private String losingText;

    public Game() {
        setupBoreasGameData();
    }

    public void startRealPlaying() {
        this.gamePanel = new GamePanel(this);
        this.gameScreen = new Game_Screen(gamePanel);

        this.gameScreen.setVisible(true);
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

            if (gameState == VICTORY || gameState == DEFEATED || gameState == PAUSED || gameState == HELP) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ---------- AKTUALIZACE A VYKRESLOVÁNÍ ----------

    public void update() {
        if (gameState == VICTORY || gameState == DEFEATED || gameState == HELP || gameState == PAUSED) {
            return;
        }

        if (gameState == RUNNING) {
            roomManager.update();
            player.update();
            for (NPCEntity npc : npcEntities) {
                npc.update();
            }
        }
    }

    public void render(Graphics g) {
        switch (gameState) {
            case LOADING -> drawLoadingScreen(g);
            case RUNNING -> {
                roomManager.draw(g);
                drawNPCs(g);
                player.render(g);
                drawHUD(g);
            }
            case HELP -> drawHelpScreen(g);
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

    // ---------- POMOCNÉ METODY PRO ----------

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
        g.drawString("Press R to Restart", GAME_WIDTH / 2 - 100, GAME_HEIGHT - 150);
        g.drawString("Press ESC for Main Menu", GAME_WIDTH / 2 - 130, GAME_HEIGHT - 120);
    }

    private void drawLoadingScreen(Graphics g) {
        // Černé pozadí
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Nadpis Loading
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.CYAN);
        g.drawString("INITIALIZING BOREAS SYSTEMS...", 50, 80);

        // --- SEKCE: OVLÁDÁNÍ A VSTUPY ---
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("PŘEHLED OVLÁDÁNÍ:", 50, 150);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        int y = 190;
        String[][] controls = {
                {"W, A, S, D", "Pohyb postavou po stanici"},
                {"TAB", "Otevřít / Zavřít inventář a okolí"},
                {"W / S (v inv.)", "Pohyb v seznamech předmětů"},
                {"A / D (v inv.)", "Přepínání mezi Inventářem a Zemí"},
                {"SPACE (v inv.)", "Zvednout předmět ze země / Položit na zem"},
                {"E (v inv.)", "Použít vybraný předmět (např. klíče, baterie)"},
                {"ESC", "Pauza hry / Hlavní menu"}
        };

        for (String[] control : controls) {
            g.setColor(Color.CYAN);
            g.drawString(control[0], 50, y);
            g.setColor(Color.WHITE);
            g.drawString(" - " + control[1], 200, y);
            y += 30;
        }

        // --- PROGRESS BAR ---
        int barWidth = GAME_WIDTH - 100;
        int barHeight = 25;
        int barX = 50;
        int barY = GAME_HEIGHT - 120;

        // Rám progres baru
        g.setColor(Color.GRAY);
        g.drawRect(barX, barY, barWidth, barHeight);

        // Výplň progres baru
        g.setColor(Color.CYAN);
        int currentBarWidth = (int) ((loadingProgress / 100.0) * barWidth);
        g.fillRect(barX + 2, barY + 2, currentBarWidth - 3, barHeight - 3);

        // Text procent / pokynu
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();

        if (!loadingFinished) {
            g.setColor(Color.WHITE);
            String progText = "Načítání: " + loadingProgress + "%";
            g.drawString(progText, GAME_WIDTH / 2 - fm.stringWidth(progText) / 2, barY - 15);
        } else {
            // Blikající nebo výrazný text pro spuštění
            g.setColor(Color.GREEN);
            String startText = "SYSTÉMY PŘIPRAVENY. STISKNI MEZERNÍK PRO VSTUP NA STANICI.";
            g.drawString(startText, GAME_WIDTH / 2 - fm.stringWidth(startText) / 2, barY - 15);
        }
    }

    private void drawHelpScreen(Graphics g) {
        // Vykreslíme hru na pozadí, ať to vypadá dobře
        roomManager.draw(g);
        player.render(g);

        // Ztmavovací filtr přes celé okno
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Ohraničení panelu nápovědy
        g.setColor(Color.CYAN);
        g.drawRoundRect(100, 50, GAME_WIDTH - 200, GAME_HEIGHT - 120, 20, 20);
        g.setColor(new Color(10, 20, 30, 240));
        g.fillRoundRect(101, 51, GAME_WIDTH - 202, GAME_HEIGHT - 122, 20, 20);

        // Titulek
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.CYAN);
        g.drawString("MANUÁL STANICE BOREAS", 140, 95);

        // Seznam příkazů
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        int y = 150;
        String[][] helpCommands = {
                {"W, A, S, D", "Pohyb astronautky po stanici"},
                {"E", "Průzkum místnosti (odhalí skryté předměty)"},
                {"F", "Interakce / Rozhovor s NPC postavou poblíž"},
                {"TAB", "Otevření / Zavření inventáře"},
                {"SPACE", "Přesun předmětu (v inv.) / Průchod dveřmi (ve hře)"},
                {"H", "Zavřít nápovědu a vrátit se do hry"},
                {"ESC", "Zavřít nápovědu / Otevřít pauza menu"}
        };

        for (String[] cmd : helpCommands) {
            g.setColor(Color.CYAN);
            g.drawString(cmd[0], 140, y);
            g.setColor(Color.WHITE);
            g.drawString(" - " + cmd[1], 280, y);
            y += 32;
        }

        // Centrování ukončovací hlášky naspodu panelu
        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.setColor(Color.GRAY);
        String footer = "Stiskni H nebo ESC pro návrat do hry...";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(footer, GAME_WIDTH / 2 - fm.stringWidth(footer) / 2, GAME_HEIGHT - 100);
    }

    public void restartGame() {
        gameState = LEAVE;

        if (this.gameScreen != null) {
            this.gameScreen.dispose();
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            new Screens.Load_Screen();
        });
    }

    public void backToMenu() {
        gameState = LEAVE; // Zastaví herní smyčku

        if (this.gameScreen != null) {
            this.gameScreen.dispose(); // Zničí okno hry
        }

        // Vrátí hráče do hlavního menu
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainMenu_Screen();
        });
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

            selectingInventory = true;
            inventorySelection = 0;
            groundSelection = 0;

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
                "W/S = MOVE | A/D = SWITCH | SPACE = TRANSFER | TAB = CLOSE | E = USE",
                GAME_WIDTH / 2 - 260,
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
            int maxItems = playerInventory.getItems().size();
            if (inventorySelection < maxItems - 1) {
                inventorySelection++;
            }
        } else {
            int maxGround = currentRoom.getItems().size();
            if (groundSelection < maxGround - 1) {
                groundSelection++;
            }
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

            // POJISTKA PROTI CHYBNÉMU INDEXU PŘED AKCÍ
            if (inventorySelection >= playerInventory.getItems().size()) {
                inventorySelection = Math.max(0, playerInventory.getItems().size() - 1);
            }

            Item item = playerInventory.getItems().get(inventorySelection);
            playerInventory.removeItem(item);
            currentRoom.addItem(item.getId());

            // CHYTRÁ KOREKCE INDEXU: Pokud index po smazání přečnívá přes nový konec listu, posuneme ho na konec
            if (inventorySelection >= playerInventory.getItems().size()) {
                inventorySelection = Math.max(0, playerInventory.getItems().size() - 1);
            }
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

            // POJISTKA PROTI CHYBNÉMU INDEXU PŘED AKCÍ
            if (groundSelection >= currentRoom.getItems().size()) {
                groundSelection = Math.max(0, currentRoom.getItems().size() - 1);
            }

            String itemId = currentRoom.getItems().get(groundSelection);
            currentRoom.removeItem(itemId);

            Item realItem = items.get(itemId);
            playerInventory.addItem(realItem);

            // CHYTRÁ KOREKCE INDEXU: Pokud index po smazání přečnívá přes nové položky na zemi, srovnáme ho
            if (groundSelection >= currentRoom.getItems().size()) {
                groundSelection = Math.max(0, currentRoom.getItems().size() - 1);
            }
        }
    }

    // ---------- ITEMS LOGIKA ----------

    public void useItem() {
        if (gameState != INVENTORY) return;

        if (selectingInventory) {
            if (playerInventory.getItems().isEmpty()) return;

            Item item = playerInventory.getItems().get(inventorySelection);

            if (item == null) {
                showPopup("Tenhle předmět nemáš v inventáři.");
                return;
            }
            if (!item.isUsable()) {
                showPopup("Tenhle předmět se teď nedá použít.");
                return;
            }

            String action = item.getUseAction();
            Map<String, Object> effects = item.getEffects();
            String result;

            switch (action) {
                case "repair_drone" -> result = useRepairDroneAction(currentRoom, effects);
                case "restore_power" -> result = useRestorePowerAction(currentRoom, effects);
                case "install_uv_lamp" -> result = useInstallUVLampAction(currentRoom, effects);
                case "unlock_server_room" -> result = useUnlockServerRoomAction(currentRoom, effects);
                case "sleep_target" -> result = useAffectTargetAction(currentRoom, effects, "sleep");
                case "confuse_target" -> result = useAffectTargetAction(currentRoom, effects, "confuse");
                case "activate_signal" -> result = useActivateSignalAction(currentRoom, effects);
                default -> result = "ERROR: Tohle použití ještě není implementované: " + action;
            }

            if (result != null && !result.startsWith("ERROR:")) {
                if (item.isConsumable() || item.isSingleUse()) {
                    playerInventory.removeItem(item);
                    if (inventorySelection > 0) {
                        inventorySelection--;
                    }
                }
                if (gameState != VICTORY && gameState != DEFEATED) {
                    gameState = RUNNING;
                    showPopup(result);
                } else {
                    popupTimer = 0;
                    gamePanel.repaint();
                }
                showPopup(result);
            } else {
                String cleanError = result.replace("ERROR: ", "");
                showPopup(cleanError);
            }
        }
    }

    private String useRepairDroneAction(Room room, Map<String, Object> effects) {
        String npcId = normalizeNpcId(effectString(effects, "repairNpc"));
        if (npcId == null) return "ERROR: Item nemá nastavené repairNpc.";

        Entities.NPCEntity sparkEntity = getNPCById(npcId);
        if (sparkEntity == null || !room.getId().equals(sparkEntity.getNpc().getLocation()) || !isPlayerNearEntity(sparkEntity)) {
            return "ERROR: Tady to nemáš do čeho zapojit (jdi blíž k dronovi).";
        }

        NPC npc = NPCs.get(npcId);
        if (npc == null) return "ERROR: Chyba dat: NPC '" + npcId + "' neexistuje.";

        if (npc.isRepaired()) {
            return npc.getNickname() + " už je opravený.";
        }

        npc.setIsRepaired(true);
        setCheckpoint(1);
        decreaseTimeLeft();

        return npc.getNickname() + ": " + pickDialogue(npc, "repaired", "broken");
    }
    private String useRestorePowerAction(Room room, Map<String, Object> effects) {
        boolean restore = effectBoolean(effects, "restoreElectricity");
        if (!restore) return "ERROR: Item neumí obnovit elektřinu.";

        if (!"chodba".equals(room.getId())) {
            return "ERROR: Pojistky dávají smysl použít v chodbě u rozvaděče.";
        }

        setCheckpoint(2);
        decreaseTimeLeft();

        return "Vyměnil/a jsi pojistky. Nouzové osvětlení zesílí a stanice plně ožije.";
    }
    private String useInstallUVLampAction(Room room, Map<String, Object> effects) {
        String npcId = normalizeNpcId(effectString(effects, "satisfyNpc"));
        if (npcId == null) return "ERROR: Item nemá nastavené satisfyNpc.";

        if (!"botanicka_zahrada".equals(room.getId())) {
            return "ERROR: UV lampu je nejlepší použít v botanické zahradě.";
        }

        Entities.NPCEntity babickaEntity = getNPCById(npcId);
        if (babickaEntity == null || !isPlayerNearEntity(babickaEntity)) {
            return "ERROR: Musíš jít blíž k babičce Aničce.";
        }

        NPC babicka = NPCs.get(npcId);
        if (babicka == null) return "ERROR: Chyba dat: NPC '" + npcId + "' neexistuje.";

        if (!babicka.isPlantNeedsLight()) {
            return babicka.getName() + ": " + pickDialogue(babicka, "default", null);
        }

        babicka.setPlantNeedsLight(false);
        setCheckpoint(3);
        decreaseTimeLeft();

        return babicka.getName() + ": " + pickDialogue(babicka, "afterUVLamp", "default");
    }
    private String useUnlockServerRoomAction(Room room, Map<String, Object> effects)  {
        String locationId = effectString(effects, "unlockLocation");
        if (locationId == null) return "ERROR: Item nemá nastavené unlockLocation.";

        if (!"karantena".equals(room.getId())) {
            return "ERROR: Kartu musíš použít u dveří do serverovny (v karanténě).";
        }

        Room target = rooms.get(locationId);
        if (target == null) return "ERROR: Chyba dat: místnost '" + locationId + "' neexistuje.";

        if (!target.isLocked()) return "Serverovna už je odemčená.";

        target.setIsLocked(false);
        return "Píp. Dveře do serverovny se odemknou.";
    }
    private String useAffectTargetAction(Room room, Map<String, Object> effects, String mode) {
        if (!"karantena".equals(room.getId())) {
            return "ERROR: Tohle dává smysl použít v karanténě.";
        }

        String targetId = normalizeNpcId(effectString(effects, "targetNpc"));
        double chance = effectDouble(effects, "successChance", 1.0);

        if (targetId == null) return "ERROR: Item nemá nastavené targetNpc.";

        Entities.NPCEntity viktorEntity = getNPCById(targetId);
        if (viktorEntity == null || !isPlayerNearEntity(viktorEntity)) {
            return "ERROR: Viktor tu není nebo stojíš moc daleko.";
        }

        NPC viktor = NPCs.get(targetId);
        if (viktor == null) return "ERROR: Chyba dat: NPC '" + targetId + "' neexistuje.";

        if (!viktor.isHostile()) {
            String key = "sleep".equals(mode) ? "asleep" : "confused";
            return viktor.getName() + ": " + pickDialogue(viktor, key, "default");
        }

        java.util.Random rnd = new java.util.Random();
        boolean success = rnd.nextDouble() <= chance;
        if (!success) {
            return viktor.getName() + ": " + pickDialogue(viktor, "aggressive", "default");
        }

        viktor.setHostile(false);

        if ("sleep".equals(mode)) {
            return viktor.getName() + ": " + pickDialogue(viktor, "asleep", "confused");
        } else {
            return viktor.getName() + ": " + pickDialogue(viktor, "confused", "aggressive");
        }
    }
    private String useActivateSignalAction(Room room, Map<String, Object> effects)  {
        boolean win = effectBoolean(effects, "winGame");
        if (!win) return "ERROR: Tenhle předmět neumí spustit vysílání.";

        if (!"vysilaci_vez".equals(room.getId())) {
            return "ERROR: Šifrovací kartu musíš použít ve vysílací věži.";
        }

        setPlayerWon(true);

        return "Vložíš šifrovací kartu do terminálu. Antény ožívají a SOS signál je odeslán.";
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
        return npcId == null ? null : npcId.toLowerCase().trim();
    }
    private String pickDialogue(NPC npc, String key, String fallbackKey) {
        if (npc.getDialogues() == null) return "...";
        java.util.List<String> lines = npc.getDialogues().get(key);

        if (lines == null || lines.isEmpty()) {
            if (fallbackKey == null) return "...";
            lines = npc.getDialogues().get(fallbackKey);
        }
        if (lines == null || lines.isEmpty()) return "...";

        java.util.Random rnd = new java.util.Random();
        return lines.get(rnd.nextInt(lines.size()));
    }
    public Entities.NPCEntity getNPCById(String id) {
        for (Entities.NPCEntity entity : npcEntities) {
            if (entity.getNpc() != null && entity.getNpc().getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }
    private boolean isPlayerNearEntity(Entities.NPCEntity entity) {
        float dx = player.getX() - entity.getX();
        float dy = player.getY() - entity.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < 128;
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
        if (npc == null) return;

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

    public boolean isLoadingFinished() {
        return this.loadingFinished;
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
        this.gameState = VICTORY;
    }

    public RoomManager getRoomManager() { return roomManager; }

    public boolean playerLost() { return playerLost; }
    public void setPlayerLost(boolean playerLost) {
        this.playerLost = playerLost;
        this.gameOver = true;
        this.gameState = DEFEATED;
    }

    public String getIntroduction() { return introduction; }
    private void setIntroduction() {
        this.introduction =
                "Ticho. Tma. A pak alarm.\n\n" +
                        "Probouzíš se z kryospánku dřív, než bylo plánováno. Nouzová světla blikají a počítač stanice Boreas chladně oznamuje: posádka – mrtvá. Stabilita jádra planety – kritická.\n" +
                        "Do rozpadu planety zbývá jen 17h 32min 42s.\n\n" +
                        "Stanice je bez energie, chodby jsou ponořené do temnoty a něco tu není v pořádku. Jsi tu sama.\n" +
                        "Nebo… skoro sama.\n\n" +
                        "Pokud se ti v čas nepodaří zprovoznit sysémy a odeslat SOS signál, Boreas – i ty – zmizíte v explozi.\n" +
                        "Čas běží. Každé rozhodnutí se počítá.\n\n" +
                        "Vítej na stanici Boreas.";
    }

    public String getWinningText() { return winningText; }
    private void setWinningText() {
        this.winningText =
                "Signál odeslán.\n\n" +
                        "Anténa se probouzí k životu a stanice se po dlouhé době znovu rozzáří.\n" +
                        "Nouzový signál míří do hlubokého vesmíru – a tentokrát nezůstane bez odpovědi.\n" +
                        "\n" +
                        "Záchrana je na cestě.\n" +
                        "Stanice Boreas žije – díky tobě.";
    }

    public String getLosingText() { return losingText; }
    private void setLosingText() {
        this.losingText = "Vešel jsi do mistnosti, kde je jedovatý plyn a nemáš plynovou masku;;.";
    }
}