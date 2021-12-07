package GUISwing;

import controllers.MainController;
import entities.Event;
import interfaces.EventInfoGetter;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class SelectEvent extends PopUpWindowFrame implements ActionListener {

    MainController mc;
    MeltParentWindow parent;
    EventInfoGetter eventInfoGetter;

    public SelectEvent (MainController mc, MeltParentWindow parent, EventInfoGetter eventInfoGetter) {
        this.parent = parent;
        this.mc = mc;
        this.eventInfoGetter = eventInfoGetter;
        JScrollPane eventScroller = displayEvents();
        eventScroller.setBounds(0, 0, this.getWidth(), 2*this.getHeight()/3);
        this.setVisible(true);
    }

    private JScrollPane displayEvents(){
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        JScrollPane eventScroller= new JScrollPane(eventPanel);
        for (Event event: eventInfoGetter.getAllEvents()) {
            JButton btn= new JButton(eventInfoGetter.getName(eventInfoGetter.getID(event)) + " start:  " +
                    eventInfoGetter.getStart(eventInfoGetter.getID(event)) + "  end: " +
                    eventInfoGetter.getEnd(eventInfoGetter.getID(event)));
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setActionCommand(eventInfoGetter.getID(event).toString());
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
        new EditEventWindow(mc, eventInfoGetter,UUID.fromString(e.getActionCommand()), parent);
        this.dispose();
    }
}
