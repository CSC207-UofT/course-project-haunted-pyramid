package controllers;

import entities.Event; // Needs to be removed after implementing EventController/EventManager
import gateways.IOSerializable;
import presenters.CalendarPresenter;
import usecases.CalendarManager;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;


public class MainController {

    private CalendarManager calendarManager; //May be updated to CalendarController
    private CalendarPresenter calendarPresenter;
    private LoginController loginController;
    private StudentController studentController;

    private IOSerializable ioSerializable;
    private Scanner scanner = new Scanner(System.in);
    private int iD = 1;

    public MainController() {
        //Instantiation of the IOSerializable
        this.ioSerializable = new IOSerializable();
        this.studentController = new StudentController(this.ioSerializable.hasSavedData(), this.ioSerializable);
        this.loginController = new LoginController(this.studentController);
        this.calendarManager = new CalendarManager();
        this.calendarPresenter = new CalendarPresenter(this.calendarManager);
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
    public String displayCalendar() {
        return this.calendarPresenter.showMonthCalendar(this.calendarManager.getCurrentYear(),
                this.calendarManager.getCurrentMonth());
    }

    public void generateEvent() {
        String check = "N";
        while (check.equals("N")) {

            System.out.println("Please select the Event's name");
            String eventName = scanner.nextLine();
            System.out.println("Please choose a date");
            System.out.println("Type a number between 1 - 31 (depending on the month)");
            String chosenDate = scanner.nextLine();
            System.out.println("Print select the Event's start time");
            System.out.println("In the form of hh:mm");
            String eventStartTime = scanner.nextLine();
            System.out.println("Print select the Event's end time");
            System.out.println("In the form of hh:mm");
            String eventEndTime = scanner.nextLine();
            Event event = new Event(this.iD, eventName, this.calendarManager.getCurrentYear(),
                    this.calendarManager.getCurrentMonth(), Integer.parseInt(chosenDate),
                    Integer.parseInt(eventStartTime.substring(0, 2)), Integer.parseInt(eventEndTime.substring(0, 2)),
                    Integer.parseInt(eventStartTime.substring(3, 5)), Integer.parseInt(eventEndTime.substring(3, 5)));
            this.iD = this.iD + 1;
            this.calendarManager.addToCalendar(event);
            System.out.println("Would you like to add more event to the calendar?");
            System.out.println("Y/N");
            String answer = scanner.nextLine();
            if (answer.equals("N")){
                check = "Y";
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
