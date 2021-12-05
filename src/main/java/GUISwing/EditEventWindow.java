package GUISwing;

import GUISwing.MainFrame;
import GUISwing.MainMenu;
import GUISwing.MenuCreationHelper;
import controllers.EventController;
import controllers.UserController;
import entities.UserPreferences;
import gateways.ICalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

public class EditEventWindow implements ActionListener {
    EventController eventController;
    UserController userController;
    UUID event;
    ActionListener parent;
    JFrame frame = new PopUpWindowFrame();
    JPanel datesPanel = new JPanel(new GridLayout(2, 4));
    JPanel infoPanel = new JPanel(new GridLayout(2, 1));
    JPanel moreOptionsPanel = new JPanel(new FlowLayout());
    JComboBox<YearMonth> startMonth = MenuCreationHelper.monthComboBox();
    JComboBox<YearMonth> endMonth = MenuCreationHelper.monthComboBox();
    JComboBox<Integer> startDate = MenuCreationHelper.dateJComboBox(startMonth.getItemAt(0), true);
    JComboBox<Integer> endDate = MenuCreationHelper.dateJComboBox(endMonth.getItemAt(0), false);
    JComboBox<LocalTime> startTime = MenuCreationHelper.timeComboBox();
    JComboBox<LocalTime> endTime = MenuCreationHelper.timeComboBox();

    JButton save = new JButton("save");
    JButton delete = new JButton("delete");
    JButton workSessions = new JButton("work sessions ...");
    JButton recursion = new JButton("repeats ...");

    JTextField name;
    JTextArea description;


    public EditEventWindow(UserController userController, EventController eventController, UUID event, ActionListener parent){
        this.parent = parent;
        this.eventController = eventController;
        this.userController = userController;
        this.event = event;
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(new Dimension( 500, 300));
        setNameField();
        setDateTimePickers();
        saveDeleteButtons();
        otherOptionsSetup();

        frame.setVisible(true);
    }

    public void setDateTimePickers(){
        frame.getContentPane().add(datesPanel);
        datesPanel.setBounds(5, 5, 100, 100);
        datesPanel.setBackground(Color.ORANGE);

        JLabel lblStart = new JLabel("start: ");
        setDateOptions(lblStart, startMonth, startDate, startTime,eventController.getEventManager().getStart(event));

        JLabel lblEnd = new JLabel("End: ");
        setDateOptions(lblEnd, endMonth, endDate, endTime, eventController.getEventManager().getEnd(event));

        startMonth.addActionListener(this);
        endMonth.addActionListener(this);
    }

    private void setNameField(){
        name = new JTextField(eventController.getEventManager().getName(event));
        description = new JTextArea(eventController.getEventManager().getDescription(event));
        frame.getContentPane().add(infoPanel);
        infoPanel.add(name);
        infoPanel.add(description);
    }

    private void setDateOptions(JLabel lblEnd, JComboBox<YearMonth> endMonth, JComboBox<Integer> endDate, JComboBox<LocalTime> endTime, LocalDateTime date) {
        datesPanel.add(lblEnd);
        datesPanel.add(endMonth);
        datesPanel.add(endDate);
        datesPanel.add(endTime);
        if (date != null){
            endMonth.setSelectedItem(YearMonth.of(date.getYear(), date.getMonthValue()));
            endTime.setSelectedItem(date.toLocalTime());
            endDate.setSelectedItem(date.getDayOfMonth());
        }else {
            endDate.setSelectedItem(null);
        }

    }

    private void saveDeleteButtons(){
        frame.getContentPane().add(save);
        frame.getContentPane().add(delete);
        save.setActionCommand("refresh");
        save.addActionListener(parent);
        save.addActionListener(this);
        delete.setActionCommand("refresh");
        delete.addActionListener(parent);
        delete.addActionListener(this);
    }

    private void otherOptionsSetup(){
        frame.getContentPane().add(moreOptionsPanel);
        moreOptionsPanel.add(workSessions);
        workSessions.addActionListener(this);
        moreOptionsPanel.add(recursion);
        recursion.addActionListener(this);
    }

    private void changeDays(ActionEvent e){
        if (e.getSource() == startMonth | e.getSource() == endMonth){
            startDate = MenuCreationHelper.dateJComboBox((YearMonth) Objects.requireNonNull(startMonth.getSelectedItem()), true);
            endDate = MenuCreationHelper.dateJComboBox((YearMonth) Objects.requireNonNull(endMonth.getSelectedItem()), true);
        }
    }
    private void save(ActionEvent e){
        if(e.getSource() == save){
            System.out.println(eventController.getEventManager().getStartWorking(event));
            eventController.getEventManager().setName(event, name.getText());
            eventController.getEventManager().setEnd(event, LocalDateTime.of(LocalDate.of((
                    (YearMonth) (Objects.requireNonNull(endMonth.getSelectedItem()))).getYear(), ((YearMonth) (endMonth.getSelectedItem())).
                    getMonthValue(), (Integer)(endDate.getSelectedItem())), (LocalTime) Objects.requireNonNull(endTime.getSelectedItem())));
            eventController.getEventManager().setDescription(event, description.getText());
            if (startDate.getSelectedItem() != null){
                eventController.getEventManager().setStart(event, LocalDateTime.of(LocalDate.of((
                        (YearMonth) (Objects.requireNonNull(startMonth.getSelectedItem()))).getYear(), ((YearMonth) (startMonth.getSelectedItem())).
                        getMonthValue(), (Integer)(startDate.getSelectedItem())), (LocalTime) Objects.requireNonNull(startTime.getSelectedItem())));
            }
            frame.dispose();
            System.out.println(eventController.getEventManager().getAllEventsFlatSplit());
            System.out.println(eventController.getEventManager().getStartWorking(event));
        }

    }

    private void delete(ActionEvent e){
        if (e.getSource() == delete){
            eventController.getEventManager().remove(event);
            frame.dispose();
        }
    }

    private void workSessions(ActionEvent e){
        if (e.getSource() == workSessions){
            new WorkSessionEdit(eventController, userController, event);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        changeDays(e);
        save(e);
        delete(e);
        workSessions(e);
    }
}
