package GUISwing;

import controllers.UserController;
import entities.User;
import entities.UserPreferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class SettingsMenu extends JMenu implements ActionListener {
    JMenuItem freeTime;
    JMenuItem profile;
    JCheckBoxMenuItem cram;
    JCheckBoxMenuItem procrastinate;
    JMenu spacing;
    UserController userController;
    JCheckBoxMenuItem morningPerson;

    ActionListener parent;

    public SettingsMenu(UserController userController, ActionListener parent){
        this.userController = userController;
        this.parent = parent;
        this.setText("Settings");
        this.setVisible(true);


        profile = new JMenuItem("profile settings");
        profile.addActionListener(this);
        this.add(profile);


        freeTime = new JMenuItem("free time");
        freeTime.setIcon(new ImageIcon("/res/freeTimeIcon.jpg"));
        freeTime.addActionListener(this);
        this.add(freeTime);


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
}
