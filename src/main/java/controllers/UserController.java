package controllers;

import gateways.IOSerializable;
import helpers.ControllerHelper;
import presenters.DisplayMenu;
import presenters.MenuStrategies.ProfileMenuContent;
import usecases.UserManager;

import java.sql.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserController {

    private final UserManager userManager;
    private UUID currentUser;
    private final ControllerHelper helper;
    private final IOController ioController;

    public UserController(boolean hasSavedData, IOSerializable ioSerializable) {
        if (hasSavedData) {
            this.userManager = new UserManager(ioSerializable.usersReadFromSerializable());
        } else {
            this.userManager = new UserManager(new ArrayList<>());
        }
        this.helper = new ControllerHelper();
        this.ioController = new IOController();
    }

    public void setCurrentUser(UUID currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUsername(){
        return this.userManager.getName(this.currentUser);
    }

    public UserManager getUserManager() { return this.userManager; }

    public Map<LocalTime, LocalTime> getCurrentFreeTime(){
        return this.userManager.getFreeTime(this.currentUser);
    }

    public boolean getCurrentProcrastinate(){
        return this.userManager.getProcrastinate(this.currentUser);
    }

    public void editProfile(){
        boolean done = false;
        while (!done){
            DisplayMenu dm = new DisplayMenu();
            ProfileMenuContent profileMenuContent = new ProfileMenuContent(this.currentUser, this.getUserManager());
            System.out.println("Note: Work Sessions will only set up during Free Time");
            String firstAction = ioController.getAnswer(dm.displayMenu(profileMenuContent));
            firstAction = helper.invalidCheck(dm, firstAction, profileMenuContent.numberOfOptions(), profileMenuContent);
            if (firstAction.equalsIgnoreCase("Return")){
                return;
            }
            done = getAction(firstAction);
        }
    }

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

    private void changeName(){
        System.out.println("You may type Return to return to the menu");
        String name = ioController.getAnswer("What is your new name?");
        if (name.equalsIgnoreCase("Return")){
            return;
        }
        this.userManager.getUserInfo().get(this.currentUser).setName(name);
    }

    private void addFreeTime(){
        System.out.println("You may type Return to return to the menu");
        List<Integer> start = ioController.getTime("Enter the start time of your regular free time");
        if (start.equals(new ArrayList<>())){
            return;
        }
        List<Integer> end = ioController.getTime("Enter the end time of your regular free time");
        if (end.equals(new ArrayList<>())){
            return;
        }
        this.userManager.addFreeTime(this.currentUser, LocalTime.of(start.get(0), start.get(1)),
                LocalTime.of(end.get(0), end.get(1)));
    }

    private void removeFreeTime(){
        System.out.println("You may type Return to return to the menu");
        List<Integer> start = ioController.getTime("Enter the start time of your regular free time");
        if (start.equals(new ArrayList<>())){
            return;
        }
        this.userManager.removeFreeTime(this.currentUser, LocalTime.of(start.get(0), start.get(1)));
    }

    private void toggleProcrastinate(){
        this.userManager.toggleProcrastinate(this.currentUser);
    }
}
