package GUISwing.newSubMenus;

import GUISwing.MainFrame;
import GUISwing.MenuCreationHelper;
import controllers.EventController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Objects;

public class NewEventWindow implements ActionListener {
    JFrame frame = new MainFrame();
    JPanel contentPanel = new JPanel();
    JPanel datesPanel = new JPanel(new GridLayout(2, 4));
    JComboBox<YearMonth> startMonth = MenuCreationHelper.monthComboBox();
    JComboBox<YearMonth> endMonth = MenuCreationHelper.monthComboBox();
    JComboBox<Integer> startDate = MenuCreationHelper.dateJComboBox(startMonth.getItemAt(0));
    JComboBox<Integer> endDate = MenuCreationHelper.dateJComboBox(endMonth.getItemAt(0));
    JComboBox<LocalTime> startTime = MenuCreationHelper.timeComboBox();
    JComboBox<LocalTime> endTime = MenuCreationHelper.timeComboBox();


    public NewEventWindow(EventController eventController){
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setPreferredSize(new Dimension(frame.getWidth() - 10, frame.getHeight() - 10));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(datesPanel);
        setDateTimePickers();

        frame.add(contentPanel);

        frame.setVisible(true);
        contentPanel.setVisible(true);
    }

    public void setDateTimePickers(){
        JLabel lblStart = new JLabel("start: ");
        setDateOptions(lblStart, startMonth, startDate, startTime);

        JLabel lblEnd = new JLabel("End: ");
        setDateOptions(lblEnd, endMonth, endDate, endTime);

        startMonth.addActionListener(this);
        endMonth.addActionListener(this);
    }

    private void setDateOptions(JLabel lblEnd, JComboBox<YearMonth> endMonth, JComboBox<Integer> endDate, JComboBox<LocalTime> endTime) {
        datesPanel.add(lblEnd);
        datesPanel.add(endMonth);
        datesPanel.add(endDate);
        datesPanel.add(endTime);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startMonth | e.getSource() == endMonth){
            startDate = MenuCreationHelper.dateJComboBox((YearMonth) Objects.requireNonNull(startMonth.getSelectedItem()));
            endDate = MenuCreationHelper.dateJComboBox((YearMonth) Objects.requireNonNull(endMonth.getSelectedItem()));
        }
    }
}
