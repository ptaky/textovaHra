package Data;

import Engine.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static Data.Constants.GameStates.*;
import static Engine.Game.*;
import static Entities.Player.*;

public class RoomManager {

    private Game game;
    private HashMap<String, BufferedImage> roomBackgrounds;

    private HashMap<String, HashMap<String, String>> roomTransitions;

    private final int border = TILE_SIZE;

    public RoomManager(Game game) {
        this.game = game;
        this.roomBackgrounds = new HashMap<>();
        this.roomTransitions = new HashMap<>();

        preloadRoomBackgrounds();
        loadRoomTransitions();
    }

    private void preloadRoomBackgrounds() {
        System.out.println("--- Načítání grafických pozadí místností stanice Boreas ---");
        for (Room room : game.getRooms().values()) {
            String imgPath = room.getImg();
            if (imgPath != null && !imgPath.isEmpty()) {
                try {
                    BufferedImage bg = DataLoader.loadImage(imgPath);
                    roomBackgrounds.put(room.getId(), bg);
                    System.out.println("Úspěšně načteno pozadí pro: " + room.getName() + " (" + imgPath + ")");
                } catch (Exception e) {
                    System.err.println("Chyba při načítání obrázku " + imgPath + " pro místnost " + room.getId());
                }
            }
        }
    }

    public void update() {
//        checkRoomTransitions();
    }

    public void draw(Graphics g) {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom == null) return;

        BufferedImage bgImg = roomBackgrounds.get(currentRoom.getId());
        if (bgImg != null) {
            g.drawImage(bgImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString("Chybí textura pozadí pro: " + currentRoom.getName(), 50, GAME_HEIGHT / 2);
        }

        if (currentRoom.isExplored()) {
            g.setColor(new Color(0, 255, 255, 100));
            g.setFont(new Font("Courier New", Font.PLAIN, 12));

            int yOffset = 100;
            if (!currentRoom.getItems().isEmpty()) {
                g.drawString("DETEKoVÁNY PŘEDMĚTY NA ZEMI: " + currentRoom.getItems(), 40, yOffset);
                yOffset += 20;
            }
            if (!currentRoom.getNpcs().isEmpty()) {
                g.drawString("BIOLOGICKÁ PŘÍTOMNOST: " + currentRoom.getNpcs(), 40, yOffset);
            }
        }
    }

    public void tryTransition() {

        Room currentRoom = game.getCurrentRoom();

        if (currentRoom == null) return;

        float x = game.getPlayer().getX();
        float y = game.getPlayer().getY();

        String roomId = currentRoom.getId();

        HashMap<String, String> exits = roomTransitions.get(roomId);

        if (exits == null) return;

        if (y <= border && exits.containsKey("up")) {
            changeRoom(exits.get("up"), "up");
        }

        else if (y + PLAYER_SIZE >= GAME_HEIGHT - border
                && exits.containsKey("down")) {

            changeRoom(exits.get("down"), "down");
        }

        else if (x <= border && exits.containsKey("left")) {
            changeRoom(exits.get("left"), "left");
        }

        else if (x + PLAYER_SIZE >= GAME_WIDTH - border
                && exits.containsKey("right")) {

            changeRoom(exits.get("right"), "right");
        }
    }

    private void changeRoom(String targetRoomId, String direction) {

        Room targetRoom = game.getRooms().get(targetRoomId);

        if (targetRoom == null) return;
        if (targetRoom.isLocked()) {
            game.showPopup("tahle místnost je zamčená");
            return;
        }
        if (targetRoomId.equals("karantena") && !game.getPlayerInventory().contains(game.getPlayerInventory().getItemById("plynova_maska"))) {
            game.setGameState(DEFEATED);
            return;
        }

        game.setCurrentRoom(targetRoom);

        switch (direction) {

            case "up" -> {
                game.getPlayer().setY(Game.GAME_HEIGHT - PLAYER_SIZE);
            }

            case "down" -> {
                game.getPlayer().setY(0);
            }

            case "left" -> {
                game.getPlayer().setX(GAME_WIDTH - PLAYER_SIZE);
            }

            case "right" -> {
                game.getPlayer().setX(0);
            }
        }
    }

    private void addTransition(String from, String direction, String to) {

        roomTransitions.putIfAbsent(from, new HashMap<>());
        roomTransitions.get(from).put(direction, to);
    }

    private void loadRoomTransitions() {

        addTransition("kryokomora", "up", "lekarsky_trakt");

        addTransition("lekarsky_trakt", "down", "kryokomora");
        addTransition("lekarsky_trakt", "up", "chodba");
        addTransition("lekarsky_trakt", "right", "dilna");

        addTransition("chodba", "down", "lekarsky_trakt");
        addTransition("chodba", "up", "karantena");
        addTransition("chodba", "right", "botanicka_zahrada");

        addTransition("karantena", "down", "chodba");
        addTransition("karantena", "up", "vysilaci_vez");
        addTransition("karantena", "right", "serverovna");

        addTransition("vysilaci_vez", "down", "karantena");

        addTransition("dilna", "left", "lekarsky_trakt");
        addTransition("botanicka_zahrada", "left", "chodba");
        addTransition("serverovna", "left", "karantena");
    }
}