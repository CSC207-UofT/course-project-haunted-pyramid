package GUISwing;

import controllers.EventController;
import controllers.MainController;
import controllers.UserController;
import entities.Event;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class SelectEvent extends PopUpWindowFrame implements ActionListener{
    MainController mc;
    MeltParentWindow parent;
    public SelectEvent(MainController mc, MeltParentWindow parent){
        this.parent = parent;
        this.mc = mc;
        JScrollPane eventScroller = displayEvents();
        eventScroller.setBounds(0, 0, this.getWidth(), 2*this.getHeight()/3);
        this.setVisible(true);
    }

    private JScrollPane displayEvents(){
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        JScrollPane eventScroller= new JScrollPane(eventPanel);
        for (Event event: this.mc.getEventController().getAllEvents()) {
            JButton btn= new JButton(this.mc.getEventController().getName(this.mc.getEventController().getID(event)) + " start:  " +
                    this.mc.getEventController().getStart(this.mc.getEventController().getID(event)) + "  end: " +
                    this.mc.getEventController().getEnd(this.mc.getEventController().getID(event)));
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setActionCommand(this.mc.getEventController().getID(event).toString());
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
        new EditEventWindow(mc, UUID.fromString(e.getActionCommand()), parent);
        this.dispose();
    }
}
