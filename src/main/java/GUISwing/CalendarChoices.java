package GUISwing;

import controllers.MainController;

import javax.swing.*;
import java.awt.*;

public class CalendarChoices {
    private final JFrame frame;
    private final MainController mc;
    private JButton buttonMonthly = new JButton("View Calendar By Monthly");
    private JButton buttonWeekly = new JButton("View Calendar By Weekly");
    private JButton buttonDaily = new JButton("View Calendar By Daily");

    public CalendarChoices(MainController mainController) {
        this.frame = new PopUpWindowFrame();
        this.mc = mainController;
        this.frame.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        frame.add(buttonPanel, BorderLayout.CENTER);





        this.frame.setVisible(true);
    }
}
