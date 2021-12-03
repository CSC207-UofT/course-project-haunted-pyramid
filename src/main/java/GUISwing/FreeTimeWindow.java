package GUISwing;

import controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class FreeTimeWindow {
    UserController userController;
    final JFrame frame;
    ScrollPane panel;
    JPanel pane;

    public FreeTimeWindow(UserController userController) {
        this.userController = userController;
        frame = new MainFrame();
        frame.setSize(500, 500);
        panel = new ScrollPane();
        pane = new JPanel();
        panel.add(pane);
        pane.setBounds(frame.getWidth() / 4, frame.getHeight() / 6, frame.getWidth() / 2, 2 * frame.getHeight() / 3);
        frame.add(pane);
        showFreeTime();
        frame.setVisible(true);
        pane.setVisible(true);
    }

    private void showFreeTime() {
        int y = 0;
        for (LocalTime start : userController.getCurrentFreeTime().keySet()) {
            JComboBox<LocalTime> startTime = MenuCreationHelper.timeComboBox();
            startTime.setBounds(0, y, 50, 20);
            startTime.setSelectedItem(start);
            JComboBox<LocalTime> endTime = MenuCreationHelper.timeComboBox();
            endTime.setBounds(100, y, 50, 20);
            endTime.setSelectedItem(userController.getCurrentFreeTime().get(start));
            pane.add(startTime);
            pane.add(endTime);
            y += 50;
        }
    }

    private void addButton() {

    }

    private void saveButton() {

    }

    private void popUp() {

    }

}
