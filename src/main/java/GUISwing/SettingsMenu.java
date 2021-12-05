package GUISwing;

import controllers.UserController;
import helpers.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Objects;

public class SettingsMenu extends JMenu implements ActionListener {
    JMenuItem profile;
    JCheckBoxMenuItem cram;
    JCheckBoxMenuItem procrastinate;
    JMenu spacing;
    UserController userController;
    JCheckBoxMenuItem morningPerson;

    JFrame frame = new JFrame();

    JButton saveFT = new JButton("save");

    JButton saveName = new JButton("save name");
    JTextField changeName = new JTextField();

    JComboBox<LocalTime> startTime = MenuCreationHelper.timeComboBox();
    JComboBox<LocalTime> endTime = MenuCreationHelper.timeComboBox();


    ActionListener parent;

    public SettingsMenu(UserController userController, ActionListener parent){
        this.userController = userController;
        this.parent = parent;
        this.setText("Settings");
        this.setVisible(true);


        profile = new JMenuItem("profile settings");
        profile.addActionListener(this);
        this.add(profile);

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
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(Constants.POPUP_WIDTH, 370);
        frame.setVisible(false);

        setFreeTimePanel();
        setCheckItems();
        setChangeNameField();

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
        if (e.getSource() == saveFT){
            freeTime();
        } else if (e.getSource() == cram){
            userController.getUserManager().toggleEvenSpacing(userController.getCurrentUser());
            System.out.println("cram toggled");
        } else if (e.getSource() == procrastinate){
            userController.getUserManager().toggleProcrastinate(userController.getCurrentUser());
            System.out.println("Procrastinate toggled");
        } else if(e.getSource() == morningPerson) {
            userController.getUserManager().toggleMorningPerson(userController.getCurrentUser());
            System.out.println("morning person toggled");
        } else if(e.getSource() == saveName){
            changeName();

        } else{
            userController.getUserManager().getPreferences(userController.getCurrentUser()).setSpacingSameDay(e.getActionCommand());
        }
    }

    private void freeTime(){
        LocalTime start = (LocalTime) Objects.requireNonNull(startTime.getSelectedItem());
        LocalTime end = (LocalTime) Objects.requireNonNull(endTime.getSelectedItem());

        this.userController.getUserManager().addFreeTime(userController.getCurrentUser(), start, end);
        System.out.println("Free time saved successfully!");
//        new FreeTimeWindow(userController);
    }

    private void changeName(){
        this.userController.getUserManager().setName(userController.getCurrentUser(), changeName.getText());
        System.out.println("Changed Name Successfully!");
    }


    public void setFreeTimePanel() {
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.ORANGE);
        panel1.setPreferredSize(new Dimension(100, 63));

        JLabel text = new JLabel("Set Free Time    ");
        JLabel startText = new JLabel("Start Time: ");
        JLabel endText = new JLabel("End Time: ");

        panel1.add(text);
        panel1.add(startText);
        panel1.add(startTime);
        panel1.add(endText);
        panel1.add(endTime);
        saveFT.addActionListener(this);
        panel1.add(saveFT);

        frame.add(panel1, BorderLayout.NORTH);
    }

    public void setCheckItems(){
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.GRAY);
        panel2.setPreferredSize(new Dimension(100, 70));

        JLabel instructions1 = new JLabel("Procrastinate will schedule Work Sessions more towards the Deadline.");
        JLabel instructions2 = new JLabel("Cram will be schedule on days they fit as opposed to even day spacing.");
        JLabel instructions3 = new JLabel("if morning person is on, your events will be scheduled as early in the");
        JLabel instructions3b = new JLabel("    day as possible.");
        JLabel instructions4 = new JLabel("if multiple work sessions of the same event occur on the same day, they");
        JLabel instructions4b = new JLabel("    will be scheduled with spacing between according to 'work session ");
        JLabel instructions4c = new JLabel(     "(if it is null, they will be merged).");


        panel2.add(procrastinate);
        panel2.add(cram);
        panel2.add(morningPerson);
        panel2.add(instructions1);
        panel2.add(instructions2);
        panel2.add(instructions3);
        panel2.add(instructions3b);
        panel2.add(instructions4);
        panel2.add(instructions4b);
        panel2.add(instructions4c);

        frame.add(panel2, BorderLayout.CENTER);

    }

    public void setChangeNameField(){
        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.GREEN);
        panel3.setPreferredSize(new Dimension(100, 50));
        saveName.addActionListener(this);
        changeName.setPreferredSize(new Dimension(75, 25));

        JLabel text = new JLabel("Change Name: ");
        panel3.add(text);
        panel3.add(changeName);
        panel3.add(saveName);
        frame.add(panel3, BorderLayout.SOUTH);
    }

    public void display(){
        frame.setVisible(true);
    }

}

