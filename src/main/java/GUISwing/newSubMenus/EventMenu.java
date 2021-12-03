package GUISwing.newSubMenus;

import controllers.EventController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventMenu extends JMenu implements ActionListener {
    JMenuItem newEvent = new JMenuItem("new");
    EventController eventController;
    public EventMenu(EventController eventController){
        this.eventController = eventController;
        this.setText("Event");
        this.add(newEvent);
        newEvent.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newEvent){
            new NewEventWindow(eventController);
        }
    }
}
