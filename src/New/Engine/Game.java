package New.Engine;

import New.Screens.GameScreen;

public class Game implements Runnable {

    public static final int WIDTH = 1280, HEIGHT = 800;
    public static final int DELTA_MOVE_VALUE = 6;

    private GamePanel gamePanel;
    private GameScreen gameScreen;
    private Thread thread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public Game() {
        gamePanel = new GamePanel();
        gameScreen = new GameScreen(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        gamePanel.updateGame();
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
}
