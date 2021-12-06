package GUISwing;

import controllers.MainController;
import helpers.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalendarChoices implements ActionListener {
    private final JFrame frame;
    private final MainController mc;
    private MainMenu parent;
    private JButton buttonMonthly = new JButton("View Calendar By Monthly");
    private JButton buttonWeekly = new JButton("View Calendar By Weekly");
    private JButton buttonDaily = new JButton("View Calendar By Daily");
    private JButton buttonReturn = new JButton("Return to the Main Menu");

    public CalendarChoices(MainController mainController, MainMenu parent) {
        this.frame = new PopUpWindowFrame();
        this.mc = mainController;
        this.frame.setLayout(new BorderLayout());
        this.parent = parent;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(Constants.WINDOW_COLOR);
        buttonMonthly.setBounds(150, 40, 200, 30);
        buttonWeekly.setBounds(150, 85, 200, 30);
        buttonDaily.setBounds(150, 130, 200, 30);
        buttonReturn.setBounds(150, 175, 200, 30);
        buttonMonthly.addActionListener(this);
        buttonWeekly.addActionListener(this);
        buttonDaily.addActionListener(this);
        buttonReturn.addActionListener(this);

        frame.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.add(buttonMonthly);
        buttonPanel.add(buttonWeekly);
        buttonPanel.add(buttonDaily);
        buttonPanel.add(buttonReturn);


        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonMonthly) {

        }

        if (e.getSource() == buttonWeekly) {

        }

        if (e.getSource() == buttonDaily) {

        }

        if (e.getSource() == buttonReturn) {
            parent.refresh();
            this.frame.dispose();
        }
    }
}
