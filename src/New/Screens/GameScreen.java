package New.Screens;

import New.Engine.Game;
import New.Engine.GamePanel;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameScreen {
    private JFrame frame;

    public GameScreen(GamePanel gp) {
        frame = new JFrame("Stanice Boreas - Grafická Edice");
        init(gp);
    }

    public void init(GamePanel gp) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gp); // Přidáme herní panel

        frame.pack(); // Automaticky přizpůsobí velikost okna podle GamePanelu (včetně okrajů OS)

        frame.setLocationRelativeTo(null); // Vycentrování na střed obrazovky
        frame.setResizable(false); // Zamezení změny velikosti, aby se nerozbil tile grid
        frame.setVisible(true);

        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                gp.getGame().windowsFocusLost(); // Zastaví pohyb hráče při přepnutí okna
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                // Volitelné: Obnovení prvků po návratu do okna
            }
        });
    }
}