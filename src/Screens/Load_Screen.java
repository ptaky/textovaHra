package Screens;

import Engine.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles the pre-game loading sequence screen layout.
 * Runs an asynchronous background worker thread to parse master core game asset collections,
 * while animating a multi-stage visual simulation progress bar to disguise layout transitions.
 * @author Ondřej Ptáček + AI
 */
// Pomahal mi chat. Vubec totiz tahle trida neni potreba ke spravne funkci, jen se mi nelibila mezera mezi MainMenu a Game
public class Load_Screen extends JFrame {

    private int progress = 0;
    private boolean isFinished = false;
    private boolean gameDataLoaded = false;
    private final Timer timer;

    private Game preloadedGame = null;

    private final int width = Game.GAME_WIDTH;
    private final int height = Game.GAME_HEIGHT;

    /**
     * Initializes the loading screen window frame context.
     * Fires up the asynchronous data preloader thread and schedules the UI tick timer.
     */
    public String load_Screen() { return ""; } // Placeholder signature to preserve class structure format
    public Load_Screen() {
        setTitle("Boreas - Initializing Systems");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        LoadingPanel panel = new LoadingPanel();
        add(panel);

        // --- SKUTEČNÉ NAČÍTÁNÍ NA POZADÍ ---
        Thread loadingThread = new Thread(() -> {
            preloadedGame = new Game();
            gameDataLoaded = true;
        });
        loadingThread.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && isFinished && gameDataLoaded) {
                    timer.stop();
                    dispose();

                    preloadedGame.startRealPlaying();
                }
            }
        });

        timer = new Timer(30, e -> {
            if (progress < 100) {
                if (Math.random() < 0.4) {
                    progress += new java.util.Random().nextInt(3) + 1;
                    if (progress > 100) progress = 100;
                }
                panel.repaint();
            } else if (gameDataLoaded) {
                isFinished = true;
                panel.repaint();
            }
        });

        setVisible(true);
        timer.start();
    }

    /**
     * Internal rendering surface panel responsible for drawing the layout summary control guide
     * and the graphical progress bar fill ratios.
     */
    private class LoadingPanel extends JPanel {

        /**
         * Standard constructor establishing default canvas color properties.
         */
        public LoadingPanel() {
            setBackground(Color.BLACK);
        }

        /**
         * Overridden paint engine block drawing background overlays, operational guide listings,
         * and updating the structural progress gauge components.
         * @param g active global Graphics context brush tracker instance
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.CYAN);
            g.drawString("INITIALIZING BOREAS SYSTEMS...", 50, 80);

            // --- OVLÁDÁNÍ ---
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("PŘEHLED OVLÁDÁNÍ STANICE:", 50, 150);

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            int y = 190;
            String[][] controls = {
                    {"W, A, S, D", "Pohyb astronautky po stanici"},
                    {"TAB", "Otevřít / Zavřít inventář a okolí"},
                    {"W / S (v inv.)", "Pohyb v seznamech předmětů"},
                    {"A / D (v inv.)", "Přepínání mezi Inventářem a Zemí"},
                    {"SPACE (v inv.)", "Zvednout předmět ze země / Položit na zem"},
                    {"E (v inv.)", "Použít vybraný předmět (např. klíče, baterie)"},
                    {"ESC", "Pauza hry / Hlavní menu"},
                    {"H", "Zobrazit nápovědu přímo během hry"}
            };

            for (String[] control : controls) {
                g.setColor(Color.CYAN);
                g.drawString(control[0], 50, y);
                g.setColor(Color.WHITE);
                g.drawString(" - " + control[1], 200, y);
                y += 30;
            }

            // --- PROGRESS BAR ---
            int barWidth = width - 100;
            int barHeight = 25;
            int barX = 50;
            int barY = height - 120;

            g.setColor(Color.GRAY);
            g.drawRect(barX, barY, barWidth, barHeight);

            g.setColor(Color.CYAN);
            int currentBarWidth = (int) ((progress / 100.0) * (barWidth - 4));
            g.fillRect(barX + 2, barY + 2, currentBarWidth, barHeight - 3);

            g.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();

            // Podmínka zobrazení stavu
            if (!isFinished) {
                g.setColor(Color.WHITE);
                String msg = !gameDataLoaded ? "Načítání dat stanice... " + progress + "%" : "Defragmentace paměti... " + progress + "%";
                g.drawString(msg, width / 2 - fm.stringWidth(msg) / 2, barY - 15);
            } else {
                g.setColor(Color.GREEN);
                String startText = "SYSTÉMY PŘIPRAVENY. STISKNI MEZERNÍK PRO VSTUP NA STANICI.";
                g.drawString(startText, width / 2 - fm.stringWidth(startText) / 2, barY - 15);
            }
        }
    }
}