package GUISwing;

import controllers.EventController;
import controllers.UserController;
import entities.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectEvent extends PopUpWindowFrame implements ActionListener{
    EventController eventController;
    UserController userController;
    ActionListener parent;
    public SelectEvent(EventController eventController, UserController userController, ActionListener parent){
        this.parent = parent;
        this.eventController = eventController;
        this.userController = userController;
        JScrollPane eventScroller = displayEvents();
        eventScroller.setBounds(0, 0, this.getWidth(), 2*this.getHeight()/3);
        this.setVisible(true);
    }

    private JScrollPane displayEvents(){
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        JScrollPane eventScroller= new JScrollPane(eventPanel);
        for (Event event: this.eventController.getEventManager().getAllEvents()) {
            JButton btn= new JButton(eventController.getEventManager().getName(event) + " start:  " +
                    eventController.getEventManager().getStart(event) + "  end: " +
                    eventController.getEventManager().getEnd(event));
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setActionCommand(eventController.getEventManager().getID(event).toString());
            btn.addActionListener(this);
            eventPanel.add(btn);
        }
        eventScroller.setPreferredSize(new Dimension(150, 100));
        add(eventScroller);
        setVisible(true);
        return eventScroller;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
 //       new EditEventWindow(this.mainController, UUID.fromString(e.getActionCommand()));
        this.dispose();
    }
}
