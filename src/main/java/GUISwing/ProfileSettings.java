package GUISwing;

import controllers.UserController;
import helpers.Constants;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Profile Settings page
 * @author Shameel Farooq
 * @see UserController
 */

public class ProfileSettings implements ActionListener {

    private final UserController userController;
    private final MeltParentWindow parent;
    private final JFrame frame = new PopUpWindowFrame();
    private final JButton saveFT = new JButton("save");
    private final JButton saveName = new JButton("save name");
    private final JTextField changeName = new JTextField();
    private final JComboBox<LocalTime> startTime;
    private final JComboBox<LocalTime> endTime;

    /**
     * Constructor for ProfileSettings
     * @param uc UserController
     * @param parent MeltParentWindow
     */
    public ProfileSettings(UserController uc, MeltParentWindow parent){
        this.userController = uc;
        this.parent = parent;
        MenuCreationHelper helper = new MenuCreationHelper();
        startTime = helper.timeComboBox();
        endTime = helper.timeComboBox();
        frame.setTitle("Profile Settings");
        frame.setResizable(false);
        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Icon.png");
        frame.setIconImage(hauntedPyramid.getImage());
        frame.getContentPane().setBackground(new Color(233, 161, 161));
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(Constants.POPUP_WIDTH, 370);
        frame.setVisible(false);

        setFreeTimePanel();
        setCheckItems();
        setChangeNameField();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveFT){
            freeTime();
            this.parent.refresh();
        } else if(e.getSource() == saveName){
            changeName();
            this.parent.refresh();
        }
    }

    /**
     * Method for setting free time according to the input of the user
     */
    private void freeTime(){
        LocalTime start = (LocalTime) Objects.requireNonNull(startTime.getSelectedItem());
        LocalTime end = (LocalTime) Objects.requireNonNull(endTime.getSelectedItem());

        this.userController.getUserManager().addFreeTime(userController.getCurrentUser(), start, end);
        System.out.println("Free time saved successfully!");
    }

    /**
     * Method for changing name according to the input of the user
     */
    private void changeName(){
        this.userController.getUserManager().setName(userController.getCurrentUser(), changeName.getText());
        System.out.println("Changed Name Successfully!");
    }

    public void setFreeTimePanel() {
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.ORANGE);
        panel1.setPreferredSize(new Dimension(100, 63));

        JLabel text = new JLabel("Set Free Time    ");
        JLabel startText = new JLabel("Start Time: ");
        JLabel endText = new JLabel("End Time: ");

        panel1.add(text);
        panel1.add(startText);
        panel1.add(startTime);
        panel1.add(endText);
        panel1.add(endTime);
        saveFT.addActionListener(this);
        saveFT.addActionListener((ActionListener) parent);
        panel1.add(saveFT);

        frame.add(panel1, BorderLayout.NORTH);
    }

    /**
     * Method for adding the panel which contains the information of different preferences. The preferences are
     * procrastinate, cram and morning person. These preferences allow the work sessions to be scheduled accordingly.
     */
    public void setCheckItems(){
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.GRAY);
        panel2.setPreferredSize(new Dimension(100, 70));

        JLabel instructions1 = new JLabel("Procrastinate will schedule Work Sessions more towards the Deadline.");
        JLabel instructions2 = new JLabel("Cram will be schedule on days they fit as opposed to even day spacing.");
        JLabel instructions3 = new JLabel("if morning person is on, your events will be scheduled as early in the");
        JLabel instructions3b = new JLabel("    day as possible.");
        JLabel instructions4 = new JLabel("if multiple work sessions of the same event occur on the same day, they");
        JLabel instructions4b = new JLabel("    will be scheduled with spacing between according to 'work session ");
        JLabel instructions4c = new JLabel(     "(if it is null, they will be merged).");

        panel2.add(instructions1);
        panel2.add(instructions2);
        panel2.add(instructions3);
        panel2.add(instructions3b);
        panel2.add(instructions4);
        panel2.add(instructions4b);
        panel2.add(instructions4c);

        frame.add(panel2, BorderLayout.CENTER);
    }

    /**
     * Method which adds a new panel to the bottom of the window. Panel contains the text box in which the user can
     * input the name they would like.
     */
    public void setChangeNameField(){
        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.GREEN);
        panel3.setPreferredSize(new Dimension(100, 50));
        saveName.addActionListener(this);
        changeName.setPreferredSize(new Dimension(75, 25));

        JLabel text = new JLabel("Change Name: ");
        panel3.add(text);
        panel3.add(changeName);
        panel3.add(saveName);
        frame.add(panel3, BorderLayout.SOUTH);
    }

    /**
     * Method which changes displays the pop up window of the Profile settings.
     */
    public void display(){
        frame.setVisible(true);
    }
}
