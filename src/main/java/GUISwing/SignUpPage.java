package GUISwing;

import controllers.LoginController;
import helpers.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage implements ActionListener {

    LoginController loginController;

    JFrame frame = new JFrame();

    JButton signUpButton = new JButton("Sign Up");
    JButton cancelButton = new JButton("Cancel");

    JLabel addInfo = new JLabel("Please enter your desired account information.");
    JLabel addName = new JLabel("First & Last name:");
    JLabel addUsername = new JLabel("Username:");
    JLabel addPassword = new JLabel("Password:");

    JTextField name = new JTextField();
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();

    public SignUpPage(LoginController loginController) {
        this.loginController = loginController;

        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Icon.png");
        frame.setIconImage(hauntedPyramid.getImage());
        frame.setTitle("Sign Up");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Constants.WINDOW_COLOR);
        frame.setVisible(true);

        this.createButtons();
        this.createLabels();
        this.createFields();
        this.addToFrame();
    }

    public void createButtons() {
        signUpButton.setBounds(135, 190, 100, 25);
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(this);
        cancelButton.setBounds(235, 190, 100, 25);
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);
    }

    public void createLabels() {
        addInfo.setBounds(80, 20, frame.getX(), 50);
        addInfo.setFont(new Font("Monaco", Font.BOLD, 14));

        addName.setBounds(70, 65, 150, 50);
        addName.setFont(new Font("Arial", Font.PLAIN, 12));
        addUsername.setBounds(107, 100, 150, 50);
        addUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        addPassword.setBounds(110, 135, 150, 50);
        addPassword.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    public void createFields() {
        name.setBounds(175, 80, 170, 20);
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        username.setBounds(175, 115, 170, 20);
        username.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        password.setBounds(175, 150, 170, 20);
        password.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void addToFrame() {
        frame.add(signUpButton);
        frame.add(cancelButton);
        frame.add(addInfo);
        frame.add(addName);
        frame.add(addUsername);
        frame.add(addPassword);
        frame.add(name);
        frame.add(username);
        frame.add(password);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpButton) {
            if (name.getText().equals("") || username.getText().equals("") || password.getPassword().length == 0) {
                addInfo.setText("Please make sure all the fields are filled in.");
                name.setText("");
                username.setText("");
                password.setText("");
            } else {
                loginController.signUp(name.getText(), username.getText(), String.valueOf(password.getPassword()));
                addInfo.setText("Successfully Signed up - Please Log In");
                frame.dispose();
            }
        }
        if (e.getSource() == cancelButton) {
            frame.dispose();
        }
    }
}
