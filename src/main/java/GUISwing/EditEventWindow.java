package GUISwing;

import controllers.EventController;
import controllers.MainController;
import controllers.UserController;
import helpers.Constants;
import helpers.GUIInfoProvider;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;


public class EditEventWindow implements ActionListener, MeltParentWindow {
    private final UUID eventID;
    private final MeltParentWindow parent;
    private final JFrame frame;
    private final MainController mc;
    private final EventController ec;
    private final UserController uc;
    private JTextField eventName;
    private JTextArea eventDescription;
    private JButton setUpStart;
    private JButton setUpEnd;
    private JButton workSessionButton;
    private JButton recursionButton;
    private JButton saveButton;
    private JButton deleteButton;

    public EditEventWindow(MainController mc, UUID eventID, MeltParentWindow parent) {
        GUIInfoProvider helper = new GUIInfoProvider();
        this.eventID = eventID;
        this.parent = parent;
        this.frame = new PopUpWindowFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.mc = mc;
        this.ec = mc.getEventController();
        this.uc = mc.getUserController();
        JPanel infoPanel = new JPanel();
        infoPanelSetUp(infoPanel);
        setUpLabels(eventID, helper, infoPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(Constants.WINDOW_COLOR);
        buttonPanel.setBounds(0, 80, Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT * 2 / 3);

        setUpButtons();
        addButtons(buttonPanel);
        addActionLister();
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

    private void setUpLabels(UUID eventID, GUIInfoProvider helper, JPanel infoPanel) {
        eventName = new JTextField(this.ec.getEventManager().getName(this.eventID));
        eventName.setBounds(30, 17, 200, 20);
        eventDescription = new JTextArea(this.ec.getEventManager().getDescription(this.eventID));
        eventDescription.setBounds(30, 47, 200, 50);
        JLabel viewStart = new JLabel("Current Event Start Time:");
        viewStart.setBounds(270, 10, 270, 20);
        JLabel startTime = new JLabel(helper.getEventStartInfo(eventID, this.ec));
        startTime.setBounds(270, 30, 270, 20);
        JLabel viewEnd = new JLabel("Current Event End Time:");
        viewEnd.setBounds(270, 50, 270, 20);
        JLabel endTime = new JLabel(helper.getEventEndInfo(eventID, this.ec));
        endTime.setBounds(270, 70, 270, 20);

        infoPanel.add(eventName);
        infoPanel.add(eventDescription);
        infoPanel.add(viewStart);
        infoPanel.add(viewEnd);
        infoPanel.add(startTime);
        infoPanel.add(endTime);
    }

    private void setUpButtons() {
        setUpStart = new JButton("Modify Start Time");
        setUpStart.setBounds(30, 50, 200, 20);
        setUpEnd = new JButton("Modify End Time");
        setUpEnd.setBounds(270, 50, 200, 20);
        workSessionButton = new JButton("Add / Modify Work Sessions");
        workSessionButton.setBounds(30, 90, 200, 20);
        recursionButton = new JButton("Set Up Recursion");
        recursionButton.setBounds(270, 90, 200, 20);
        saveButton = new JButton("Save");
        saveButton.setBounds(30, 130, 200, 20);
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(270, 130, 200, 20);
    }

    private void addActionLister() {
        setUpStart.addActionListener(this);
        setUpEnd.addActionListener(this);
        workSessionButton.addActionListener(this);
        recursionButton.addActionListener(this);
        saveButton.addActionListener(this);
        deleteButton.addActionListener(this);
    }

    private void addButtons(JPanel buttonPanel) {
        buttonPanel.add(setUpStart);
        buttonPanel.add(setUpEnd);
        buttonPanel.add(workSessionButton);
        buttonPanel.add(recursionButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
    }

    private void infoPanelSetUp(JPanel infoPanel) {
        infoPanel.setBounds(0, 0, Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT / 3);
        infoPanel.setLayout(null);
        infoPanel.setBackground(Constants.WINDOW_COLOR);
        this.frame.add(infoPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == setUpStart) {
            this.frame.setEnabled(false);
            new TimeSetUp(this.mc, this.eventID,this, "Start");
        }

        if (e.getSource() == setUpEnd) {
            this.frame.setEnabled(false);
            new TimeSetUp(this.mc, this.eventID,this, "End");
        }

        if (e.getSource() == workSessionButton) {
            // lead to work session
            this.parent.enableFrame();
            exitFrame();
        }

        if (e.getSource() == recursionButton) {
            // lead to recursion
            this.parent.enableFrame();
            exitFrame();
        }

        if (e.getSource() == saveButton) {
            this.ec.getEventManager().setName(this.eventID, eventName.getText());
            this.ec.getEventManager().setDescription(this.eventID, eventDescription.getText());
            this.parent.enableFrame();
            this.parent.exitFrame();
            new MainMenu(this.mc);
            this.exitFrame();
        }

        if (e.getSource() == deleteButton) {
            if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove this event?",
                    "Remove Event", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                this.ec.getEventManager().remove(eventID);
                this.parent.enableFrame();
                this.parent.exitFrame();
                new MainMenu(this.mc);
                exitFrame();
            }
        }
    }
    @Override
    public MeltParentWindow getParent() {
        return this.parent;
    }

    @Override
    public void enableFrame() {
        this.frame.setEnabled(true);
    }

    @Override
    public void exitFrame() {
        this.frame.dispose();
    }
}
