package controllers;

// Just to type cast, never uses any variables or methods in entities.User
import entities.Event;
import entities.User;

import gateways.IOSerializable;

import helpers.ControllerHelper;
import presenters.DisplayMenu;
import presenters.MenuStrategies.BasicMenuContent;
import usecases.events.EventManager;
import usecases.UserManager;
import usecases.WorkSessionScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * The main controller for the program, which initializes other controllers and their respective managers.
 * Takes user input and utilizes them as the program sees fit.
 *
 * @author Sebin Im
 * @author Sean Yi
 * @see main.ProgramRun
 * @see IOController
 * @see LoginController
 */
public class MainController {

    private final UserController userController;
    private final CalendarController calendarController;
    private EventController eventController;
    private final LoginController loginController;

    private final IOSerializable ioSerializable;
    private final Scanner scanner = new Scanner(System.in);
    private final ControllerHelper helper = new ControllerHelper();

    private final DisplayMenu displayMenu;

    /**
     * Initialize the entire program as this class is being initialized.
     * First, initialize and run IOSerializable, to retrieve serialized data from the dropbox repository.
     * Then asks the user to log in, which then will provide the user with the rest of the program.
     */
    public MainController() {
        this.ioSerializable = new IOSerializable(true);
        this.userController = new UserController(this.ioSerializable.hasSavedData(), this.ioSerializable);
        this.loginController = new LoginController(this.userController);
        this.calendarController = new CalendarController();
        this.displayMenu = new DisplayMenu();
        this.eventController = new EventController(this.ioSerializable.hasSavedData(), this.ioSerializable);
        this.displayInitScreen();
        System.out.println("WELCOME " + this.userController.getCurrentUsername() + "!");
        this.displayScreen();
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
     * User will be directed to different controllers depending on what they want to achieve
     */
    public void displayScreen() {
        this.eventController = new EventController(this.ioSerializable.hasSavedData(), this.ioSerializable,
                new WorkSessionController(new WorkSessionScheduler(this.userController.getCurrentFreeTime(),
                        this.userController.getCurrentProcrastinate())));
        while (this.loginController.isLoggedIn()) {
            System.out.println(this.calendarController.showDefaultCalendar(this.eventController));
            System.out.println("Please choose your action");
            BasicMenuContent basicMenu = new BasicMenuContent();
            System.out.println(displayMenu.displayMenu(basicMenu)); //display the basic menu
            String firstChoice = scanner.nextLine();
            firstChoice = helper.invalidCheck(displayMenu, firstChoice, basicMenu.numberOfOptions(), basicMenu);
            switch (firstChoice) {
                case "1":
                    this.userController.editProfile();
                    break;
                case "2":
                    this.calendarController.showCalendar(this.eventController);
                    break;
                case "3":
                    this.eventController.createDefaultEvent();
                    break;
                case "4":
                    this.calendarController.dailyCalendarForModification(this.eventController);
                    break;
                case "5":
                    this.calendarController.monthlyCalendarForRepetition(this.eventController);
                    break;
                case "6":
                    this.loginController.logout();
                    this.displayInitScreen();
                    break;
                case "7":
                    this.saveAndExitProgram();
                    break;
            }
        }
    }

    /**
     * Used to make sure the User files that are being imported into the dropbox is up-to-date with any new data that
     * were added whilst the program was running.
     *
     * @return ArrayList of the union set of all users in the dropbox repository and this program
     */
    public ArrayList<User> combineTwoUserFileContents(UserManager um1, UserManager um2) {
        ArrayList<User> arrayListUM1 = new ArrayList<>(um1.getAllUsers());
        ArrayList<User> arrayListUM2 = new ArrayList<>(um2.getAllUsers());
        Set<User> set = new HashSet<>();
        set.addAll(arrayListUM1);
        set.addAll(arrayListUM2);
        return new ArrayList<>(set);
    }

    /**
     * Used to make sure the Event files that are being imported into the dropbox is up-to-date with any new data that
     * were added whilst the program was running.
     *
     * @return ArrayList of the union set of all events in the dropbox repository and this program
     */
    public ArrayList<Event> combineTwoEventFileContents(EventManager em1, EventManager em2) {
        ArrayList<Event> arrayListEM1 = new ArrayList<>(em1.getAllEventsFlatSplit());
        ArrayList<Event> arrayListEM2 = new ArrayList<>(em2.getAllEventsFlatSplit());
        Set<Event> set = new HashSet<>();
        set.addAll(arrayListEM1);
        set.addAll(arrayListEM2);
        return new ArrayList<>(set);
    }

    /**
     * Save and exit the program. Only save students into students.ser for now.
     */
    public void saveAndExitProgram() {
        // Variables below are only for the final serialization process
        IOSerializable tempIoSerializable = new IOSerializable(false);
        UserController tempUserController = new UserController(true, tempIoSerializable);
        tempIoSerializable.eventsWriteToSerializable(new ArrayList<>(this.eventController.getEventManager().
                getAllEvents()));
        tempIoSerializable.usersWriteToSerializable(combineTwoUserFileContents(this.userController.getUserManager(),
                tempUserController.getUserManager()));
        tempIoSerializable.saveToDropbox();
        this.ioSerializable.deleteOldFiles();
        this.ioSerializable.deleteNewFiles();
        System.exit(0);
    }
}
