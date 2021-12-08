package controllers;

import entities.UserPreferences;
import gateways.IOSerializable;
import helpers.Constants;
import helpers.ControllerHelper;
import presenters.MenuStrategies.DisplayMenu;
import presenters.MenuStrategies.ProfileMenuContent;
import usecases.UserManager;
import usecases.events.worksessions.WorkSessionScheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * A controller for accessing (after login) User info and allowing a user to edit their profile
 *
 * @author Taite Cullen
 * @author Seo Won Yi
 * @author Sebin Im
 */
public class UserController {
    private final UserManager userManager;
    private UUID currentUser;
    private final ControllerHelper helper;
    private final IOController ioController;

    /**
     * Instantiates a UserController from serialized data
     * creates UserManager from serialized data (or blank if hasSavedData is false),
     * instantiates IOController and ControllerHelper
     *
     * @param hasSavedData   hasSavedData, boolean
     * @param ioSerializable ioSerializable
     */
    public UserController(boolean hasSavedData, IOSerializable ioSerializable) {
        if (hasSavedData) {
            this.userManager = new UserManager(ioSerializable.usersReadFromSerializable());
        } else {
            this.userManager = new UserManager(new ArrayList<>());
        }
        this.helper = new ControllerHelper();
        this.ioController = new IOController();
    }

    /**
     * Get the UUID of the user that is currently logged in to the program
     *
     * @return UUID of the currently logged-in user
     */
    public UUID getCurrentUser() {
        return this.currentUser;
    }

    /**
     * sets current user UUID - to access logged-in User during runtime
     *
     * @param currentUser UUID of the logged-in user that can be accessed and edited
     */
    public void setCurrentUser(UUID currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * gets the username of the current User
     *
     * @return the String username
     */
    public String getCurrentUsername() {
        return this.userManager.getName(this.currentUser);
    }

    /**
     * the UserManager created from IOSerializable <code>this.userManager</code>
     *
     * @return <code>this.userManager</code>
     */
    public UserManager getUserManager() {
        return this.userManager;
    }

    /**
     * gets the map of the start=end times of the users usual free time
     *
     * @return <code>currentUser.userPreferences.freeTime</code>
     */
    public Map<LocalTime, LocalTime> getCurrentFreeTime() {
        return this.userManager.getFreeTime(this.currentUser);
    }

    /**
     * gets the current users setting for procrastinate
     *
     * @return true if currentUser.getProcrastinate is true, otherwise false
     */
    public boolean getCurrentProcrastinate() {
        return this.userManager.getProcrastinate(this.currentUser);
    }

    /**
     * prompts the user to choose from a menu how they would like to edit their profile then runs <code>this.getAction</code>
     */
    public void editProfile() {
        boolean done = false;
        while (!done) {
            DisplayMenu dm = new DisplayMenu();
            ProfileMenuContent profileMenuContent = new ProfileMenuContent(this.currentUser, this.getUserManager());
            System.out.println("Note:");
            System.out.println("Work Sessions will only set up during Free Time");
            System.out.println("If Procrastinate is on, Work Sessions will be scheduled more towards the Deadline");
            System.out.println("if Cram is on, longer work sessions will be scheduled on days they fit as opposed to even " +
                    "day spacing");
            System.out.println("if morning person is on, your events will be scheduled as early in the day as possible");
            System.out.println("if multiple work sessions of the same event occur on the same day, they will be scheduled" +
                    "with spacing between according to 'work session spacing' (if it is null, they will be merged)");
            String firstAction = ioController.getAnswer(dm.displayMenu(profileMenuContent));
            firstAction = helper.invalidCheck(dm, firstAction, profileMenuContent.numberOfOptions(), profileMenuContent);
            if (firstAction.equalsIgnoreCase("Return")) {
                return;
            }
            done = getAction(firstAction);
        }
    }

    /**
     * passes to next method depending on input String (a number between 1 and 7)"
     *
     * @param action String, "1" - "7"
     * @return done if the user is done editing
     */
    private boolean getAction(String action) {
        boolean indicator = false;
        switch (action) {
            case "1":
                this.changeName();
                break;
            case "2":
                this.addFreeTime();
                break;
            case "3":
                this.removeFreeTime();
                break;
            case "4":
                this.toggleProcrastinate();
                break;
            case "5":
                this.toggleSessionSpacing();
                break;
            case "6":
                this.toggleCram();
                break;
            case "7":
                this.toggleMorningPerson();
            case "8":
                indicator = true;
                break;
        }
        return indicator;
    }

    /**
     * prompts the user to enter a new name, and changes the users name
     */
    private void changeName() {
        System.out.println("You may type Return to return to the menu");
        String name = ioController.getAnswer("What is your new name?");
        changeName(name);
    }

    /**
     * uses userManager to change name of currentUser to input name
     *
     * @param name new NAme
     */
    public void changeName(String name) {
        if (name.equalsIgnoreCase("Return")) {
            return;
        }
        this.userManager.setName(this.currentUser, name);
    }

    /**
     * prompts the user to enter the start time and end time of new usual free time, and adds free time
     */
    private void addFreeTime() {
        System.out.println("You may type Return to return to the menu");
        LocalTime start = ioController.getTime("Enter the start time of your regular free time");
        LocalTime end = ioController.getTime("Enter the end time of your regular free time");
        addFreeTime(start, end);
    }

    /**
     * usee userManager to add input start and end time to currentUser's UserPreferences freeTime
     *
     * @param start LocalTime start time
     * @param end   LocalTime end time
     */
    public void addFreeTime(LocalTime start, LocalTime end) {
        if (start.equals(Constants.RETURN_NOTIFIER)) {
            return;
        }
        if (end.equals(Constants.RETURN_NOTIFIER)) {
            return;
        }
        this.userManager.addFreeTime(this.currentUser, start,
                end);
    }

    /**
     * prompts the user to enter start time for free time they would like to remove - removes free time
     */
    private void removeFreeTime() {
        System.out.println("You may type Return to return to the menu");
        LocalTime start = ioController.getTime("Enter the start time of your regular free time");
        removeFreeTime(start);
    }

    /**
     * uses the userManager to access current user's userPreferences and remove free time starting at this start
     *
     * @param start LocalTime start time
     */
    public void removeFreeTime(LocalTime start) {
        if (start.equals(Constants.RETURN_NOTIFIER)) {
            return;
        }
        this.userManager.removeFreeTime(this.currentUser, start);
    }

    private void toggleProcrastinate() {
        this.userManager.toggleProcrastinate(this.currentUser);
    }

    public void setProcrastinate(boolean procrastinate) {
        this.userManager.getPreferences(currentUser).setProcrastinate(procrastinate);
    }

    private void toggleMorningPerson() {
        this.userManager.toggleMorningPerson(this.currentUser);
    }

    public void setMorningPerson(boolean morningPerson) {
        this.userManager.getPreferences(currentUser).setMorningPerson(morningPerson);
    }

    private void toggleCram() {
        this.userManager.toggleEvenSpacing(this.currentUser);
    }

    public void setCram(boolean cram) {
        this.userManager.getPreferences(currentUser).setCram(cram);
    }

    private void toggleSessionSpacing() {
        this.userManager.toggleWorkSessionSpacing(this.currentUser);
    }

    public void setSessionSpacing(String sessionSpacing) {
        this.userManager.getPreferences(currentUser).setSpacingSameDay(sessionSpacing);
    }

    public UserPreferences getPreferences() {
        return this.getUserManager().getPreferences(currentUser);
    }
}
