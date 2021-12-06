package GUISwing;

import controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Objects;

public class FreeTimeWindow implements ActionListener {
    UserController userController;
    final JFrame frame;
    private MenuCreationHelper helper;
    JScrollPane slotsScrollPain;
    JPanel panel = new JPanel(new GridLayout(0, 5));

    JButton add = new JButton("add");
    JButton save = new JButton("save");

    public FreeTimeWindow(UserController userController) {
        this.userController = userController;
        frame = new PopUpWindowFrame();
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.setSize(500, 500);
        this.helper = new MenuCreationHelper();
        slotsScrollPain = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(slotsScrollPain);
        showFreeTime();
        addButton();
        saveButton();

        frame.setVisible(true);
        panel.setVisible(true);
    }

    private void showFreeTime() {
        for (LocalTime start : userController.getCurrentFreeTime().keySet()) {
            addFreeTime(start, userController.getCurrentFreeTime().get(start));
        }
    }

    private void addFreeTime(LocalTime start, LocalTime end){
        JComboBox<LocalTime> startTime = helper.timeComboBox();
        startTime.setSelectedItem(start);
        JComboBox<LocalTime> endTime = helper.timeComboBox();
        endTime.setSelectedItem(end);
        JButton delete = new JButton("delete");
        panel.add(new JLabel("start: "));
        panel.add(startTime);
        panel.add(new JLabel("end: "));
        panel.add(endTime);
        panel.add(delete);
        delete.setActionCommand(Objects.requireNonNull(startTime.getSelectedItem()).toString());
        delete.addActionListener(this);
    }

    private void addButton() {
        add = new JButton("add");
        add.addActionListener(this);
        frame.add(add);
    }

    private void saveButton() {
        save = new JButton("save");
        save.addActionListener(this);
        frame.add(save);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add){
            addFreeTime(LocalTime.of(0, 0), LocalTime.of(0, 0));
        }else {
            String key = e.getActionCommand();
            String[] hourMin = key.split(":");
            userController.getCurrentFreeTime().remove(LocalTime.of(Integer.parseInt(hourMin[0]), Integer.parseInt(hourMin[1])));
            new FreeTimeWindow(userController);
            frame.dispose();
        }
    }
}
