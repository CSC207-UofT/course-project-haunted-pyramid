package GUISwing;

import GUISwing.MainFrame;
import controllers.*;
import gateways.IOSerializable;
import presenters.MenuStrategies.DisplayMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class logInWindow implements ActionListener {
    JFrame frame = new MainFrame();
    JTextField fldUserName = new JTextField();
    JPasswordField fldPassword = new JPasswordField();
    JLabel lblUserName = new JLabel("username: ");
    JLabel lblPassword = new JLabel("password");
    JButton btnLogIn = new JButton("Log In");
    JButton btnSignUp = new JButton("Sign up");
    JLabel lblLoginMessage = new JLabel();
    MainController mainController;
    LoginController loginController;

    public logInWindow(){
        this.mainController = new MainController();
        this.loginController = new LoginController(mainController.getUserController());

        btnLogIn.addActionListener(this);
        btnSignUp.addActionListener(this);


        lblUserName.setBounds(100, 150, 100, 20);
        lblPassword.setBounds(100, 190, 100, 20);
        fldUserName.setBounds((int) (lblUserName.getBounds().getX() + 150), lblUserName.getY(), 100, 20);
        fldUserName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fldPassword.setBounds((int) (lblPassword.getBounds().getX() + 150), lblPassword.getY(), 100, 20);
        fldPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnLogIn.setBounds(100, 300, 100, 20);
        btnSignUp.setBounds(100, 350, 100, 20);
        lblUserName.setVisible(true);
        lblPassword.setVisible(true);
        fldPassword.setVisible(true);
        fldUserName.setVisible(true);
        btnLogIn.setVisible(true);
        btnSignUp.setVisible(true);
        lblLoginMessage.setBounds(100, 240, 400, 20);

        frame.add(lblUserName);
        frame.add(lblPassword);
        frame.add(fldUserName);
        frame.add(fldPassword);
        frame.add(btnSignUp);
        frame.add(btnLogIn);
        frame.add(lblLoginMessage);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogIn){
            loginController.login(fldUserName.getText(), Arrays.toString(fldPassword.getPassword()));
            if (loginController.isLoggedIn()){
                frame.dispose();
                IOSerializable ioSerializable = new IOSerializable(true);
                this.mainController.setEventController(new EventController(ioSerializable.hasSavedData(), ioSerializable, mainController.getUserController()));
                MainMenu mainMenu= new MainMenu(mainController);
                mainMenu.display();
            }
            lblLoginMessage.setText("incorrect username or password - please try again");
        }
        if(e.getSource() == btnSignUp){
            loginController.signUp(fldUserName.getText(), Arrays.toString(fldPassword.getPassword()));
            lblLoginMessage.setText("Successfully signed up - please log in");
        }
        fldPassword.setText("");
        fldUserName.setText("");
        lblLoginMessage.setVisible(true);
    }

    public static void main(String[] args){
        new logInWindow();
    }
}
