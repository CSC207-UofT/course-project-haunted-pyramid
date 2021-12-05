package GUISwing;

import controllers.*;
import entities.UserPreferences;
import helpers.Constants;
import interfaces.MeltParentWindow;

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
    private final UserController uc;
    private final LoginController lc;
    private JLabel calendar;
    private JPanel calendarPanel;
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
        this.uc = mainController.getUserController();
        this.lc = new LoginController(this.uc);
        this.calendar = new JLabel();
        JLabel welcomeMessage = new JLabel();
        setUpWelcomeMessage(this.uc.getCurrentUsername(), welcomeMessage);
        frame.add(welcomeMessage);
        this.calendarPanel = setUpCalendarPanel();
        setDefaultCalendar(this.ec, calendarPanel);
        JScrollPane calendarScrollPane = new JScrollPane(calendarPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        calendarScrollPane.setBounds(50, 150, 1344, 500);
        frame.add(calendarScrollPane);
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(0, 700, 1444, 1000/2);
        menuPanel.setBackground(Constants.WINDOW_COLOR);
        frame.add(menuPanel);
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

    public void setDefaultCalendar(EventController eventController, JPanel calendarPanel) {
        CalendarController calendarController = new CalendarController();
        String defaultCalendarString = calendarController.showDefaultCalendar(eventController);
        defaultCalendarString = defaultCalendarString.replaceAll("\n", "<br/>");
        this.calendar.setText("<html><pre>" + defaultCalendarString + "</pre><html>");
        this.calendar.setHorizontalTextPosition(JLabel.CENTER);
        this.calendar.setVerticalTextPosition(JLabel.TOP);
        this.calendar.setVerticalAlignment(JLabel.TOP);
        this.calendar.setHorizontalAlignment(JLabel.CENTER);
        calendarPanel.add(this.calendar);
    }

    private void setUpWelcomeMessage(String name, JLabel welcomeMessage) {
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
            SettingsMenu sM = new SettingsMenu(mc.getUserController(), this);
            sM.display();
        }
        else if (e.getSource() == buttonCalendar) {
            this.frame.setEnabled(false);

        }
        else if (e.getSource() == buttonAddEvent) {
            this.frame.setEnabled(false);
            UUID newEventID = this.ec.getEventManager().addEvent("Event Name", LocalDateTime.of(
                    LocalDate.now(), LocalTime.of(0, 0)));
            new EditEventWindow(this.mc, newEventID, this);
        }
        else if (e.getSource() == buttonModifyEvent) {
            new SelectEvent(this.ec, this.uc, this);
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
            UUID user = this.uc.getCurrentUser();
            UserPreferences preferences = this.uc.getUserManager().getPreferences(user);
            this.ec.update(preferences);
            frame.revalidate();
            frame.repaint();
        }
    }

    public void setCalendar(JLabel newCalendar) {
        this.calendar = newCalendar;
    }

    @Override
    public void refresh() {
        this.calendarPanel.removeAll();
        setUpCalendarPanel();
        setDefaultCalendar(this.mc.getEventController(), this.calendarPanel);
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
