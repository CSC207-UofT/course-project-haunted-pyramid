package GUISwing;

import controllers.*;
import helpers.Constants;
import interfaces.MeltParentWindow;
import presenters.CalendarFactory.CalendarDisplayFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Main Menu of GUI
 * @author Seo Won Yi
 * @see MainFrame
 * @see MainFrameWithMenu
 * @see MainController
 */

public class MainMenu implements ActionListener, MeltParentWindow {
    private final JFrame frame;
    private final MainController mc;
    private final EventController ec;
    private final LoginController lc;
    private JLabel calendar;
    private LocalDate dateInfo;
    private String calendarMode;
    private final JPanel calendarPanel;
    private final JPanel welcomePanel;
    private JLabel welcomeMessage;
    private final JButton buttonProfile = new JButton("1. Profile Setting");
    private final JButton buttonCalendar = new JButton("2. Change Calendar");
    private final JButton buttonAddEvent = new JButton("3. Add a new Event");
    private final JButton buttonModifyEvent = new JButton("4. Modify an Event");
    private final JButton buttonCreateRecursion = new JButton("5. Create Recursion");
    private final JButton buttonExport = new JButton("6. Export Entire Calendar to iCal File");
    private final JButton buttonLogOut = new JButton("7. Log Out");
    private final JButton buttonExit = new JButton("8. Save and Exit");

    /**
     * Set up the main menu GUI
     * @param mainController MainController object to access every other controllers
     */
    public MainMenu(MainController mainController) {
        this.frame = new MainFrameWithMenu(mainController.getUserController(), this);
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.mc = mainController;
        this.ec = mainController.getEventController();
        UserController uc = mainController.getUserController();
        this.lc = new LoginController(uc);
        this.calendar = new JLabel();
        this.dateInfo = LocalDate.now();
        this.calendarMode = "Default";
        this.welcomePanel = new JPanel();
        this.welcomePanel.setBackground(Constants.WINDOW_COLOR);
        this.welcomePanel.setBounds(0, 0, Constants.WINDOW_WIDTH, 200);
        this.welcomePanel.setLayout(null);
        this.welcomeMessage = new JLabel();
        setUpWelcomeMessage(uc.getCurrentUsername());
        this.welcomePanel.add(welcomeMessage);
        this.calendarPanel = setUpCalendarPanel();
        setDefaultCalendar(this.ec);
        this.calendarPanel.add(this.calendar);
        JScrollPane calendarScrollPane = new JScrollPane(calendarPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        calendarScrollPane.setBounds(50, 150, 1344, 500);
        frame.add(calendarScrollPane);
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(0, 700, 1444, 300);
        menuPanel.setBackground(Constants.WINDOW_COLOR);
        frame.add(menuPanel);
        frame.add(welcomePanel);
        buttonSetUp(menuPanel);
        shutDownCondition();
    }

    private void shutDownCondition() {
        this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Current Information will not be saved \n Would you like to close the program?", "Confirm Close",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    exitFrame();
                }
            }
        });
    }

    /**
     * Set up the buttons to the panel
     * @param menuPanel panel that buttons will be added on
     */
    private void buttonSetUp(JPanel menuPanel) {
        setUpIndividualButton(buttonProfile, menuPanel);
        setUpIndividualButton(buttonCalendar, menuPanel);
        setUpIndividualButton(buttonAddEvent, menuPanel);
        setUpIndividualButton(buttonModifyEvent, menuPanel);
        setUpIndividualButton(buttonCreateRecursion, menuPanel);
        setUpIndividualButton(buttonExport, menuPanel);
        setUpIndividualButton(buttonLogOut, menuPanel);
        setUpIndividualButton(buttonExit, menuPanel);
    }

    /**
     * Configuration of button size and action
     * @param button buttons to be configured
     * @param menuPanel panel that buttons will be added on
     */
    private void setUpIndividualButton(JButton button, JPanel menuPanel) {
        button.setPreferredSize(new Dimension(300, 50));
        button.addActionListener(this);
        menuPanel.add(button);
    }

    /**
     * Set up the panel that will contain calendar information
     * @return Calendar Panel that will have calendar information on
     */
    public JPanel setUpCalendarPanel() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setBackground(Constants.WINDOW_COLOR);
        return calendarPanel;
    }

    /**
     * Set up the default Calendar (current month calendar)
     * @param eventController EventController object to obtain events information from
     */
    public void setDefaultCalendar(EventController eventController) {
        CalendarController calendarController = new CalendarController();
        String defaultCalendarString = calendarController.showDefaultCalendar(eventController);
        refactorCalendarString(defaultCalendarString);
    }

    /**
     * Set up the calendar with given date and type (by the option)
     * @param eventController EventController object to obtain event information from
     * @param date date of the event
     */
    private void setCalendar(EventController eventController, LocalDate date) {
        CalendarController calendarController = new CalendarController();
        CalendarDisplayFactory calendarFactory = calendarController.getDisplayCalendarFactory(eventController);
        String calendarString = calendarFactory.displaySpecificCalendarByType(this.calendarMode,
                date.getYear(), date.getMonthValue(),
                date.getDayOfMonth()).displayCalendar();
        refactorCalendarString(calendarString);
    }

    /**
     * Refactor the string of calendar to be presentable on the panel
     * @param calendarString raw calendarString to be refactored
     */
    private void refactorCalendarString(String calendarString) {
        calendarString = calendarString.replaceAll("\n", "<br/>");
        this.calendar.setText("<html><pre>" + calendarString + "</pre><html>");
        this.calendar.setHorizontalTextPosition(JLabel.CENTER);
        this.calendar.setVerticalTextPosition(JLabel.TOP);
        this.calendar.setVerticalAlignment(JLabel.TOP);
        this.calendar.setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Set up the welcome message
     * @param name name of the user
     */
    private void setUpWelcomeMessage(String name) {
        welcomeMessage.setText("Welcome " + name + "!");
        welcomeMessage.setHorizontalTextPosition(JLabel.CENTER);
        welcomeMessage.setVerticalTextPosition(JLabel.TOP);
        welcomeMessage.setFont(new Font("MV Boli", Font.ITALIC, 25));
        welcomeMessage.setVerticalAlignment(JLabel.CENTER);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBounds(1444/3, 0, 1444/3, 200);
    }

    /**
     * set up the date information for the calendar
     * @param newDate new date information to be set
     */
    public void setDateInfo(LocalDate newDate) {
        this.dateInfo = newDate;
    }

    /**
     * Set up the type of calendar to show
     * @param option type of calendar
     */
    public void setCalendarMode(String option) {
        if (option.equalsIgnoreCase("default")) {
            this.calendarMode = "Default";
        }
        else if (option.equalsIgnoreCase("monthly")) {
            this.calendarMode = "Monthly";
        }
        else if (option.equalsIgnoreCase("weekly")) {
            this.calendarMode = "Weekly";
        }
        else if (option.equalsIgnoreCase("daily")) {
            this.calendarMode = "Daily";
        }
    }

    /**
     * display the calendar
     */
    public void display() {
        frame.setVisible(true);
    }

    /**
     * Add actions for the buttons
     * @param e action to be considered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonProfile) {
            ProfileSettings sM = new ProfileSettings(mc.getUserController(), this);
            sM.display();
        }
        else if (e.getSource() == buttonCalendar) {
            this.frame.setEnabled(false);
            new CalendarChoices(this);
        }
        else if (e.getSource() == buttonAddEvent) {
            this.frame.setEnabled(false);
            UUID newEventID = this.ec.createDefaultEvent("Event Name", LocalDateTime.of(
                    LocalDate.now(), LocalTime.of(23, 59)));
            new EditEventWindow(this.mc, this.mc.getEventController().getEventManager().getDefaultEventInfoGetter(), newEventID, this, "add");
        }
        else if (e.getSource() == buttonModifyEvent) {
            this.frame.setEnabled(false);
            new SelectEvent(mc, mc.getEventController().getEventManager().getDefaultEventInfoGetter(), this, false);
        }
        else if (e.getSource() == buttonCreateRecursion) {
            JOptionPane.showMessageDialog(frame, "The implementation of this function is not complete! " +
                            "\n Please do not try to modify Events created through this " +
                            "\n (We recommend not utilizing this function yet)",
                    "Prototype Warning", JOptionPane.WARNING_MESSAGE);
            this.frame.setEnabled(false);
            new RecursionMenu(this.mc, mc.getEventController().getEventManager().getDefaultEventInfoGetter(), this);
        }

        else if (e.getSource() == buttonExport) {
            SaveICalendar saveCalendar = new SaveICalendar();
            saveCalendar.save(this.ec);
        }
        else if (e.getSource() == buttonLogOut) {
            this.lc.logout();
            frame.dispose();
            new LogInWindow(this.mc);
        }
        else if (e.getSource() == buttonExit) {
            mc.saveAndExitProgram();
            frame.dispose();
        }
        else {
            this.refresh();
        }
    }

    /**
     * Refresh the screen by reloading the contents on the panels
     */
    @Override
    public void refresh() {
        this.ec.getWorkSessionController().refresh(this.mc.getUserController().getPreferences(),
                this.ec.getEventManager());
        this.welcomePanel.removeAll();
        this.welcomeMessage = new JLabel();
        setUpWelcomeMessage(this.mc.getUserController().getCurrentUsername());
        welcomePanel.add(welcomeMessage);
        this.calendarPanel.removeAll();
        setUpCalendarPanel();
        this.calendar = new JLabel();
        if (this.calendarMode.equalsIgnoreCase("default")) {
            setDefaultCalendar(this.ec);
        }
        else {
            setCalendar(this.ec, dateInfo);
        }
        this.calendarPanel.add(this.calendar);
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
        this.mc.getIoSerializable().usersWriteToSerializable(this.mc.getUserController().getUserManager().getAllUsers());
        this.mc.getIoSerializable().eventsWriteToSerializable(this.mc.getEventController().getEventManager().getUuidEventsMap());
        this.frame.dispose();
    }

    /**
     * get parent object (previous window)
     * @return none because this class is the main menu (no prev window)
     */
    @Override
    public MeltParentWindow getParent() {
        return null;
    }
}
