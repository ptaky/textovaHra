package New.Screens;

import New.Engine.GamePanel;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class Game_Screen extends JFrame {

    public Game_Screen(GamePanel gp) {

        init(gp);
    }

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