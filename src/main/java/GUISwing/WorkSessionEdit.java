package GUISwing;

import controllers.EventController;

import controllers.WorkSessionController;
import interfaces.EventInfoGetter;
import interfaces.MeltParentWindow;
import interfaces.WorkSessionInfoGetter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * @author Taite Cullen
 */
public class WorkSessionEdit implements ActionListener {
    JFrame frame = new PopUpWindowFrame();
    JLabel hoursLbl = new JLabel("total hours: ");
    JComboBox<Long> totalHours = fromTo(50);
    JLabel sessionLbl = new JLabel("session length: ");
    JComboBox<Long> sessionLength = fromTo(10);
    JButton save = new JButton("save");
    JButton done = new JButton("done");
    JLabel startLbl = new JLabel("start working ");
    JComboBox<Long> startWorking = fromTo(10);
    JLabel daysLbl = new JLabel("days before deadline");
    EventController eventController;
    EventInfoGetter eventInfoGetter;
    UUID event;
    MeltParentWindow parent;
    JPanel pastSessionPanel = new JPanel();
    JScrollPane pastSessionScroller = new JScrollPane(pastSessionPanel);

    JPanel futureSessionPanel = new JPanel();
    JScrollPane futureSessionScroller = new JScrollPane(futureSessionPanel);

    WorkSessionInfoGetter workSessionInfoGetter;

    /**
     * constructs WorkSessionEdit window
     *
     * @param eventInfoGetter       object used to obtain event information
     * @param workSessionInfoGetter active work session info getter
     * @param parent                the window from which this window opened
     * @param event                 UUID of the event being edited
     */
    public WorkSessionEdit(EventController eventController, EventInfoGetter eventInfoGetter,
                           WorkSessionInfoGetter workSessionInfoGetter, MeltParentWindow parent, UUID event) {
        this.eventController = eventController;
        this.workSessionInfoGetter = workSessionInfoGetter;
        this.parent = parent;
        this.event = event;
        this.eventInfoGetter = eventInfoGetter;


        frame.setVisible(true);

        addSettings();

        pastSessionScroller.setBounds(0, 60, frame.getWidth() / 2 - 10, frame.getHeight() - 70);
        pastSessionScroller.setVisible(true);
        futureSessionScroller.setBounds(frame.getWidth() / 2 + 5, 60, frame.getWidth() / 2 - 10, frame.getHeight() - 70);
        futureSessionScroller.setVisible(true);
        reset();
        frame.add(pastSessionScroller);
        frame.add(futureSessionScroller);
        closeAction(parent);
    }

    /**
     * sets action for when window closed
     *
     * @param parent the parent window
     */
    private void closeAction(MeltParentWindow parent) {
        this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                {
                    parent.enableFrame();
                    parent.refresh();
                }
            }
        });
    }


    /**
     * arranges adds all settings buttons and labels to the frame
     */
    public void addSettings() {
        startWorking.setSelectedItem(9L);
        hoursLbl.setBounds(10, 5, 100, 20);
        totalHours.setBounds(110, 5, 50, 20);
        sessionLbl.setBounds(160, 5, 100, 20);
        sessionLength.setBounds(260, 5, 50, 20);
        startLbl.setBounds(10, 30, 100, 20);
        startWorking.setBounds(110, 30, 50, 20);
        daysLbl.setBounds(160, 30, 150, 20);
        save.setBounds(310, 5, frame.getWidth() - 315, 25);
        done.setBounds(310, 30, frame.getWidth() - 315, 25);

        frame.add(totalHours);
        frame.add(sessionLength);
        frame.add(startWorking);
        frame.add(startLbl);
        frame.add(daysLbl);
        frame.add(hoursLbl);
        frame.add(sessionLbl);
        frame.add(save);
        frame.add(done);
        save.addActionListener(this);
        done.addActionListener(this);
    }

    /**
     * clears the work session panels and re-fills them with workSessions of event, then revalidates and repaints the frame
     */
    private void reset() {
        pastWorkSessionsReset();
        futureWorkSessionsReset();
        totalHours.setSelectedItem(workSessionInfoGetter.getTotalHoursNeeded(event));
        sessionLength.setSelectedItem(workSessionInfoGetter.getEventSessionLength(event));
        startWorking.setSelectedItem(workSessionInfoGetter.getStartWorking(event));
        frame.revalidate();
        frame.repaint();
    }

    /**
     * resets the past work session panel - clears the panel, sets the layout to box layout, then displays
     * all past work sessions in current event with button options for complete and incomplete
     */
    private void pastWorkSessionsReset() {
        pastSessionPanel.removeAll();
        pastSessionPanel.setLayout(new BoxLayout(pastSessionPanel, BoxLayout.Y_AXIS));
        Border line = BorderFactory.createTitledBorder("past sessions");
        pastSessionPanel.setBorder(line);

        for (UUID session : workSessionInfoGetter.getPastWorkSessions(event)) {
            JPanel sessionPanel = addWorkSession(session, true);
            pastSessionPanel.add(sessionPanel);
        }
    }

    /**
     * resets the future work session panel - clears the panel, sets the layout to box layout, then displays
     * all future work sessions in current event with button options for complete
     */
    private void futureWorkSessionsReset() {
        futureSessionPanel.removeAll();
        futureSessionPanel.setLayout(new BoxLayout(futureSessionPanel, BoxLayout.Y_AXIS));
        Border line = BorderFactory.createTitledBorder("upcoming sessions");
        futureSessionPanel.setBorder(line);

        for (UUID session : workSessionInfoGetter.getFutureWorkSessions(event)) {
            JPanel sessionPanel = addWorkSession(session, false);
            futureSessionPanel.add(sessionPanel);
        }
    }

    /**
     * A helper method for creating an options panel for a work session to be added to a panel
     *
     * @param session UUID session
     * @param past    if true, adds button for mark incomplete. otherwise, doesn't.
     * @return a JPanel with a work session info and 1-2 button options
     */
    private JPanel addWorkSession(UUID session, boolean past) {
        JPanel sessionOptions = new JPanel(new FlowLayout());
        sessionOptions.setPreferredSize(new Dimension(200, 100));

        sessionOptions.add(new JLabel("start: " + eventInfoGetter.getStart(session)));
        sessionOptions.add(new JLabel("end: " + eventInfoGetter.getEnd(session)));

        JButton complete = new JButton("completed (remove)");
        complete.setPreferredSize(new Dimension(100, 20));
        sessionOptions.add(complete);
        complete.setActionCommand("complete: " + session);
        complete.addActionListener(this);
        if (past) {
            JButton incomplete = new JButton("incompleted (reschedule)");
            incomplete.setPreferredSize(new Dimension(100, 20));
            sessionOptions.add(incomplete);
            incomplete.setActionCommand("incomplete: " + session);
            incomplete.addActionListener(this);
        }
        return sessionOptions;
    }

    /**
     * creates a combo box with Longs from 0 - to
     *
     * @param to the end number in the combo box
     * @return a constructed Long combo box
     */
    private JComboBox<Long> fromTo(Integer to) {
        Long[] list = new Long[to - (Integer) 0];
        for (long i = 0; i < to - (Integer) 0; i += 1) {
            list[(int) (i)] = i;
        }
        return new JComboBox<>(list);
    }

    /**
     * save or done buttons both save combo box options to be new settings for event. save resets frame, done closes frame
     * complete and incomplete buttons mark that work session complete/incomplete then reset the frame
     *
     * @param e Action performed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            WorkSessionController workSessionController = eventController.getWorkSessionController();
            workSessionController.changeTotalHour(event, eventController.getEventManager(), (Long) totalHours.getSelectedItem());
            workSessionController.changeSessionLength(event, eventController.getEventManager(), (Long) sessionLength.getSelectedItem());
            workSessionController.changeStartWorking(event, eventController.getEventManager(), (Long) startWorking.getSelectedItem());

            reset();
        } else if (e.getSource() == done) {
            WorkSessionController workSessionController = eventController.getWorkSessionController();
            workSessionController.changeTotalHour(event, eventController.getEventManager(), (Long) totalHours.getSelectedItem());
            workSessionController.changeSessionLength(event, eventController.getEventManager(), (Long) sessionLength.getSelectedItem());
            workSessionController.changeStartWorking(event, eventController.getEventManager(), (Long) startWorking.getSelectedItem());
            parent.enableFrame();
            frame.dispose();
        } else if (e.getActionCommand().split(": ")[0].equalsIgnoreCase("complete")) {
            WorkSessionController workSessionController = eventController.getWorkSessionController();
            workSessionController.markComplete(event, UUID.fromString(e.getActionCommand().split(": ")[1]),
                    eventController.getEventManager());
            reset();
        } else if (e.getActionCommand().split(": ")[0].equalsIgnoreCase("incomplete")) {
            WorkSessionController workSessionController = eventController.getWorkSessionController();
            workSessionController.markInComplete(event, UUID.fromString(e.getActionCommand().split(": ")[1]),
                    eventController.getEventManager());
            reset();
        }
    }
}
