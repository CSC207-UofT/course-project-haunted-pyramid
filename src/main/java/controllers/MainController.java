package controllers;

import gateways.IOSerializable;
import usecases.CalendarManager;
import usecases.EventManager;

import java.util.Scanner;


public class MainController {

    private final EventController eventController;
    private final LoginController loginController;
    private final StudentController studentController;

    private final IOSerializable ioSerializable;
    private final Scanner scanner = new Scanner(System.in);

    public MainController() {
        //Instantiation of the IOSerializable
        this.ioSerializable = new IOSerializable();
        //May be updated to CalendarController
        CalendarManager calendarManager = new CalendarManager();
        this.studentController = new StudentController(this.ioSerializable.hasSavedData(), this.ioSerializable);
        this.loginController = new LoginController(this.studentController);
        this.displayInitScreen();
        EventManager eventManager = new EventManager();
        this.eventController = new EventController(eventManager, calendarManager);
        //TODO after phase 0, EventManager and CalendarManager specific to student - saved data - move to controllers

        // this.calendarPresenter.testPresenter();
        // this.saveAndExitProgram();
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
        while (this.loginController.isLoggedIn()) {
            System.out.println("Type 'add' to add an event to the calendar");
            System.out.println("Type 'Exit' to exit this program.");
            String SUorLI = scanner.nextLine();
            if (SUorLI.equalsIgnoreCase("add")) {
                this.eventController.schedule();
            } else if (SUorLI.equalsIgnoreCase("Log out")) {
                this.loginController.logout();
                this.displayInitScreen();
            } else if (SUorLI.equalsIgnoreCase("Exit")) {
                this.saveAndExitProgram();
            } else {
                System.out.println("Invalid input! Try again.");
            }
        }
    }

    /**
     * Save and exit the program. Only save students into students.ser for now.
     */
    public void saveAndExitProgram() { //save just students for now
        this.ioSerializable.studentsWriteToSerializable(this.studentController.getStudentManager().getAllStudents());
        System.exit(0);
    }

}
