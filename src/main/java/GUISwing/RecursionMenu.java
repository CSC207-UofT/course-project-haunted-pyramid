package GUISwing;

import controllers.EventController;
import controllers.IOController;
import controllers.MainController;
import controllers.RecursionController;
import helpers.Constants;
import interfaces.EventInfoGetter;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Recursion creation page
 * @author Malik Lahlou
 * @see EventController
 * @see EventInfoGetter
 */

public class RecursionMenu implements ActionListener, MeltParentWindow {

    private final MainController mc;
    private final EventInfoGetter eventInfoGetter;
    private final MeltParentWindow parent;
    private final JFrame frame;
    private final EventController ec;
    private JTextField setNumRepetition;
    private JTextArea setSecondFirstTime;
    private JTextArea setFirstIntervalDates;
    private JTextArea setSecondIntervalDates;
    private JButton addEventToRecursion;
    private JButton saveButton;
    private JButton returnButton;
    private List<UUID> eventsInOneCycle = new ArrayList<>();

    /**
     * Construct the EditEventWindow
     * @param mc MainController with access to every controller
     * @param eventInfoGetter Interface used for obtaining event information
     * @param parent parent window (prev window)
     */
    public RecursionMenu(MainController mc, EventInfoGetter eventInfoGetter, MeltParentWindow parent) {
        this.parent = parent;
        this.eventInfoGetter = eventInfoGetter;
        this.frame = new PopUpWindowFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.mc = mc;
        this.ec = mc.getEventController();
        JPanel textInfoPanel = new JPanel();
        textInfoPanelSetUp(textInfoPanel);
        setUpTextInfo(textInfoPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(Constants.WINDOW_COLOR);
        buttonPanel.setBounds(0, 80, Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT * 2 / 3);
        setUpButtons();
        addButtons(buttonPanel);
        addActionLister();
        this.frame.add(textInfoPanel);
        this.frame.add(buttonPanel);
        this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parent.enableFrame();
                exitFrame();
            }
        });
        this.frame.setVisible(true);
    }

    public void addIDToEventInOneCycle(UUID uuid){
        this.eventsInOneCycle.add(uuid);
    }

    /**
     * Set up the buttons to be used
     */
    private void setUpButtons() {
        addEventToRecursion = new JButton("Add an event to the recursion");
        addEventToRecursion.setBounds(30, 50, 200, 20);
        saveButton = new JButton("Save");
        saveButton.setBounds(30, 130, 200, 20);
        returnButton = new JButton("Return");
        returnButton.setBounds(270, 130, 200, 20);
    }

    /**
     * Enable actions with buttons
     */
    private void addActionLister() {
        addEventToRecursion.addActionListener(this);
        saveButton.addActionListener(this);
        returnButton.addActionListener(this);
    }

    /**
     * Add buttons to the panel
     * @param buttonPanel panel that contains all the buttons
     */
    private void addButtons(JPanel buttonPanel) {
        buttonPanel.add(addEventToRecursion);
        buttonPanel.add(saveButton);
        buttonPanel.add(returnButton);
    }

    /**
     * Set up the text info panel that will have text information such as name and description
     * @param textInfoPanel panel to have text information on
     */
    private void textInfoPanelSetUp(JPanel textInfoPanel) {
        textInfoPanel.setBounds(0, 0, 230, Constants.POPUP_HEIGHT / 3);
        textInfoPanel.setLayout(null);
        textInfoPanel.setBackground(Constants.WINDOW_COLOR);
    }


    /**
     * set up the text information (name and description of event) and put them on the panel
     * @param panel panel to contain the text information
     */
    private void setUpTextInfo(JPanel panel) {
        setNumRepetition = new JTextField("Enter the number of time you wish to repeat the event you chose");
        setNumRepetition.setBounds(30, 17, 200, 50);
        setSecondFirstTime = new JTextArea("Enter the date the first event you selected repeat for the first time");
        setSecondFirstTime.setBounds(240, 17, 200, 50);
        setFirstIntervalDates = new JTextArea("Enter the date this repetition begin");
        setSecondIntervalDates = new JTextArea("Enter the date this repetition end");
        setSecondFirstTime.setLineWrap(true);
        setSecondFirstTime.setWrapStyleWord(true);
        setFirstIntervalDates.setWrapStyleWord(true);
        setFirstIntervalDates.setLineWrap(true);
        setSecondIntervalDates.setWrapStyleWord(true);
        setSecondIntervalDates.setLineWrap(true);
        JScrollPane intervalDatePanel = new JScrollPane(setFirstIntervalDates, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane intervalDatePanel2 = new JScrollPane(setSecondIntervalDates, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        intervalDatePanel.setBounds(30, 70, 200, 50);
        intervalDatePanel2.setBounds(240, 70, 200, 50);
        panel.add(setNumRepetition);
        panel.add(setSecondFirstTime);
        this.frame.add(intervalDatePanel);
        this.frame.add(intervalDatePanel2);
        this.frame.add(setSecondFirstTime);
    }


    /**
     * configuration of button actions
     * @param e each action to be considered from
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addEventToRecursion) {
            this.frame.setEnabled(false);
            SelectEvent selectEvent = new SelectEvent(mc, eventInfoGetter, this, true);
            JPanel eventPanel = new JPanel();
            eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
            eventPanel.setBackground(Constants.WINDOW_COLOR);
            selectEvent.allEventDisplay(eventPanel);
        }
        if (e.getSource() == saveButton) {
            RecursionController recursionController = new RecursionController();
            eventsInOneCycle = this.ec.getEventManager().timeOrderID(eventsInOneCycle);
            IOController ioController = new IOController();
            if(isNumeric(setNumRepetition.getText())){
                try{
                    LocalDateTime secondFirstTime = LocalDateTime.of(ioController.stringToDate(setSecondFirstTime.getText()), LocalTime.of(12,0));
                    recursionController.createNewRecursion(eventsInOneCycle, ec.getEventManager(),
                            Integer.parseInt(setNumRepetition.getText()), secondFirstTime);
                    this.parent.enableFrame();
                    this.parent.refresh();
                    this.exitFrame();
                }
                catch (NumberFormatException | NullPointerException numberFormatException){
                    JOptionPane.showConfirmDialog(frame, "The input of the dates should be in the following " +
                                    "format YYYY-MM-DD and you have to choose events to add to the recursion",
                            "Note", JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
            else {
                try {
                    LocalDateTime secondFirstTime = LocalDateTime.of(ioController.stringToDate(setSecondFirstTime.getText()), LocalTime.of(12,0));
                    LocalDateTime time1 = LocalDateTime.of(ioController.stringToDate(setFirstIntervalDates.getText()), LocalTime.of(12,0));
                    LocalDateTime time2 = LocalDateTime.of(ioController.stringToDate(setSecondIntervalDates.getText()), LocalTime.of(12,0));
                    recursionController.createNewRecursion(eventsInOneCycle, ec.getEventManager(), time1, time2, secondFirstTime);
                    this.parent.enableFrame();
                    this.parent.refresh();
                    this.exitFrame();
                }
                catch (NumberFormatException | NullPointerException numberFormatException){
                    JOptionPane.showConfirmDialog(frame, "The input of the dates should be in the following " +
                            "format YYYY-MM-DD and you have to choose events to add to the recursion", "Note", JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        }

        if (e.getSource() == returnButton) {
            if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                this.parent.enableFrame();
                this.parent.refresh();
                this.exitFrame();
            }
        }
    }

    /**
     * Refresh the page by reloading specific panels
     */
    @Override
    public void refresh() {
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * get parent object (prev window)
     * @return the parent object (prev window)
     */
    @Override
    public MeltParentWindow getParent() {
        return this.parent;
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
        this.frame.dispose();
    }


    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
