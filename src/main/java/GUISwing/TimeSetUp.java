package GUISwing;

import controllers.EventController;
import controllers.MainController;
import helpers.Constants;
import helpers.GUIInfoProvider;
import interfaces.EventInfoGetter;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

/**
 * Window used for setting up the start or end time of the event
 * @author Seo Won Yi
 * @see MenuCreationHelper
 * @see EditEventWindow
 */

public class TimeSetUp implements ActionListener {
    private final EventController ec;
    private final EventInfoGetter eventInfoGetter;
    private final UUID eventID;
    private final MeltParentWindow parent;
    private final String option;
    private final MenuCreationHelper boxHelper  = new MenuCreationHelper();
    private final JFrame frame = new PopUpWindowFrame();
    private final JComboBox<YearMonth> yearMonthBox;
    private JComboBox<Integer> dateBox;
    private final JComboBox<LocalTime> hourBox;
    private final JButton saveButton;
    private final JButton cancelButton;
    private int year;
    private int month;
    private int date;
    private LocalTime time;

    /**
     * Construct the TimeSetUp window
     * @param mainController MainController used for accessing all the other controllers
     * @param eventInfoGetter Interface used for gathering event information
     * @param eventID ID of an event
     * @param parent parent window (prev window)
     * @param option option that decides setting up start time or end time
     */
    public TimeSetUp(MainController mainController, EventInfoGetter eventInfoGetter,
                     UUID eventID, MeltParentWindow parent, String option) {
        this.ec = mainController.getEventController();
        this.eventInfoGetter = eventInfoGetter;
        this.eventID = eventID;
        this.parent = parent;
        this.option = option;
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.hourBox = boxHelper.timeComboBox();
        this.yearMonthBox = boxHelper.monthComboBox();
        setDefaultTimeLine();
        GUIInfoProvider helper = new GUIInfoProvider();
        JPanel infoPanel = configureInfoPanel();
        JLabel currentTimeText = new JLabel();
        JLabel currentTimeInfo = new JLabel();
        JLabel setUpNewTime = new JLabel("Set Up the New Time");
        if (option.equalsIgnoreCase("Start")) {
            currentTimeText = new JLabel("Current Event Start Time:");
            currentTimeInfo = new JLabel(helper.getEventStartInfo(eventID, eventInfoGetter));
            dateBox = new JComboBox<>(boxHelper.dateList(this.yearMonthBox.getItemAt(3), true));
        }
        else if (option.equalsIgnoreCase("End")) {
            currentTimeText = new JLabel("Current Event End Time:");
            currentTimeInfo = new JLabel(helper.getEventEndInfo(eventID, eventInfoGetter));
            dateBox = new JComboBox<>(boxHelper.dateList(this.yearMonthBox.getItemAt(3), false));
        }
        dateBox.setSelectedIndex(LocalDateTime.now().getDayOfMonth() - 1);
        date = dateBox.getItemAt(LocalDateTime.now().getDayOfMonth() - 1);
        setUpInfoPanel(infoPanel, currentTimeText, currentTimeInfo, setUpNewTime);
        JPanel comboBoxPanel = configureBoxPanel();
        JPanel optionPanel = configureOptionPanel();
        this.saveButton = new JButton("Save");
        this.cancelButton = new JButton("Cancel");
        addActionListener();
        addToPanels(comboBoxPanel, optionPanel);
        shutDownCondition(parent);
        this.frame.setVisible(true);
    }

    /**
     * Create Option panel that will have save or cancel button
     * @return JPanel that has been set up for the window
     */
    private JPanel configureOptionPanel() {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(1,2, 40, 0));
        optionPanel.setBackground(Constants.WINDOW_COLOR);
        optionPanel.setBounds(120, 200, 200, 30);
        this.frame.add(optionPanel);
        return optionPanel;
    }

    /**
     * Set up the default timeline (year, month, time) for the screen
     */
    private void setDefaultTimeLine() {
        yearMonthBox.setSelectedIndex(3);
        year = yearMonthBox.getItemAt(3).getYear();
        month = yearMonthBox.getItemAt(3).getMonthValue();
        if (option.equalsIgnoreCase("Start")) {
            if (this.eventInfoGetter.getStart(eventID) == null) {
                hourBox.setSelectedIndex(LocalTime.now().getHour() * 2);
                time = hourBox.getItemAt(LocalTime.now().getHour() * 2);
            }
            else {
                int startHour = this.eventInfoGetter.getStart(eventID).getHour();
                hourBox.setSelectedIndex(startHour * 2);
                time = hourBox.getItemAt(startHour * 2);
            }
        }

        else if (option.equalsIgnoreCase("End")) {
            int endHour = this.eventInfoGetter.getEnd(eventID).getHour();
            hourBox.setSelectedIndex(endHour * 2);
            time = hourBox.getItemAt(endHour * 2);
        }
    }

    /**
     * Set up the shutdown condition in case the user clicked escape
     * @param parent the parent window (prev window)
     */
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

    /**
     * Add buttons and comboboxes to the corresponding panels
     * @param comboBoxPanel panel for combo boxes (for timeline set up)
     * @param optionPanel panel for save and cancel button
     */
    private void addToPanels(JPanel comboBoxPanel, JPanel optionPanel) {
        comboBoxPanel.add(yearMonthBox);
        comboBoxPanel.add(dateBox);
        comboBoxPanel.add(hourBox);
        optionPanel.add(saveButton);
        optionPanel.add(cancelButton);
    }

    /**
     * add action listener to the boxes and buttons
     */
    private void addActionListener() {
        this.saveButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.yearMonthBox.addActionListener(this);
        this.dateBox.addActionListener(this);
        this.hourBox.addActionListener(this);
    }

    /**
     * Configure the box panel with the appropriate size and layout
     * @return configured box panel that will contain JCombobox
     */
    private JPanel configureBoxPanel() {
        JPanel comboBoxPanel = new JPanel();
        this.frame.add(comboBoxPanel);
        comboBoxPanel.setLayout(new GridLayout(1, 3));
        comboBoxPanel.setBounds(0, 120, Constants.POPUP_WIDTH - 17, Constants.POPUP_HEIGHT / 7);
        return comboBoxPanel;
    }

    /**
     * Configure the info panel with the appropriate size and layout
     * @return configured info panel that will contain current event's information
     */
    private JPanel configureInfoPanel() {
        JPanel infoPanel = new JPanel();
        this.frame.add(infoPanel);
        infoPanel.setLayout(null);
        infoPanel.setBounds(0, 0, Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT / 3);
        infoPanel.setBackground(Constants.WINDOW_COLOR);
        return infoPanel;
    }

    /**
     * Put contents in the info panel
     * @param infoPanel JPanel that will have contents on
     * @param currentTimeText JLabel that has text information
     * @param currentTimeInfo JLabel that has current time information
     * @param setUpNewTime JLabel that indicates where the new time set up happens
     */
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

    /**
     * Exit the frame
     */
    private void exitFrame() {
        this.frame.dispose();
    }

    /**
     * Set up appropriate actions for the user's choices
     * @param e action to be considered
     */
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

        if (e.getSource() == hourBox) {
            this.time = (LocalTime) this.hourBox.getSelectedItem();
        }

        if (e.getSource() == saveButton) {
            save();
        }
        if (e.getSource() == cancelButton) {
            this.parent.enableFrame();
            exitFrame();
        }
    }

    /**
     * Set up the new year and month according to user's selection
     */
    private void setYearMonth() {
        YearMonth choice = (YearMonth) yearMonthBox.getSelectedItem();
        if (choice != null) {
            year = choice.getYear();
            month = choice.getMonthValue();
            if (option.equalsIgnoreCase("Start")) {
                dateBox.setModel(new DefaultComboBoxModel<>(boxHelper.dateList(choice, true)));
            }
            else {
                dateBox.setModel(new DefaultComboBoxModel<>(boxHelper.dateList(choice, false)));
            }
        }
    }

    /**
     * Save the information and return to the previous window
     */
    private void save() {
        if (option.equalsIgnoreCase("Start")) {
            if (dateBox.getSelectedItem() == null) {
                this.ec.changeStartDate(this.eventID, eventInfoGetter.getEnd(this.eventID).toLocalDate());
                this.ec.changeStartTime(this.eventID, eventInfoGetter.getEnd(this.eventID).toLocalTime());
            }
            else {
                LocalDateTime startTime = getLocalDateTime();
                this.ec.changeStartDate(this.eventID, Objects.requireNonNull(startTime).toLocalDate());
                this.ec.changeStartTime(this.eventID, startTime.toLocalTime());
            }
        }

        else if (option.equalsIgnoreCase("End")) {
            LocalDateTime endTime = getLocalDateTime();
            this.ec.changeEndDate(eventID, Objects.requireNonNull(endTime).toLocalDate());
            this.ec.changeEndTime(eventID, endTime.toLocalTime());
        }
        this.parent.enableFrame();
        this.parent.refresh();
        exitFrame();
    }

    /**
     * get local date time for the given year month date and time
     * @return local date time of given year month date and time
     */
    private LocalDateTime getLocalDateTime() {
        if (dateBox.getSelectedItem() != null) {
            return LocalDateTime.of(year, month, date,
                    Objects.requireNonNull(time).getHour(), time.getMinute());
        }
        return null;
    }
}
