package controllers;

import entities.Student;
import gateways.IOSerializable;
import usecases.CalendarManager;

import java.util.Scanner;

public class MainController {

    private LoginController loginController;
    private StudentController studentController;

    private IOSerializable ioSerializable;
    private Scanner scanner = new Scanner(System.in);

    public MainController() {
        //Instantiation of the IOSerializable
        this.ioSerializable = new IOSerializable();
        this.studentController = new StudentController(this.ioSerializable.hasSavedData());
        this.loginController = new LoginController(this.studentController);
        while (this.loginController.isLoggedIn()) {

        }
    }

    /**
     * Display the main screen. Used at and only at launch. Initially show screen of login/signup.
     */
    public void displayInitScreen() {
        //TODO: Implement this method using presenter. Make sure it overrides so the controller does not print anything.
    }

    /**
     * Display the main screen. Used at and only at launch after initialization via login/signup.
     */
    public void displayScreen() {
        //TODO: Implement this method using presenter. Make sure it overrides so the controller does not print anything.
    }

}
