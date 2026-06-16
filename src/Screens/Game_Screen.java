package Screens;

import Engine.GamePanel;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**
 * The main top-level window frame container for the Boreas application.
 * Extends JFrame to initialize OS-level window boundaries, mount the primary canvas panel,
 * and handle window focus state shifts to prevent character controller locking.
 * @author Ondřej Ptáček
 */
public class Game_Screen extends JFrame {

    /**
     * Constructs the primary viewport frame window wrapper.
     * @param gp reference to the GamePanel rendering engine canvas to embed
     */
    public Game_Screen(GamePanel gp) {

        init(gp);
    }

    /**
     * Initializes hardware window criteria, frame descriptions, close parameters,
     * component packing and registers target focus listeners.
     * @param gp reference to the GamePanel rendering engine canvas to embed
     */
    public void init(GamePanel gp) {
        setTitle("Boreas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(gp);

        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                gp.getGame().windowsFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {

            }
        });
    }
}