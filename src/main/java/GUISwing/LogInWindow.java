package GUISwing;

import controllers.*;
import gateways.IOSerializable;
import helpers.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LogInWindow implements ActionListener {
    JFrame frame = new MainFrame();
    JTextField fldUserName = new JTextField();
    JTextField fldPassword = new JTextField();
    JLabel lblUserName = new JLabel("username: ");
    JLabel lblPassword = new JLabel("password: ");
    JButton btnLogIn = new JButton("Log In");
    JButton btnSignUp = new JButton("Sign up");
    JLabel lblLoginMessage = new JLabel();
    MainController mainController;
    LoginController loginController;

    public LogInWindow(){
        this.mainController = new MainController();
        this.loginController = this.mainController.getLoginController();
        JPanel imagePanel = createImagePanel();
        addImage(imagePanel);
        JPanel contributorPanel = createContributorPanel();
        addTeamName(imagePanel);
        addContributors(contributorPanel);
        JPanel logInPanel = new JPanel();
        this.frame.add(logInPanel);
        logInPanel.setBounds(0, Constants.WINDOW_HEIGHT/3, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/3);
        logInPanel.setBackground(new Color(233, 161, 161));
        logInPanel.setLayout(null);
        updateLogInPanel(logInPanel);
        setUpUserNameLabel();
        setUpPasswordLabel();
        addLogInMessage();
        buttonSetUp();
        frame.setVisible(true);
    }

    private void addImage(JPanel imagePanel) {
        JLabel imageLabel = new JLabel();
        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Logo.png");
        imageLabel.setIcon(hauntedPyramid);
        imagePanel.add(imageLabel);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.CENTER);
        imageLabel.setBounds(200, 150, Constants.WINDOW_WIDTH - 444, Constants.WINDOW_HEIGHT - 800);
    }

    private void buttonSetUp() {
        btnLogIn.setBounds(1164/2 + 30, 200, 100, 30);
        btnSignUp.setBounds(1164/2 + 135, 200, 100, 30);
        btnLogIn.addActionListener(this);
        btnSignUp.addActionListener(this);
    }

    private void addLogInMessage() {
        lblLoginMessage.setLayout(null);
        lblLoginMessage.setHorizontalAlignment(JLabel.CENTER);
        lblLoginMessage.setVerticalAlignment(JLabel.CENTER);
        lblLoginMessage.setHorizontalTextPosition(JLabel.CENTER);
        lblLoginMessage.setVerticalTextPosition(JLabel.CENTER);
        lblLoginMessage.setBounds(1044/2, 170, 400, 20);
    }

    private void setUpPasswordLabel() {
        lblPassword.setBounds(1220/2, 135, 100, 25);
        lblPassword.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 15));
        fldPassword.setBounds((int) (lblPassword.getBounds().getX() + 80), lblPassword.getY(), 120, 25);
        fldPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void setUpUserNameLabel() {
        lblUserName.setBounds(1220/2, 100, 100, 25);
        lblUserName.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 15));
        fldUserName.setBounds((int) (lblUserName.getBounds().getX() + 80), lblUserName.getY(), 120, 25);
        fldUserName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void updateLogInPanel(JPanel logInPanel) {
        logInPanel.add(lblUserName);
        logInPanel.add(lblPassword);
        logInPanel.add(fldUserName);
        logInPanel.add(fldPassword);
        logInPanel.add(btnSignUp);
        logInPanel.add(btnLogIn);
        logInPanel.add(lblLoginMessage);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();
        this.frame.add(imagePanel);
        imagePanel.setBounds(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/3);
        imagePanel.setBackground(new Color(233, 161, 161));
        imagePanel.setLayout(null);
        return imagePanel;
    }

    private JPanel createContributorPanel() {
        JPanel contributorPanel = new JPanel();
        this.frame.add(contributorPanel);
        contributorPanel.setLayout(null);
        contributorPanel.setBackground(new Color(233, 161, 161));
        contributorPanel.setBounds(0, 1000*2/3, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/3);
        return contributorPanel;
    }

    private void addTeamName(JPanel imagePanel) {
        JLabel teamName = new JLabel();
        teamName.setText("Haunted Pyramid Calendar App");
        imagePanel.add(teamName);
        teamName.setFont(new Font("MV Boli", Font.BOLD, 30));
        teamName.setHorizontalAlignment(JLabel.CENTER);
        teamName.setVerticalAlignment(JLabel.CENTER);
        teamName.setHorizontalTextPosition(JLabel.CENTER);
        teamName.setVerticalTextPosition(JLabel.CENTER);
        teamName.setBounds(200, 50, 1000, 50);
    }

    private void addContributors(JPanel contributorPanel) {
        JLabel contributors = new JLabel("Contributors:  Malik Lahlou,  Shahzada Muhammad Shameel Farooq,  Sebin Im,  Seo Won Yi,  Taite Cullen");
        contributors.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20));
        contributors.setBounds(200, 50, 1000, 100);
        contributorPanel.add(contributors);
        contributors.setHorizontalAlignment(JLabel.CENTER);
        contributors.setVerticalAlignment(JLabel.CENTER);
        contributors.setHorizontalTextPosition(JLabel.CENTER);
        contributors.setVerticalTextPosition(JLabel.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogIn){
            loginController.login(fldUserName.getText(), fldPassword.getText());
            if (loginController.isLoggedIn()){
                IOSerializable ioSerializable = new IOSerializable(true);
                this.mainController.setEventController(new EventController(ioSerializable.hasSavedData(), ioSerializable,
                        mainController.getUserController()));
                MainMenu mainMenu = new MainMenu(mainController);
                mainMenu.display();
                frame.dispose();
            }
            lblLoginMessage.setText("Incorrect Username or Password - Please Try Again");
        }
        if(e.getSource() == btnSignUp){
            loginController.signUp(fldUserName.getText(), fldPassword.getText());
            lblLoginMessage.setText("Successfully Signed up - Please Log In");
        }
        fldPassword.setText("");
        fldUserName.setText("");
        lblLoginMessage.setVisible(true);
    }
}
