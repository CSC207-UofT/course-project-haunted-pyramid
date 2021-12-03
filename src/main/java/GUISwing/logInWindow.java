package GUISwing;

import GUISwing.MainFrame;
import controllers.CalendarController;
import controllers.LoginController;
import controllers.UserController;
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
    IOSerializable ioSerializable;
    UserController userController;
    LoginController loginController;

    public logInWindow(){
        this.ioSerializable = new IOSerializable(true);
        this.userController = new UserController(ioSerializable.hasSavedData(), ioSerializable);
        this.loginController = new LoginController(userController);

        btnLogIn.addActionListener(this);
        btnSignUp.addActionListener(this);


        lblUserName.setBounds(100, 150, 100, 20);
        lblPassword.setBounds(100, 200, 100, 20);
        fldUserName.setBounds((int) (lblUserName.getBounds().getX() + 150), lblUserName.getY(), 100, 20);
        fldUserName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fldPassword.setBounds((int) (lblPassword.getBounds().getX() + 150), lblPassword.getY(), 100, 20);
        fldPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnLogIn.setBounds(0, 300, 100, 20);
        btnSignUp.setBounds(0, 350, 100, 20);
        lblUserName.setVisible(true);
        lblPassword.setVisible(true);
        fldPassword.setVisible(true);
        fldUserName.setVisible(true);
        btnLogIn.setVisible(true);
        btnSignUp.setVisible(true);

        frame.add(lblUserName);
        frame.add(lblPassword);
        frame.add(fldUserName);
        frame.add(fldPassword);
        frame.add(btnSignUp);
        frame.add(btnLogIn);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogIn){
            loginController.login(fldUserName.getText(), Arrays.toString(fldPassword.getPassword()));
            if (loginController.isLoggedIn()){
                frame.dispose();
                MainMenu mainMenu= new MainMenu(new MainFrame());
                mainMenu.display();

            }
        }
        if(e.getSource() == btnSignUp){
            loginController.signUp(fldUserName.getText(), Arrays.toString(fldPassword.getPassword()));
            frame.add(new JLabel("now you may log in"));
        }
    }

    public static void main(String[] args){
        new logInWindow();
    }
}
