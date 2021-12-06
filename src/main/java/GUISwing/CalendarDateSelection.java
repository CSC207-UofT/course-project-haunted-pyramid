package GUISwing;

import helpers.Constants;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarDateSelection  implements ActionListener {
    private final JFrame frame;
    private final MenuCreationHelper helper;
    private final MeltParentWindow parent;
    private final MainMenu grandParent;
    private int year;
    private int month;
    private int date;
    private final String option;
    private final JComboBox<YearMonth> yearMonthBox;
    private JComboBox<Integer> dateBox;
    private final JButton save;
    private final JButton cancel;

    public CalendarDateSelection(String option, MeltParentWindow parent, MainMenu grandParent) {
        this.frame = new PopUpWindowFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.helper = new MenuCreationHelper();
        this.parent = parent;
        this.option = option;
        this.grandParent = grandParent;
        this.save = new JButton("Save");
        this.cancel = new JButton("Cancel");
        JPanel textPanel = new JPanel();
        JLabel instruction = new JLabel("Please select the date information to view Calendar");
        instruction.setHorizontalAlignment(JLabel.CENTER);
        instruction.setHorizontalTextPosition(JLabel.CENTER);
        instruction.setFont(new Font("Verdana", Font.BOLD, 14));
        textPanel.setBackground(Constants.WINDOW_COLOR);
        textPanel.setBounds(0, 50, 500, 100);
        textPanel.add(instruction);

        yearMonthBox = this.helper.monthComboBox();
        yearMonthBox.setSelectedIndex(3);
        JPanel dateSelectionPanel = new JPanel();
        if (this.option.equalsIgnoreCase("Monthly")) {
            dateSelectionPanel.setBounds(0, 110, 500, 50);
            dateSelectionPanel.setLayout(null);
            dateSelectionPanel.setBackground(Constants.WINDOW_COLOR);
            yearMonthBox.setBounds(150, 0, 200, 50);
            dateSelectionPanel.add(yearMonthBox);
        }
        else {
            dateSelectionPanel.setBounds(100, 110, 300, 30);
            dateSelectionPanel.setLayout(new GridLayout(1, 2));
            dateSelectionPanel.add(yearMonthBox);
            dateBox = new JComboBox<>(helper.dateList(this.yearMonthBox.getItemAt(3), false));
            this.date = dateBox.getItemAt(0);
            dateSelectionPanel.add(dateBox);
            dateBox.addActionListener(this);
        }
        this.year = yearMonthBox.getItemAt(3).getYear();
        this.month = yearMonthBox.getItemAt(3).getMonthValue();

        JPanel optionPanel = new JPanel();
        optionPanel.setBackground(Constants.WINDOW_COLOR);
        optionPanel.setBounds(0, 170, 500, 70);
        optionPanel.add(save);
        optionPanel.add(cancel);
        yearMonthBox.addActionListener(this);
        save.addActionListener(this);
        cancel.addActionListener(this);
        this.frame.add(dateSelectionPanel);
        this.frame.add(textPanel);
        this.frame.add(optionPanel);
        this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parent.enableFrame();
                exitFrame();
            }
        });
        this.frame.setVisible(true);
    }

    public void exitFrame() {
        this.frame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == yearMonthBox) {
            YearMonth choice = (YearMonth) yearMonthBox.getSelectedItem();
            if (this.option.equalsIgnoreCase("Monthly")) {
                if (choice != null) {
                    year = choice.getYear();
                    month = choice.getMonthValue();
                }
            }
            else {
                if (choice != null) {
                    year = choice.getYear();
                    month = choice.getMonthValue();
                    dateBox.setModel(new DefaultComboBoxModel<>(helper.dateList(choice, false)));
                }
            }
        }

        if (e.getSource() == dateBox) {
            if (dateBox.getSelectedItem() != null) {
                this.date = (Integer) this.dateBox.getSelectedItem();
            }

        }

        if (e.getSource() == save) {
            if (option.equalsIgnoreCase("Monthly")) {
                this.grandParent.setDateInfo(LocalDate.of(this.year, this.month, 1));
                this.grandParent.setCalendarMode("Monthly");
            }
            else if (option.equalsIgnoreCase("Weekly")) {
                this.grandParent.setDateInfo(LocalDate.of(this.year, this.month, this.date));
                this.grandParent.setCalendarMode("Weekly");
            }
            else if (option.equalsIgnoreCase("Daily")) {
                this.grandParent.setDateInfo(LocalDate.of(this.year, this.month, this.date));
                this.grandParent.setCalendarMode("Daily");
            }
            this.parent.enableFrame();
            this.grandParent.refresh();
            this.parent.exitFrame();
            exitFrame();
        }
        if (e.getSource() == cancel) {
            this.parent.enableFrame();
            exitFrame();
        }
    }
}
