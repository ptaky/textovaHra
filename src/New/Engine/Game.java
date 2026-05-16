package New.Engine;

import New.Screens.GameScreen;

public class Game implements Runnable {

    public static final int WIDTH = 1280, HEIGHT = 800;
    public static final int DELTA_MOVE_VALUE = 6;

    private GamePanel gamePanel;
    private GameScreen gameScreen;
    private Thread thread;
    private final int FPS_SET = 120;

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

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();

        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {

            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame) {

                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
}
