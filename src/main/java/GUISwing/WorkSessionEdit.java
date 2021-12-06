package GUISwing;

import controllers.EventController;
import controllers.UserController;
import interfaces.MeltParentWindow;
import usecases.events.worksessions.WorkSessionScheduler;
import usecases.events.worksessions.WorkSessionSchedulerBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class WorkSessionEdit implements ActionListener{
    JFrame frame = new PopUpWindowFrame();
    JLabel hourslbl = new JLabel("total hours: ");
    JComboBox<Long> totalHours = fromTo(50);
    JLabel sessionlbl = new JLabel("session length: ");
    JComboBox<Long> sessionLength = fromTo(10);
    JButton save = new JButton("save");
    EventController eventController;
    UUID event;
    MeltParentWindow parent;

    public WorkSessionEdit(EventController eventController, MeltParentWindow parent, UUID event){
        this.parent = parent;
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        this.event = event;
        this.eventController = eventController;
        totalHours.setSelectedItem(eventController.getEventManager().getTotalHoursNeeded(event));
        sessionLength.setSelectedItem(eventController.getEventManager().getEventSessionLength(event));
        frame.add(hourslbl);
        frame.add(totalHours);
        frame.add(sessionlbl);
        frame.add(sessionLength);
        save.addActionListener(this);

        frame.add(save);
    }

    private JComboBox<Long> fromTo(Integer to){
        Long[] list = new Long[to- (Integer) 0];
        for (long i = 0; i < to - (Integer) 0; i+=1){
            list[(int)(i)] = i;
        }
        return new JComboBox<>(list);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventController.setTotalHours(event, (Long) totalHours.getSelectedItem());
        eventController.setSessionLength(event, (Long) sessionLength.getSelectedItem());
        parent.enableFrame();
        frame.dispose();
    }
}
