package GUISwing;

import controllers.EventController;
import controllers.MainController;
import helpers.Constants;
import helpers.GUIInfoProvider;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class TimeSetUp implements ActionListener {
    private final String option;
    private final MainController mc;
    private final UUID eventID;
    private final MeltParentWindow parent;
    private final JFrame frame;
    private final EventController ec;
    private final JComboBox<YearMonth> yearMonthBox;
    private JComboBox<Integer> dateBox;
    private final JComboBox<LocalTime> timeBox;
    private final JButton saveButton;
    private final JButton cancelButton;
    private int year;
    private int month;
    private int date;
    private LocalTime time;

    public TimeSetUp(MainController mainController, UUID eventID, MeltParentWindow parent, String option) {
        this.option = option;
        this.mc = mainController;
        this.eventID = eventID;
        this.parent = parent;
        this.frame = new PopUpWindowFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.ec = mainController.getEventController();
        this.timeBox = MenuCreationHelper.timeComboBox();
        this.yearMonthBox = MenuCreationHelper.monthComboBox();
        this.yearMonthBox.setSelectedIndex(3);
        setDefaultTimeLine();
        GUIInfoProvider helper = new GUIInfoProvider();
        JPanel infoPanel = new JPanel();
        configureInfoPanel(infoPanel);
        JLabel currentTimeText = new JLabel();
        JLabel currentTimeInfo = new JLabel();
        JLabel setUpNewTime = new JLabel("Set Up the New Time");
        if (option.equalsIgnoreCase("Start")) {
            currentTimeText = new JLabel("Current Event Start Time:");
            currentTimeInfo = new JLabel(helper.getEventStartInfo(eventID, this.ec));
            dateBox = new JComboBox<>(MenuCreationHelper.dateList(this.yearMonthBox.getItemAt(3), true));
        }
        else if (option.equalsIgnoreCase("End")) {
            currentTimeText = new JLabel("Current Event End Time:");
            currentTimeInfo = new JLabel(helper.getEventEndInfo(eventID, this.ec));
            dateBox = new JComboBox<>(MenuCreationHelper.dateList(this.yearMonthBox.getItemAt(3), false));
        }
        dateBox.setSelectedIndex(LocalDateTime.now().getDayOfMonth() - 1);
        date = dateBox.getItemAt(LocalDateTime.now().getDayOfMonth() - 1);
        setUpInfoPanel(infoPanel, currentTimeText, currentTimeInfo, setUpNewTime);
        JPanel comboBoxPanel = new JPanel();
        configureBoxPanel(comboBoxPanel);
        JPanel optionPanel = new JPanel();
        configureOptionPanel(optionPanel);
        this.saveButton = new JButton("Save");
        this.cancelButton = new JButton("Cancel");
        addActionListener();
        addToPanels(comboBoxPanel, optionPanel);
        shutDownCondition(parent);
        this.frame.setVisible(true);
    }

    private void configureOptionPanel(JPanel optionPanel) {
        optionPanel.setLayout(new GridLayout(1,2, 40, 0));
        optionPanel.setBackground(Constants.WINDOW_COLOR);
        optionPanel.setBounds(120, 200, 200, 30);
        this.frame.add(optionPanel);
    }

    private void setDefaultTimeLine() {
        year = yearMonthBox.getItemAt(3).getYear();
        month = yearMonthBox.getItemAt(3).getMonthValue();
        time = timeBox.getItemAt(0);
    }

    private void shutDownCondition(MeltParentWindow parent) {
        this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Current Information will not be saved \n Would you like to close the window?", "Confirm Close",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    parent.enableFrame();
                    exitFrame();
                }
            }
        });
    }

    private void addToPanels(JPanel comboBoxPanel, JPanel optionPanel) {
        comboBoxPanel.add(yearMonthBox);
        comboBoxPanel.add(dateBox);
        comboBoxPanel.add(timeBox);
        optionPanel.add(saveButton);
        optionPanel.add(cancelButton);
    }

    private void addActionListener() {
        this.saveButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.yearMonthBox.addActionListener(this);
        this.dateBox.addActionListener(this);
        this.timeBox.addActionListener(this);
    }

    private void configureBoxPanel(JPanel comboBoxPanel) {
        this.frame.add(comboBoxPanel);
        comboBoxPanel.setLayout(new GridLayout(1, 3));
        comboBoxPanel.setBounds(0, Constants.POPUP_HEIGHT / 3, Constants.POPUP_WIDTH - 17, Constants.POPUP_HEIGHT / 5);
    }

    private void configureInfoPanel(JPanel infoPanel) {
        this.frame.add(infoPanel);
        infoPanel.setLayout(null);
        infoPanel.setBounds(0, 0, Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT / 3);
        infoPanel.setBackground(Constants.WINDOW_COLOR);
    }

    private void setUpInfoPanel(JPanel infoPanel, JLabel currentTimeText, JLabel currentTimeInfo, JLabel setUpNewTime) {
        setUpNewTime.setHorizontalAlignment(JLabel.CENTER);
        setUpNewTime.setHorizontalAlignment(JLabel.CENTER);
        currentTimeText.setHorizontalAlignment(JLabel.CENTER);
        currentTimeText.setHorizontalTextPosition(JLabel.CENTER);
        currentTimeInfo.setHorizontalAlignment(JLabel.CENTER);
        currentTimeInfo.setHorizontalTextPosition(JLabel.CENTER);
        currentTimeText.setBounds(30, 30, 190, 20);
        currentTimeInfo.setBounds(210, 30, 250, 20);
        setUpNewTime.setBounds(130, 80, 200, 20);
        currentTimeText.setFont(currentTimeText.getFont().deriveFont((float) 15.0));
        currentTimeInfo.setFont(currentTimeInfo.getFont().deriveFont((float) 15.0));
        infoPanel.add(currentTimeText);
        infoPanel.add(currentTimeInfo);
        infoPanel.add(setUpNewTime);
    }

    private void exitFrame() {
        this.frame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == yearMonthBox) {
            setYearMonth();
        }

        if (e.getSource() == dateBox) {
            if (this.dateBox.getSelectedItem() != null) {
                    this.date = (Integer) this.dateBox.getSelectedItem();
                }
        }

        if (e.getSource() == timeBox) {
            this.time = (LocalTime) this.timeBox.getSelectedItem();
        }

        if (e.getSource() == saveButton) {
            save();
        }
        if (e.getSource() == cancelButton) {
            this.parent.enableFrame();
            exitFrame();
        }
    }

    private void setYearMonth() {
        YearMonth choice = (YearMonth) yearMonthBox.getSelectedItem();
        if (choice != null) {
            year = choice.getYear();
            month = choice.getMonthValue();
            if (option.equalsIgnoreCase("Start")) {
                dateBox.setModel(new DefaultComboBoxModel<>(MenuCreationHelper.dateList(choice, true)));
            }
            else {
                dateBox.setModel(new DefaultComboBoxModel<>(MenuCreationHelper.dateList(choice, false)));
            }
        }
    }

    private void save() {
        if (option.equalsIgnoreCase("Start")) {
            if (dateBox.getSelectedItem() == null) {
                this.ec.getEventManager().setStart(this.eventID, null);
            }
            else {
                LocalDateTime startTime = getLocalDateTime();
                this.ec.getEventManager().setStart(this.eventID, startTime);
            }
        }

        else if (option.equalsIgnoreCase("End")) {
            LocalDateTime endTime = getLocalDateTime();
            this.ec.getEventManager().setEnd(eventID, endTime);
        }
        this.parent.enableFrame();
        this.parent.refresh();
        exitFrame();
    }

    private LocalDateTime getLocalDateTime() {
        if (dateBox.getSelectedItem() != null) {
            return LocalDateTime.of(year, month, date,
                    Objects.requireNonNull(time).getHour(), time.getMinute());
        }
        return null;
    }
}
