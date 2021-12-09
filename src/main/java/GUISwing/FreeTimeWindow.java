package GUISwing;

import controllers.UserController;
import helpers.Constants;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Taite Cullen
 */
public class FreeTimeWindow implements ActionListener {
    private final UserController userController;
    private final JFrame frame;
    private final MenuCreationHelper helper;
    private Map<JPanel, List<JComboBox<LocalTime>>> freeTimes;
    private JButton add = new JButton("add");
    private JButton save = new JButton("save");
    private final JPanel freeTimePanel = new JPanel();
    private final JScrollPane eventScroller = new JScrollPane(freeTimePanel);
    private final MeltParentWindow parent;
    private String buttonNum;
    private List<JPanel> panelList;

    /**
     * Constructor for FreeTimeWindow
     * @param userController Controller for User entity
     * @param parent parent window
     */
    public FreeTimeWindow(UserController userController, MeltParentWindow parent) {
        this.parent = parent;
        this.userController = userController;
        frame = new PopUpWindowFrame();
        this.helper = new MenuCreationHelper();
        showFreeTime();
        frame.add(eventScroller);
        saveButton();
        frame.setVisible(true);
    }

    /**
     * Method which shows the current free time for the user.
     */
    private void showFreeTime(){
        buttonNum = "0";
        panelList = new ArrayList<>();
        freeTimes = new HashMap<>();
        freeTimePanel.setLayout(new BoxLayout(freeTimePanel, BoxLayout.Y_AXIS));
        freeTimePanel.setBackground(Constants.WINDOW_COLOR);
        for (LocalTime start: userController.getCurrentFreeTime().keySet()){
            JPanel extraFreeTime = addFreeTime(start, userController.getCurrentFreeTime().get(start), buttonNum);
            freeTimePanel.add(extraFreeTime);
            buttonNum = String.valueOf(Integer.parseInt(buttonNum) + 1);
        }
        eventScroller.setPreferredSize(new Dimension(150, 100));
        eventScroller.setBounds(0, 0, frame.getWidth() - 20, frame.getHeight() - 100);
        eventScroller.setVisible(true);
    }

    /**
     * Method for adding new free time for the user
     * @param start Start time of free time
     * @param end End time of free time
     * @return A panel
     */
    private JPanel addFreeTime(LocalTime start, LocalTime end, String buttonNum){
        JPanel extraFreeTime = new JPanel(new FlowLayout());
        extraFreeTime.setBackground(Constants.WINDOW_COLOR);
        extraFreeTime.setPreferredSize(new Dimension(200, 30));
        JComboBox<LocalTime> startTime = helper.timeComboBox();
        JComboBox<LocalTime> endTime = helper.timeComboBox();
        startTime.setSelectedItem(start);
        endTime.setSelectedItem(end);
        JButton delete = new JButton("delete");
        extraFreeTime.add(new JLabel("start: "));
        extraFreeTime.add(startTime);
        extraFreeTime.add(new JLabel("end: "));
        extraFreeTime.add(endTime);
        extraFreeTime.add(delete);
        panelList.add(extraFreeTime);
        delete.setActionCommand(buttonNum);
        delete.addActionListener(this);
        List<JComboBox<LocalTime>> timeInfo = new ArrayList<>();
        timeInfo.add(startTime);
        timeInfo.add(endTime);
        freeTimes.put(extraFreeTime, timeInfo);
        return extraFreeTime;
    }


    /**
     * Method for adding buttons onto frame
     */
    private void saveButton() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Constants.WINDOW_COLOR);
        buttonPanel.setBounds(0, frame.getHeight()-100, frame.getWidth(), 100);
        save = new JButton("save");
        save.addActionListener(this);
        add = new JButton("add");
        add.addActionListener(this);
        buttonPanel.add(add);
        buttonPanel.add(save);

        frame.add(buttonPanel);
    }

    /**
     * add button adds a free time slot from 0:00 - 0:00 and refreshes
     * save button saves all free time currently displayed as the total set of User free time
     * delete buttons pass start times (keys in freetime map), these keys are removed and the frame is refreshed
     * @param e Action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) {
            List<LocalTime> timeList = new ArrayList<>();
            for (JPanel timePanel: freeTimes.keySet()) {
                timeList.add((LocalTime) Objects.requireNonNull(freeTimes.get(timePanel).get(0).getSelectedItem()));
            }
            LocalTime temp = LocalTime.of(0,0);
            while (timeList.contains(temp)) {
                temp = temp.plusMinutes(30);
                if (temp == LocalTime.of(23,0)) {
                    break;
                }
            }
            freeTimePanel.add(addFreeTime(temp, temp, buttonNum));
            buttonNum = String.valueOf(Integer.parseInt(buttonNum) + 1);
            frame.revalidate();
            frame.repaint();
        }

        else if (e.getSource() == save) {
            userController.getCurrentFreeTime().clear();
            for (JPanel timePanel : freeTimes.keySet()) {
                userController.addFreeTime((LocalTime) Objects.requireNonNull(freeTimes.get(timePanel).get(0).getSelectedItem()),
                        (LocalTime) freeTimes.get(timePanel).get(1).getSelectedItem());
            }
            parent.refresh();
            frame.dispose();
        }

        else {
            freeTimePanel.remove(panelList.get(Integer.parseInt(e.getActionCommand())));
            freeTimes.remove(panelList.get(Integer.parseInt(e.getActionCommand())));
            frame.revalidate();
            frame.repaint();
        }
    }
}
