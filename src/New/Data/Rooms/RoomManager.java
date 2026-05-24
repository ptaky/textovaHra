package New.Data.Rooms;

import New.Engine.Game;
import Old.Data.Room;
import New.Data.DataLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

//TODO nefunguje prechod mezi roomkama
public class RoomManager {

    private Game game;
    private HashMap<String, BufferedImage> roomBackgrounds;

    public RoomManager(Game game) {
        this.game = game;
        this.roomBackgrounds = new HashMap<>();
        preloadRoomBackgrounds();
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
        checkRoomTransitions();
    }

    public void draw(Graphics g) {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom == null) return;

        BufferedImage bgImg = roomBackgrounds.get(currentRoom.getId());
        if (bgImg != null) {
            g.drawImage(bgImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString("Chybí textura pozadí pro: " + currentRoom.getName(), 50, Game.GAME_HEIGHT / 2);
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

    private void checkRoomTransitions() {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom == null) return;

        float playerX = game.getPlayer().getX();
        float playerY = game.getPlayer().getY();
        String currentRoomId = currentRoom.getId();

        if (playerX > Game.GAME_WIDTH - Game.TILE_SIZE) {
            switch (currentRoomId) {
                case "lekarsky_trakt" -> tryTransition("dilna", "right");
                case "chodba" -> tryTransition("botanicka_zahrada", "right");
                case "karantena" -> tryTransition("serverovna", "right");
                default -> game.getPlayer().setX(Game.GAME_WIDTH - Game.TILE_SIZE);
            }
        }
        else if (playerX < 0) {
            switch (currentRoomId) {
                case "dilna" -> tryTransition("lekarsky_trakt", "left");
                case "botanicka_zahrada" -> tryTransition("chodba", "left");
                case "serverovna" -> tryTransition("karantena", "left");
                default -> game.getPlayer().setX(0);
            }
        }
        else if (playerY > Game.GAME_HEIGHT - Game.TILE_SIZE) {
            switch (currentRoomId) {
                case "vysilaci_vez" -> tryTransition("karantena", "down");
                case "karantena" -> tryTransition("chodba", "down");
                case "chodba" -> tryTransition("lekarsky_trakt", "down");
                case "lekarsky_trakt" -> tryTransition("kryokomora", "down");
                default -> game.getPlayer().setY(Game.GAME_HEIGHT - Game.TILE_SIZE);
            }
        }
        else if (playerY < 0) {
            switch (currentRoomId) {
                case "kryokomora" -> tryTransition("lekarsky_trakt", "up");
                case "lekarsky_trakt" -> tryTransition("chodba", "up");
                case "chodba" -> tryTransition("karantena", "up");
                case "karantena" -> tryTransition("vysilaci_vez", "up");
                default -> game.getPlayer().setY(0);
            }
        }
    }

    private void tryTransition(String targetRoomId, String direction) {
        Room targetRoom = game.getRooms().get(targetRoomId);
        if (targetRoom != null) {
            if (!targetRoom.isLocked()) {
                game.setCurrentRoom(targetRoom);
                switch (direction) {
                    case "up" -> game.getPlayer().setY(Game.GAME_HEIGHT - Game.TILE_SIZE - 5);
                    case "down" -> game.getPlayer().setY(5);
                    case "left" -> game.getPlayer().setX(Game.GAME_WIDTH - Game.TILE_SIZE - 5);
                    case "right" -> game.getPlayer().setX(5);
                }
            } else {
                switch (direction) {
                    case "up" -> game.getPlayer().setY(5);
                    case "down" -> game.getPlayer().setY(Game.GAME_HEIGHT - Game.TILE_SIZE - 5);
                    case "left" -> game.getPlayer().setX(5);
                    case "right" -> game.getPlayer().setX(Game.GAME_WIDTH - Game.TILE_SIZE - 5);
                }
            }
        }
    }
}