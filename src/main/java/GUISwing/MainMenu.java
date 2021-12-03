package GUISwing;

import controllers.CalendarController;
import controllers.EventController;
import controllers.MainController;
import controllers.UserController;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    private JFrame frame;
    private MainController mainController;

    public MainMenu(JFrame frame) {
        //this.mainController = new MainController();
        this.frame = frame;
        JLabel welcomeMessage = new JLabel();
        setUpWelcomeMessage("Sean", welcomeMessage);
        this.frame.add(welcomeMessage);
        this.frame.setVisible(false);
        JPanel calendarPanel = new JPanel();
        calendarPanel.setBounds(444/2, 150, 1000, 500);
        calendarPanel.setBackground(new Color(0, 161, 161));
        this.frame.add(calendarPanel);
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(444/2, 700, 1000, 1444/2);
        menuPanel.setBackground(new Color(233, 161, 161));
        this.frame.add(menuPanel);
        JButton buttonOne = new JButton("1. Profile Setting");
        buttonOne.setPreferredSize(new Dimension(300, 50));
        JButton buttonTwo = new JButton("2. View/Export Calendar");
        buttonTwo.setPreferredSize(new Dimension(300, 50));
        JButton buttonThree = new JButton("3. Add a New Event");
        buttonThree.setPreferredSize(new Dimension(300, 50));
        JButton buttonFour = new JButton("4. View/Modify an Existing Event");
        buttonFour.setPreferredSize(new Dimension(300, 50));
        JButton buttonFive = new JButton("5. Create Repetition of the Existing Events");
        buttonFive.setPreferredSize(new Dimension(300, 50));
        JButton buttonSix = new JButton("6. Export Entire Calendar to iCal File");
        buttonSix.setPreferredSize(new Dimension(300, 50));
        JButton buttonSeven = new JButton("7. Log Out");
        buttonSeven.setPreferredSize(new Dimension(300, 50));
        JButton buttonEight = new JButton("8. Exit");
        buttonEight.setPreferredSize(new Dimension(300, 50));
        menuPanel.add(buttonOne);
        menuPanel.add(buttonTwo);
        menuPanel.add(buttonThree);
        menuPanel.add(buttonFour);
        menuPanel.add(buttonFive);
        menuPanel.add(buttonSix);
        menuPanel.add(buttonSeven);
        menuPanel.add(buttonEight);

    }

    private void setUpWelcomeMessage(String name, JLabel welcomeMessage) {
        welcomeMessage.setText("Welcome " + name + "!");
        welcomeMessage.setHorizontalTextPosition(JLabel.CENTER);
        welcomeMessage.setVerticalTextPosition(JLabel.TOP);
        welcomeMessage.setForeground(Color.black);
        welcomeMessage.setFont(new Font("MV Boli", Font.ITALIC, 25));
        welcomeMessage.setVerticalAlignment(JLabel.CENTER);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBounds(1444/3, 0, 1444/3, 200);
    }

    public void display() {
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu(new MainFrame());
        mainMenu.display();
    }
}
