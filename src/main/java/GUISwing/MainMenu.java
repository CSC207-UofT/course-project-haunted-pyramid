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
    private final JButton buttonCalendar = new JButton("2. View/Export Calendar");
    private final JButton buttonAddEvent = new JButton("3. Add a new Event");
    private final JButton buttonModifyEvent = new JButton("4. Modify an Event");
    private final JButton buttonExport = new JButton("5. Export Entire Calendar to iCal File");
    private final JButton buttonLogOut = new JButton("6. Log Out");
    private final JButton buttonExit = new JButton("7. Exit");


    public MainMenu(MainController mainController) {
        this.frame = new MainFrameWithMenu(mainController.getUserController(), this);
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
    }

    private void buttonSetUp(JPanel menuPanel) {
        setUpIndividualButton(buttonProfile, menuPanel);
        setUpIndividualButton(buttonCalendar, menuPanel);
        setUpIndividualButton(buttonAddEvent, menuPanel);
        setUpIndividualButton(buttonModifyEvent, menuPanel);
        setUpIndividualButton(buttonExport, menuPanel);
        setUpIndividualButton(buttonLogOut, menuPanel);
        setUpIndividualButton(buttonExit, menuPanel);
    }

    private void setUpIndividualButton(JButton button, JPanel menuPanel) {
        button.setPreferredSize(new Dimension(300, 50));
        button.addActionListener(this);
        menuPanel.add(button);
    }

    public JPanel setUpCalendarPanel() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setBackground(Constants.WINDOW_COLOR);
        return calendarPanel;
    }

    public void setDefaultCalendar(EventController eventController) {
        CalendarController calendarController = new CalendarController();
        String defaultCalendarString = calendarController.showDefaultCalendar(eventController);
        refactorCalendarString(defaultCalendarString);
    }

    public void setCalendar(EventController eventController, LocalDate date, String option) {
        CalendarController calendarController = new CalendarController();
        CalendarDisplayFactory calendarFactory = calendarController.getDisplayCalendarFactory(eventController);
        String calendarString = calendarFactory.displaySpecificCalendarByType(option, date.getYear(), date.getMonthValue(),
                date.getDayOfMonth()).displayCalendar();
        refactorCalendarString(calendarString);
    }

    private void refactorCalendarString(String calendarString) {
        calendarString = calendarString.replaceAll("\n", "<br/>");
        this.calendar.setText("<html><pre>" + calendarString + "</pre><html>");
        this.calendar.setHorizontalTextPosition(JLabel.CENTER);
        this.calendar.setVerticalTextPosition(JLabel.TOP);
        this.calendar.setVerticalAlignment(JLabel.TOP);
        this.calendar.setHorizontalAlignment(JLabel.CENTER);
    }

    private void setUpWelcomeMessage(String name) {
        welcomeMessage.setText("Welcome " + name + "!");
        welcomeMessage.setHorizontalTextPosition(JLabel.CENTER);
        welcomeMessage.setVerticalTextPosition(JLabel.TOP);
        welcomeMessage.setFont(new Font("MV Boli", Font.ITALIC, 25));
        welcomeMessage.setVerticalAlignment(JLabel.CENTER);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBounds(1444/3, 0, 1444/3, 200);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonProfile) {
            ProfileSettings sM = new ProfileSettings(mc.getUserController(), this);
            sM.display();
        }
        else if (e.getSource() == buttonCalendar) {
            new CalendarChoices(this.mc, this);
        }
        else if (e.getSource() == buttonAddEvent) {
            this.frame.setEnabled(false);
            UUID newEventID = this.ec.createDefaultEvent("Event Name", LocalDateTime.of(
                    LocalDate.now(), LocalTime.of(23, 59)));
            new EditEventWindow(this.mc, newEventID, this);
        }
        else if (e.getSource() == buttonModifyEvent) {
            new SelectEvent(mc, this);
        }
        else if (e.getSource() == buttonExport) {
            SaveICalendar saveCalendar = new SaveICalendar();
            saveCalendar.save(this.ec);
        }
        else if (e.getSource() == buttonLogOut) {
            this.lc.logout();
            frame.dispose();
            new LogInWindow();
        }
        else if (e.getSource() == buttonExit) {
            mc.saveAndExitProgram();
            frame.dispose();
        }
        else {
            this.refresh();
        }
    }

    public void setDateInfo(LocalDate newDate) {
        this.dateInfo = newDate;
    }

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

    @Override
    public void refresh() {
        this.mc.getEventController().updatePreferences(this.mc.getUserController().getPreferences());
        this.welcomePanel.removeAll();
        this.welcomeMessage = new JLabel();
        setUpWelcomeMessage(this.mc.getUserController().getCurrentUsername());
        welcomePanel.add(welcomeMessage);
        this.calendarPanel.removeAll();
        setUpCalendarPanel();
        if (this.calendarMode.equalsIgnoreCase("default")) {
            setDefaultCalendar(this.ec);
        }
        else {
            setCalendar(this.ec, dateInfo, this.calendarMode);
        }
        this.calendarPanel.add(this.calendar);
        this.frame.revalidate();
        this.frame.repaint();
    }

    @Override
    public void enableFrame() {
        this.frame.setEnabled(true);
    }

    @Override
    public void exitFrame() {
        this.frame.dispose();
    }

    @Override
    public MeltParentWindow getParent() {
        return null;
    }

    public void display() {
        frame.setVisible(true);
    }
}
