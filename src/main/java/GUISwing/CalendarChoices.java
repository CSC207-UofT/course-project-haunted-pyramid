package GUISwing;

import controllers.MainController;

import javax.swing.*;

public class CalendarChoices {
    private final JFrame frame;
    private final MainController mc;
    private JButton buttonMonthly = new JButton("View Monthly Calendar");
    private JButton buttonWeekly = new JButton("View Weekly Calendar");
    private JButton buttonDaily = new JButton("View Daily Calendar");

    public CalendarChoices(MainController mainController) {
        this.frame = new PopUpWindowFrame();
        this.mc = mainController;

        this.frame.setVisible(true);

    }
}
