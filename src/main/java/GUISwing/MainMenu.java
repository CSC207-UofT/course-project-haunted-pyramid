package GUISwing;

import controllers.CalendarController;
import controllers.EventController;
import controllers.MainController;
import controllers.UserController;
import entities.UserPreferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class MainMenu implements ActionListener {
    private final JFrame frame;
    private final MainController mc;
    private final JButton buttonOne = new JButton("1. Profile Setting");
    private final JButton buttonTwo = new JButton("2. View/Export Calendar");
    private final JButton buttonThree = new JButton("3. Add Event");
    private final JButton editEvent = new JButton("4. Edit Event");
    private final JButton buttonFour = new JButton("5. Export Entire Calendar to iCal File");
    private final JButton buttonFive = new JButton("6. Log Out");
    private final JButton buttonSix = new JButton("7. Exit");

    public MainMenu(MainController mainController) {
        this.frame = new MainFrameWithMenu(mainController, this);
        this.mc = mainController;
        EventController eventController= mc.getEventController();
        CalendarController calendarController = mc.getCalendarController();
        UserController userController =  mc.getUserController();
        JLabel welcomeMessage = new JLabel();
        setUpWelcomeMessage(userController.getCurrentUsername(), welcomeMessage);
        frame.add(welcomeMessage);
        JPanel calendarPanel = setUpCalendarPanel();
        setUpDefaultCalendar(eventController, calendarController, calendarPanel);
        JScrollPane calendarScrollPane = new JScrollPane(calendarPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        calendarScrollPane.setBounds(50, 150, 1344, 500);
        frame.add(calendarScrollPane);
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(0, 700, 1444, 1000/2);
        menuPanel.setBackground(new Color(233, 161, 161));
        frame.add(menuPanel);
        buttonSetUp(menuPanel);
    }

    private void buttonSetUp(JPanel menuPanel) {
        setUpIndividualButton(buttonOne, menuPanel);
        setUpIndividualButton(buttonTwo, menuPanel);
        setUpIndividualButton(buttonThree, menuPanel);
        setUpIndividualButton(editEvent, menuPanel);
        setUpIndividualButton(buttonFour, menuPanel);
        setUpIndividualButton(buttonFive, menuPanel);
        setUpIndividualButton(buttonSix, menuPanel);
    }

    private void setUpIndividualButton(JButton button, JPanel menuPanel) {
        button.setPreferredSize(new Dimension(300, 50));
        button.addActionListener(this);
        menuPanel.add(button);
    }

    public JPanel setUpCalendarPanel() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setBackground(new Color(233, 161, 161));
        return calendarPanel;
    }

    public void setUpDefaultCalendar(EventController eventController, CalendarController calendarController, JPanel calendarPanel) {
        JLabel defaultCalendar = new JLabel();
        String defaultCalendarString = calendarController.showDefaultCalendar(eventController);
        defaultCalendarString = defaultCalendarString.replaceAll("\n", "<br/>");
        defaultCalendar.setText("<html><pre>" + defaultCalendarString + "</pre><html>");
        defaultCalendar.setHorizontalTextPosition(JLabel.CENTER);
        defaultCalendar.setVerticalTextPosition(JLabel.TOP);
        defaultCalendar.setVerticalAlignment(JLabel.TOP);
        defaultCalendar.setHorizontalAlignment(JLabel.CENTER);
        calendarPanel.add(defaultCalendar);
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
        if (e.getSource() == buttonOne) {
            //lead to profile setting page
            mc.getLoginController().logout();
            frame.dispose();
            new LogInWindow();
        }
        else if (e.getSource() == buttonTwo) {
            //lead to calendar page
            mc.getLoginController().logout();
            frame.dispose();
            new LogInWindow();
        }
        else if (e.getSource() == buttonThree) {
            //lead to event page
            UUID newEvent = mc.getEventController().getEventManager().addEvent("event title", LocalDateTime.of(
                    LocalDate.now(), LocalTime.of(0, 0)));
            System.out.println(mc.getEventController().getEventManager().getStartWorking(newEvent));
            new EditEventWindow(mc.getUserController(), mc.getEventController(), newEvent, this);
        }
        else if (e.getSource() == buttonFour) {
            SaveICalendar saveCalendar = new SaveICalendar();
            saveCalendar.save(mc.getEventController());
        }
        else if (e.getSource() == buttonFive) {
            mc.getLoginController().logout();
            frame.dispose();
            new LogInWindow();
        }
        else if (e.getSource() == buttonSix) {
            mc.saveAndExitProgram();
            frame.dispose();
        } else if (e.getSource() == editEvent){
            new SelectEvent(mc.getEventController(), mc.getUserController(), this);
        }
        else{
            UUID user = mc.getUserController().getCurrentUser();
            UserPreferences preferences = mc.getUserController().getUserManager().getPreferences(user);
            mc.getEventController().update(preferences);
            frame.dispose();
            new MainMenu(mc);
            this.dispose();
        }
    }

    public MainController getmc(){return this.mc;}

    public void display() {
        frame.setVisible(true);
    }

    public void dispose() {frame.dispose();
    }
}
