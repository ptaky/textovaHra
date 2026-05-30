package Screens;

import Engine.Game;

import javax.swing.*;
import java.awt.*;

public class MainMenu_Screen extends JFrame {

    private final int width = 800;
    private final int height = 600;

    public MainMenu_Screen() {
        init();

        setupUI();
    }

    private void init() {
        setTitle("Boreas");
        setSize(width, height);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

//        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void setupUI() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("BOREAS");

        title.setFont(new Font("Courier New", Font.BOLD, 56));
        title.setForeground(Color.CYAN);

        JButton startBtn = createButton("NEW GAME");
        JButton exitBtn = createButton("EXIT");

        startBtn.addActionListener(e -> startGame());
        exitBtn.addActionListener(e -> System.exit(0));

        // TITLE

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 60, 0);

        panel.add(title, gbc);

        // START BUTTON

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);

        panel.add(startBtn, gbc);

        // EXIT BUTTON

        gbc.gridy = 2;
        panel.add(exitBtn, gbc);

        add(panel);
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Courier New", Font.BOLD, 22));

        button.setForeground(Color.CYAN);
        button.setBackground(Color.DARK_GRAY);

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 60));

        return button;
    }

    private void startGame() {

        dispose();

        new Load_Screen();

        System.out.println("Starting game...");
    }
}