package controllers;

import gateways.IOSerializable;
import presenters.DisplayMenu;
import presenters.MenuStrategies.ProfileMenuContent;
import usecases.UserManager;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserController {

    private final UserManager userManager;
    private UUID currentUser;

    public UserController(boolean hasSavedData, IOSerializable ioSerializable) {
        if (hasSavedData) {
            this.userManager = new UserManager(ioSerializable.usersReadFromSerializable());
        } else {
            this.userManager = new UserManager(new ArrayList<>());
        }
    }

    public void setCurrentUser(UUID currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUsername(){
        return this.userManager.getName(this.currentUser);
    }

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
            System.out.println(dm.displayMenu(profileMenuContent));
            String next = IOController.getAnswer("");
            if (next.equalsIgnoreCase("done")){
                done = true;
            }
            this.getAction(next);
        }
    }

    private void getAction(String action){
        if (action.equalsIgnoreCase("change name")){
            this.changeName();
        } else if(action.equalsIgnoreCase("add free time")){
            this.addFreeTime();
        }else if(action.equalsIgnoreCase("remove free time")){
            this.removeFreeTime();
        }else if(action.equalsIgnoreCase("toggle procrastinate")){
           this.toggleProcrastinate();
        }
    }

    public void changeName(){
        this.userManager.getUserInfo().get(this.currentUser).setName(IOController.getAnswer("what is your new name?"));
    }
    public void addFreeTime(){
        List<Integer> start = IOController.getTime("enter the start time of your regular free time");
        List<Integer> end = IOController.getTime("enter the end time of your regular free time");
        this.userManager.addFreeTime(this.currentUser, LocalTime.of(start.get(0), start.get(1)),
                LocalTime.of(end.get(0), end.get(1)));
    }
    public void removeFreeTime(){
        List<Integer> start = IOController.getTime("enter the start time of your regular free time");
        this.userManager.removeFreeTime(this.currentUser, LocalTime.of(start.get(0), start.get(1)));
    }

    public void toggleProcrastinate(){
        this.userManager.toggleProcrastinate(this.currentUser);
    }

    public UserManager getUserManager() { return this.userManager; }

}
