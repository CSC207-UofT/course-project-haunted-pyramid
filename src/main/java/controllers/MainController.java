package controllers;

// Just to type cast, never uses any variables or methods in entities.User
import entities.Event;
import entities.User;

import gateways.IOSerializable;
import usecases.EventManager;
import usecases.UserManager;

import java.util.ArrayList;
import java.util.Scanner;


public class MainController {

    private final UserController userController;
    private final StudentController studentController;
    private final CalendarController calendarController;
    private final EventController eventController;
    private final LoginController loginController;

    private final IOSerializable ioSerializable;
    private final Scanner scanner = new Scanner(System.in);

    public MainController() {
        this.ioSerializable = new IOSerializable();
        this.userController = new UserController(this.ioSerializable.hasSavedData(), this.ioSerializable);
        this.studentController = new StudentController(this.userController);
        this.loginController = new LoginController(this.userController, this.studentController);
        this.displayInitScreen();
        this.calendarController = new CalendarController();
        this.eventController = new EventController(this.ioSerializable.hasSavedData(), this.ioSerializable,
                this.calendarController.getCalendarManager());
        displayScreen();
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
            this.eventController.displayEvents();
            System.out.println("Type 'add' to add an event to the calendar");
            System.out.println("Type 'Exit' to exit this program.");
            System.out.println("Type the number before an Event to edit that Event");
            String SUorLI = scanner.nextLine();
            if (SUorLI.equalsIgnoreCase("add")) {
                this.eventController.schedule();
            } else if (SUorLI.equalsIgnoreCase("Log out")) {
                this.loginController.logout();
                this.displayInitScreen();
            } else if (SUorLI.equalsIgnoreCase("Exit")) {
                this.saveAndExitProgram();
            } else{
                this.eventController.edit(Integer.parseInt(SUorLI));
            }
        }
    }

    /**
     * Used to make sure the User files that are being imported into the dropbox is up-to-date with any new data that
     * were added whilst the program was running.
     */
    public ArrayList<User> combineTwoUserFileContents(UserManager um1, UserManager um2) {
        ArrayList<User> arrayListUM1 = new ArrayList<>(um1.getAllUsers());
        ArrayList<User> arrayListUM2 = new ArrayList<>(um2.getAllUsers());
        arrayListUM1.removeAll(arrayListUM2);
        arrayListUM1.addAll(arrayListUM2);
        return arrayListUM1;
    }

    /**
     * Used to make sure the Event files that are being imported into the dropbox is up-to-date with any new data that
     * were added whilst the program was running.
     */
    public ArrayList<Event> combineTwoEventFileContents(EventManager em1, EventManager em2) {
        ArrayList<Event> arrayListEM1 = new ArrayList<>(em1.getAllEvents());
        ArrayList<Event> arrayListEM2 = new ArrayList<>(em2.getAllEvents());
        arrayListEM1.removeAll(arrayListEM2);
        arrayListEM1.addAll(arrayListEM2);
        return arrayListEM1;
    }

    /**
     * Save and exit the program. Only save students into students.ser for now.
     */
    public void saveAndExitProgram() {
        this.ioSerializable.eventsWriteToSerializable(this.eventController.getEventManager().getAllEvents());
        this.ioSerializable.usersWriteToSerializable(this.userController.getUserManager().getAllUsers());
        this.ioSerializable.saveToDropbox();
        System.exit(0);
    }

}
