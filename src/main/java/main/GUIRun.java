package main;


import GUISwing.logInWindow;
import controllers.LoginController;

public class GUIRun {
    public static void main(String[] args) {
        //run log in GUI (which will run main GUI that runs everything else)
        new logInWindow();
    }
}