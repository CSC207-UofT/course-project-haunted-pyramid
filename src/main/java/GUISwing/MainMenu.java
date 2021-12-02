package GUISwing;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    public MainMenu(String name, JFrame frame) {
        JLabel welcomeMessage = new JLabel();
        welcomeMessage.setText("Welcome " + name + "!");
        welcomeMessage.setHorizontalTextPosition(JLabel.CENTER);
        welcomeMessage.setVerticalTextPosition(JLabel.TOP);
        welcomeMessage.setForeground(Color.black);
        welcomeMessage.setFont(new Font("MV Boli", Font.ITALIC, 25));
        welcomeMessage.setVerticalAlignment(JLabel.CENTER);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBounds(1444/3, 0, 1444/3, 200);
        frame.add(welcomeMessage);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu("Sean", new MainFrame());
    }
}
