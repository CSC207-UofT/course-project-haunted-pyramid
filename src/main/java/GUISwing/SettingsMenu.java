package GUISwing;

import controllers.UserController;
import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class SettingsMenu extends JMenu implements ActionListener {
    JMenuItem freeTime;
    JMenuItem profile;
    ButtonGroup personType;
    JCheckBoxMenuItem cram;
    JMenu spacing;
    UserController userController;

    public SettingsMenu(UserController userController){
        this.userController = userController;
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
        personType = new ButtonGroup();
        JRadioButtonMenuItem morningPerson = new JRadioButtonMenuItem("morning person");
        morningPerson.setSelected(true);
        JRadioButtonMenuItem eveningPerson = new JRadioButtonMenuItem("evening person");
        personType.add(morningPerson);
        personType.add(eveningPerson);
        this.add(morningPerson);
        this.add(eveningPerson);


        this.addSeparator();
        cram = new JCheckBoxMenuItem("cram");
        this.add(cram);

        spacing = new JMenu("spacing");
        ButtonGroup spacings = new ButtonGroup();
        JRadioButtonMenuItem none = new JRadioButtonMenuItem("none");
        none.setSelected(true);
        spacings.add(none);
        spacings.add(new JRadioButtonMenuItem("small"));
        spacings.add(new JRadioButtonMenuItem("medium"));
        spacings.add(new JRadioButtonMenuItem("large"));
        for (Iterator<AbstractButton> it = spacings.getElements().asIterator(); it.hasNext(); ) {
            AbstractButton button = it.next();
            spacing.add(button);
        }
        this.add(spacing);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == freeTime){
            freeTime();
            System.out.println("this worked");
        }
    }

    private void freeTime(){
        new FreeTimeWindow(userController);
    }
}
