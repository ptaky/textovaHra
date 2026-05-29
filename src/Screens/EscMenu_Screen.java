package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EscMenu_Screen extends JFrame {

    private Runnable onResume;

    private Runnable onLeave;

    public EscMenu_Screen(Runnable onResume, Runnable onLeave) {
        this.onResume = onResume;
        this.onLeave = onLeave;

        init();
        setupUI();
        setupListeners();
    }

    private void init() {
        setTitle("Paused");
        setSize(400, 300);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setVisible(true);
    }

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

    private void resumeGame() {
        dispose();

        if (onResume != null) {
            onResume.run();
        }
    }
    private void leaveGame() {
        dispose();
    }
}