package Old.Screens;

import javax.swing.*;
import java.awt.*;

public class TitleScreen {
    private JFrame frame;

    public TitleScreen() {
        frame = new JFrame("textovaHra - Boreas");
        init();
    }

    public void init() {
        JLabel label = new JLabel("vitej na stanici Boreas", JLabel.CENTER);
        JButton button = new JButton("START");

        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(label, BorderLayout.NORTH);

        frame.add(button, BorderLayout.CENTER);
        button.setForeground(Color.PINK);
        button.setBackground(new Color(143, 41, 172));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            new AppScreen();
            frame.dispose();
        });


    }
}
