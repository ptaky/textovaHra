package New.Engine;

import New.Entities.Player;
import New.Screens.GameScreen;

import java.awt.*;

public class Game implements Runnable {

    public static final int WIDTH = 1280, HEIGHT = 800;

    private GamePanel gamePanel;
    private GameScreen gameScreen;
    private Thread thread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameScreen = new GameScreen(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    public void initClasses() {
        player = new Player(WIDTH/2, HEIGHT/2);
    }

    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        player.update();
    }

    public void render(Graphics g) {
        player.render(g);
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaF = 0;

        double timePerUpdate = 1000000000.0 / UPS_SET;
        int updates = 0;
        long lastUpdate = System.nanoTime();
        double deltaU = 0;


        while (true) {
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

    public void windowsFocusLost() {
        player.resetDirBooleans();
    }

    // getters & setters
    public Player getPlayer() {
        return player;
    }
}
