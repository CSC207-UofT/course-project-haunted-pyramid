package GUISwing;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    private JFrame frame;

    public MainMenu(String name, JFrame frame) {
        this.frame = frame;
        JLabel welcomeMessage = new JLabel();
        welcomeMessage.setText("Welcome " + name + "!");
        welcomeMessage.setHorizontalTextPosition(JLabel.CENTER);
        welcomeMessage.setVerticalTextPosition(JLabel.TOP);
        welcomeMessage.setForeground(Color.black);
        welcomeMessage.setFont(new Font("MV Boli", Font.ITALIC, 25));
        welcomeMessage.setVerticalAlignment(JLabel.CENTER);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBounds(1444/3, 0, 1444/3, 200);
        this.frame.add(welcomeMessage);
        this.frame.setVisible(false);
    }

    public void display() {
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu("Sean", new MainFrame());
        mainMenu.display();
    }
}
