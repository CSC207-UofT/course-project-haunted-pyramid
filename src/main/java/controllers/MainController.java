package controllers;

import entities.Student;
import gateways.IOSerializable;
import presenters.CalendarPresenter;
import usecases.CalendarManager;

import java.util.Scanner;

public class MainController {

    private CalendarManager calendarManager; //DELETE AFTER - this is only temporary for Phase 0
    private CalendarPresenter calendarPresenter;
    private LoginController loginController;
    private StudentController studentController;

    private IOSerializable ioSerializable;
    private Scanner scanner = new Scanner(System.in);

    public MainController() {
        //Instantiation of the IOSerializable
        this.ioSerializable = new IOSerializable();
        this.studentController = new StudentController(this.ioSerializable.hasSavedData());
        this.loginController = new LoginController(this.studentController);
        this.calendarPresenter = new CalendarPresenter(this.calendarManager);
        this.displayInitScreen();
    }

    /**
     * Display the main screen. Used at and only at launch. Initially show screen of login/signup/exit.
     * Temporary for now since the presenter isn't fully implemented.
     */
    public void displayInitScreen() {
        while (!this.loginController.isLoggedIn()) {
            System.out.println("Type 'Sign Up' to create a new account, and type 'Log In' to login.");
            System.out.println("Type 'Exit' to exit this program.");
            String SUorLI = scanner.nextLine();
            if (SUorLI.equalsIgnoreCase("Sign Up")) {
                this.loginController.signUp();
            } else if (SUorLI.equalsIgnoreCase("Log In")) {
                this.loginController.login();
            } else if (SUorLI.equalsIgnoreCase("Exit")) {
                this.saveAndExitProgram();
            } else {
                System.out.println("Invalid input! Try again.");
            }
        }
    }

    /**
     * Display the main screen. Used at and only at launch after initialization via login/signup.
     * Temporary for now since the presenter isn't fully implemented.
     */
    public void displayScreen() {
        //this.calendarPresenter.testPresenter();

    }

    /**
     * Save and exit the program. Only save students into students.ser for now.
     */
    public void saveAndExitProgram() { //save just students for now
        this.ioSerializable.studentsWriteToSerializable(this.studentController.getStudentManager().getAllStudent());
        System.exit(0);
    }

}
