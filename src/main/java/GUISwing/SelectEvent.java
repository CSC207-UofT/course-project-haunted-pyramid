package GUISwing;

import controllers.MainController;
import entities.Event;
import helpers.Constants;
import interfaces.EventInfoGetter;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Menu that allows the users to select events to view and modify
 * @author Seo Won Yi
 * @author Taite Cullen
 * @see EditEventWindow
 */

public class SelectEvent extends PopUpWindowFrame implements ActionListener {
    private final MainController mc;
    private final EventInfoGetter eventInfoGetter;
    private final MeltParentWindow parent;
    private final JButton returnButton;

    /**
     * Construct the window by placing the appropriate components
     * @param mc MainController object that allows access to every other controllers
     * @param eventInfoGetter Abstraction used for getting information of a given event
     * @param parent parent window (prev window)
     */
    public SelectEvent (MainController mc, EventInfoGetter eventInfoGetter, MeltParentWindow parent) {
        this.mc = mc;
        this.eventInfoGetter = eventInfoGetter;
        this.parent = parent;
        JScrollPane eventScroller = displayEvents();
        eventScroller.setBounds(0, 0, this.getWidth(), 2 * this.getHeight() / 3);
        JPanel returnPanel = setUpReturnPanel();
        returnButton = configureReturnButton(returnPanel);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parent.enableFrame();
            }
        });
        this.setVisible(true);
    }

    /**
     * Configure the Return Button and place it appropriately
     * @param returnPanel panel to contain the button
     * @return configured JButton object
     */
    private JButton configureReturnButton(JPanel returnPanel) {
        final JButton returnButton;
        returnButton = new JButton("Return");
        returnButton.setPreferredSize(new Dimension(100, 30));
        returnButton.addActionListener(this);
        returnPanel.add(returnButton);
        return returnButton;
    }

    /**
     * Set up the panel that will contain return button
     * @return configured JPanel
     */
    private JPanel setUpReturnPanel() {
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.Y_AXIS));
        returnPanel.setBounds(0, 2 * this.getHeight() / 3, this.getWidth(), this.getHeight() / 3);
        returnPanel.setBackground(Constants.WINDOW_COLOR);
        this.add(returnPanel);
        return returnPanel;
    }

    /**
     * Display all the stored events to allow the user to choose
     * @return JScrollPane that contains every event as options to choose from
     */
    private JScrollPane displayEvents(){
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(Constants.WINDOW_COLOR);
        for (Event event: eventInfoGetter.getAllEvents()) {
            String eventName = eventInfoGetter.getName(eventInfoGetter.getID(event));
            if (eventName.length() > 20) {
                eventName = eventName.substring(0, 17) + "...";
            }
            LocalDate eventEndDate = eventInfoGetter.getEnd(eventInfoGetter.getID(event)).toLocalDate();
            LocalTime eventEndTime = eventInfoGetter.getEnd(eventInfoGetter.getID(event)).toLocalTime();
            JButton btn;
            if (eventInfoGetter.getStart(eventInfoGetter.getID(event)) != null) {
                btn = getEventInfoButton(event, eventName, eventEndDate, eventEndTime);
            }
            else {
                String eventInfo = "<html>" + eventName + "<br/>" + "Due: " + eventEndDate + " " + eventEndTime
                        + "<html>";
                btn= new JButton(eventInfo);
            }
            configureButton(eventPanel, event, btn);
        }
        JScrollPane eventScroller = new JScrollPane(eventPanel);
        eventScroller.setPreferredSize(new Dimension(150, 100));
        add(eventScroller);
        setVisible(true);
        return eventScroller;
    }

    /**
     * Configure the event buttons
     * @param eventPanel panel that contains all the event buttons
     * @param event event object to be considered from
     * @param btn button that will be configured
     */
    private void configureButton(JPanel eventPanel, Event event, JButton btn) {
        btn.setMaximumSize(new Dimension(180, 50));
        btn.setActionCommand(eventInfoGetter.getID(event).toString());
        btn.addActionListener(this);
        eventPanel.add(btn);
        eventPanel.add(Box.createVerticalStrut(10));
    }

    /**
     * add information to a JButton
     * @param event event object with information
     * @param eventName name of the event
     * @param eventEndDate end date of the event
     * @param eventEndTime end time of the event
     * @return JButton that contains the event information
     */
    private JButton getEventInfoButton(Event event, String eventName, LocalDate eventEndDate, LocalTime eventEndTime) {
        JButton btn;
        LocalDate eventStartDate = eventInfoGetter.getStart(eventInfoGetter.getID(event)).toLocalDate();
        LocalTime eventStartTime = eventInfoGetter.getStart(eventInfoGetter.getID(event)).toLocalTime();
        String eventInfo = "<html>" + eventName + "<br/>" + "Start:  " + eventStartDate + " " +
                eventStartTime + "<br/>" + "End:   " + eventEndDate + " " + eventEndTime + "<html>";
        btn= new JButton(eventInfo);
        return btn;
    }

    /**
     * Perform actions upon user's selection
     * @param e action to be considered from
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton) {
            parent.enableFrame();
        }
        else {
            new EditEventWindow(mc, eventInfoGetter, UUID.fromString(e.getActionCommand()), parent, "modify");
        }
        this.dispose();
    }
}
