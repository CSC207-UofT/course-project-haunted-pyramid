package GUISwing;

import controllers.UserController;
import entities.User;
import entities.UserPreferences;
import gateways.IOSerializable;
import helpers.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Iterator;

public class SettingsMenu extends JMenu implements ActionListener {
//    JMenuItem freeTime;
    JMenuItem profile;
    JCheckBoxMenuItem cram;
    JCheckBoxMenuItem procrastinate;
    JMenu spacing;
    UserController userController;
    JCheckBoxMenuItem morningPerson;

    JFrame frame = new JFrame();


    JButton freeTime = new JButton("free time");
    JButton saveFT = new JButton("save");

    JComboBox<LocalTime> startTime = MenuCreationHelper.timeComboBox();
    JComboBox<LocalTime> endTime = MenuCreationHelper.timeComboBox();
    JPanel datesPanel = new JPanel(new GridLayout(2, 4));


    ActionListener parent;

    public SettingsMenu(UserController userController, ActionListener parent){
        this.userController = userController;
        this.parent = parent;
        this.setText("Settings");
        this.setVisible(true);


        profile = new JMenuItem("profile settings");
        profile.addActionListener(this);
        this.add(profile);


//        freeTime = new JMenuItem("free time");
//        freeTime.setIcon(new ImageIcon("/res/freeTimeIcon.jpg"));
//        freeTime.addActionListener(this);
//        this.add(freeTime);




        this.addSeparator();
        morningPerson = new JCheckBoxMenuItem("morning person");
        morningPerson.setSelected(userController.getUserManager().getPreferences(userController.getCurrentUser()).getMorningPerson());
        this.add(morningPerson);
        morningPerson.setActionCommand("morning");
        morningPerson.addActionListener(parent);
        morningPerson.addActionListener(this);



        this.addSeparator();
        cram = new JCheckBoxMenuItem("cram");
        cram.setSelected(userController.getUserManager().getPreferences(userController.getCurrentUser()).getCram());
        cram.addActionListener(parent);
        cram.addActionListener(this);
        procrastinate = new JCheckBoxMenuItem("procrastinate");
        procrastinate.setSelected(userController.getCurrentProcrastinate());
        procrastinate.addActionListener(parent);
        procrastinate.addActionListener(this);
        this.add(cram);
        this.add(procrastinate);


        spacing = new JMenu("spacing");
        ButtonGroup spacings = new ButtonGroup();
        getCramButton(userController.getUserManager().getPreferences(userController.getCurrentUser()).getSpacingSameDay(), spacings);
        for (Iterator<AbstractButton> it = spacings.getElements().asIterator(); it.hasNext(); ) {
            AbstractButton button = it.next();
            spacing.add(button);
            button.setActionCommand(button.getText());
            button.addActionListener(parent);
            button.addActionListener(this);

        }
        this.add(spacing);

        frame.setTitle("Profile Settings");
        frame.setResizable(false);
        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Icon.png");
        frame.setIconImage(hauntedPyramid.getImage());
        frame.getContentPane().setBackground(new Color(233, 161, 161));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT);
        frame.setVisible(true);

//        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
//        JPanel panel1 = new JPanel();
//        panel1.setBackground(Color.ORANGE);
//        panel1.setPreferredSize(new Dimension(100, 50));
//        panel1.add(freeTime);
//        frame.add(panel1, BorderLayout.NORTH);
        setFreeTimePanel();
        setCheckItems();

    }

    private void getCramButton(String spacing, ButtonGroup spacings){
        JRadioButtonMenuItem none = new JRadioButtonMenuItem("none");
        JRadioButtonMenuItem small = new JRadioButtonMenuItem("small");
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem("medium");
        JRadioButtonMenuItem large = new JRadioButtonMenuItem("large");
        spacings.add(none);
        spacings.add(small);
        spacings.add(medium);
        spacings.add(large);
        switch (spacing) {
            case "none":
                spacings.setSelected(none.getModel(), true);
                break;
            case "small":
                spacings.setSelected(small.getModel(), true);
                break;
            case "medium":
                spacings.setSelected(medium.getModel(), true);
                break;
            case "large":
                spacings.setSelected(large.getModel(), true);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == freeTime){
            freeTime();
        } else if (e.getSource() == cram){
            userController.getUserManager().toggleEvenSpacing(userController.getCurrentUser());
        } else if (e.getSource() == procrastinate){
            userController.getUserManager().toggleProcrastinate(userController.getCurrentUser());
        } else if(e.getSource() == morningPerson){
             userController.getUserManager().toggleMorningPerson(userController.getCurrentUser());
        } else {
            userController.getUserManager().getPreferences(userController.getCurrentUser()).setSpacingSameDay(e.getActionCommand());
        }
    }

    private void freeTime(){
        new FreeTimeWindow(userController);
    }

    public void setFreeTimePanel() {
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.ORANGE);
        panel1.setPreferredSize(new Dimension(100, 50));

        JLabel startText = new JLabel("Start Time: ");
        JLabel endText = new JLabel("End Time: ");
        panel1.add(startText);
        panel1.add(startTime);
        panel1.add(endText);
        panel1.add(endTime);
        panel1.add(saveFT);

        frame.add(panel1, BorderLayout.NORTH);
    }

    public void setCheckItems(){
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.BLUE);
        panel2.setPreferredSize(new Dimension(100, 50));

        panel2.add(procrastinate);
        panel2.add(cram);
        panel2.add(morningPerson);
        frame.add(panel2, BorderLayout.CENTER);

    }


}

