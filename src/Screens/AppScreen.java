package Screens;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppScreen {
    private JFrame frame;
    private DefaultListModel<String> model;
    private JList<String> list;
    private JScrollPane scrollPane;
    private JTextField textField;

    public AppScreen() {
        frame = new JFrame("textovaHra - Boreas");
        model = new DefaultListModel<>();
        list = new JList<>(model);
        scrollPane = new JScrollPane(list);
        textField = new JTextField();
        init();
    }

    public void init() {
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton button = new JButton("pridej");
        JPanel panel = new JPanel(new BorderLayout());

        button.addActionListener(e -> {

        });
        panel.add(button, BorderLayout.EAST);

        textField.setText("zde piste:");
        panel.add(textField, BorderLayout.CENTER);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) model.remove(list.getSelectedIndex());
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                }
            }
        });

        frame.add(panel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    public void addToModel(String input) {
        String text = textField.getText();
        if (!text.isEmpty()) model.addElement(text);
        textField.setText("");


    }
}
