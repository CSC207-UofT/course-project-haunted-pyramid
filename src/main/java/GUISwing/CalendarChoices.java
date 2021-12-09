package GUISwing;

import helpers.Constants;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A window that lets the user choose the type of calendar that they would like to view
 * @author Seo Won Yi
 * @see presenters.CalendarFactory.CalendarDisplayFactory
 * @see CalendarDateSelection
 */

public class CalendarChoices implements ActionListener, MeltParentWindow {
    private final JFrame frame;
    private final MainMenu parent;
    private final JButton buttonMonthly = new JButton("View Calendar By Monthly");
    private final JButton buttonWeekly = new JButton("View Calendar By Weekly");
    private final JButton buttonDaily = new JButton("View Calendar By Daily");
    private final JButton buttonReturn = new JButton("Return to the Main Menu");

    /**
     * construct the window by setting up the option buttons (type of calendars)
     * @param parent MainMenu object that will be altered depending on the user's choice
     */
    public CalendarChoices (MainMenu parent) {
        this.frame = new PopUpWindowFrame();
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

    /**
     * Set up the appropriate actions according to user's choices
     * @param e action to be considered from
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonMonthly) {
            this.frame.setEnabled(false);
            new CalendarDateSelection("Monthly", this, this.parent);
        }

        if (e.getSource() == buttonWeekly) {
            this.frame.setEnabled(false);
            new CalendarDateSelection("Weekly", this, this.parent);
        }

        if (e.getSource() == buttonDaily) {
            this.frame.setEnabled(false);
            new CalendarDateSelection("Daily", this, this.parent);
        }
        if (e.getSource() == buttonReturn) {
            parent.refresh();
            this.frame.dispose();
        }
    }

    /**
     * refresh the screen
     */
    @Override
    public void refresh() {
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * enable the frame
     */
    @Override
    public void enableFrame() {
        this.frame.setEnabled(true);
    }

    /**
     * exit the frame
     */
    @Override
    public void exitFrame() {
        this.frame.dispose();
    }

    /**
     * get parent object
     * @return the parent object (prev window)
     */
    @Override
    public MeltParentWindow getParent() {
        return this.parent;
    }
}
