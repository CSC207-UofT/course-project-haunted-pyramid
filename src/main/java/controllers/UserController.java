package controllers;

import entities.User;
import gateways.IOSerializable;
import helpers.Constants;
import helpers.ControllerHelper;
import presenters.DisplayMenu;
import presenters.MenuStrategies.ProfileMenuContent;
import usecases.UserManager;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A controller for accessing (after login) User info and allowing a user to edit their profile
 * @author Taite Cullen
 * @author Sean Yi
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
     * @param hasSavedData hasSavedData, boolean
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
     * sets current user UUID - to access logged-in User during runtime
     * @param currentUser UUID of the logged-in user that can be accessed and edited
     */
    public void setCurrentUser(UUID currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * gets the username of the current User
     * @return the String username
     */
    public String getCurrentUsername(){
        return this.userManager.getName(this.currentUser);
    }

    /**
     * the UserManager created from IOSerializable <code>this.userManager</code>
     * @return <code>this.userManager</code>
     */
    public UserManager getUserManager() { return this.userManager; }

    /**
     * gets the map of the start=end times of the users usual free time
     * @see User#getFreeTime()
     * @return <code>currentUser.freeTime</code>
     */
    public Map<LocalTime, LocalTime> getCurrentFreeTime(){
        return this.userManager.getFreeTime(this.currentUser);
    }

    /**
     * gets the current users setting for procrastinate
     * @return true if currentUser.getProcrastinate is true, otherwise false
     */
    public boolean getCurrentProcrastinate(){
        return this.userManager.getProcrastinate(this.currentUser);
    }

    /**
     * prompts the user to choose from a menu how they would like to edit their profile then runs <code>this.getAction</code>
     */
    public void editProfile(){
        boolean done = false;
        while (!done){
            DisplayMenu dm = new DisplayMenu();
            ProfileMenuContent profileMenuContent = new ProfileMenuContent(this.currentUser, this.getUserManager());
            System.out.println("Note:");
            System.out.println("Work Sessions will only set up during Free Time");
            System.out.println("If Procrastinate is on, Work Sessions will be scheduled more towards the Deadline");
            String firstAction = ioController.getAnswer(dm.displayMenu(profileMenuContent));
            firstAction = helper.invalidCheck(dm, firstAction, profileMenuContent.numberOfOptions(), profileMenuContent);
            if (firstAction.equalsIgnoreCase("Return")){
                return;
            }
            done = getAction(firstAction);
        }
    }

    /**
     * passes to next method depending on input String (a number between 1 and 5)"
     * @param action String, "1" - "5"
     * @return done if the user is done editing
     */
    private boolean getAction(String action){
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
                indicator = true;
                break;
        }
        return indicator;
    }

    /**
     * prompts the user to enter a new name, and changes the users name
     */
    private void changeName(){
        System.out.println("You may type Return to return to the menu");
        String name = ioController.getAnswer("What is your new name?");
        if (name.equalsIgnoreCase("Return")){
            return;
        }
        this.userManager.getUserInfo().get(this.currentUser).setName(name);
    }

    /**
     * prompts the user to enter the start time and end time of new usual free time, and adds free time
     */
    private void addFreeTime(){
        System.out.println("You may type Return to return to the menu");
        LocalTime start = ioController.getTime("Enter the start time of your regular free time");
        if (start.equals(Constants.RETURN_NOTIFIER)) {
            return;
        }
        LocalTime end = ioController.getTime("Enter the end time of your regular free time");
        if (end.equals(Constants.RETURN_NOTIFIER)) {
            return;
        }
        this.userManager.addFreeTime(this.currentUser, start,
                end);
    }

    /**
     * prompts the user to enter start time for free time they would like to remove - removes free time
     */
    private void removeFreeTime(){
        System.out.println("You may type Return to return to the menu");
        LocalTime start = ioController.getTime("Enter the start time of your regular free time");
        if (start.equals(Constants.RETURN_NOTIFIER)){
            return;
        }
        this.userManager.removeFreeTime(this.currentUser, start);
    }

    /**
     * changes the current user's value for procrastinate (false -> true, true -> false)
     */
    private void toggleProcrastinate(){
        this.userManager.toggleProcrastinate(this.currentUser);
    }
}
