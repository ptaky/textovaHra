package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Provides a pop-up pause menu frame built with Swing components.
 * Employs GridBagLayout for alignment and uses functional interface callbacks
 * to communicate menu state actions back to the core game loop.
 * @author Ondřej Ptáček
 */
public class EscMenu_Screen extends JFrame {

    private Runnable onResume;

    private Runnable onLeave;

    /**
     * Constructs the escape pause menu screen.
     * Registers functional interface triggers to coordinate state switching upon closing.
     * @param onResume operational task sequence to execute when returning to gameplay
     * @param onLeave operational task sequence to execute when exiting back to main menu
     */
    public EscMenu_Screen(Runnable onResume, Runnable onLeave) {
        this.onResume = onResume;
        this.onLeave = onLeave;

        init();
        setupUI();
        setupListeners();
    }

    /**
     * Sets up core window framing criteria, window dimensions, title bar, and centering.
     */
    private void init() {
        setTitle("Paused");
        setSize(400, 300);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setVisible(true);
    }

    /**
     * Constructs layout constraints, color palettes, titles, and control button arrays.
     */
    private void setupUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Courier New", Font.BOLD, 42));
        title.setForeground(Color.CYAN);

        JButton resumeBtn = createButton("RESUME");
        JButton leaveBtn = createButton("LEAVE");

        resumeBtn.addActionListener(e -> resumeGame());
        leaveBtn.addActionListener(e -> {

            dispose();

            if (onLeave != null) {
                onLeave.run();
            }
        });

        // TITLE

        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,50,0);

        panel.add(title, gbc);

        // RESUME

        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,10,0);

        panel.add(resumeBtn, gbc);

        // LEAVE

        gbc.gridy = 2;
        panel.add(leaveBtn, gbc);

        add(panel);
    }

    /**
     * Standardized manufacturing method to produce styled menu control buttons.
     * @param text string caption to display on the button face
     * @return fully configured and padded JButton instance
     */
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Courier New", Font.BOLD, 20));

        button.setForeground(Color.CYAN);
        button.setBackground(Color.DARK_GRAY);

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 55));

        return button;
    }

    /**
     * Hooks event filters to intercept OS close signals or window focus shifts.
     * Prevents game loop hanging by unpausing if the focus is accidentally broken.
     */
    private void setupListeners() {

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                resumeGame();
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                resumeGame();
            }
        });
    }

    /**
     * Dismantles window frames safely and executes the resumption callback routine.
     */
    private void resumeGame() {
        dispose();

        if (onResume != null) {
            onResume.run();
        }
    }

    /**
     * Explicit window disposal endpoint.
     */
    private void leaveGame() {
        dispose();
    }
}