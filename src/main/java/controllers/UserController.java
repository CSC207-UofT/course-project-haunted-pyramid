package controllers;

import entities.User;
import gateways.IOSerializable;
import presenters.DisplayMenu;
import presenters.MenuStrategies.ProfileMenuContent;
import usecases.UserManager;

import java.util.ArrayList;
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

    public void editProfile(){
        DisplayMenu dm = new DisplayMenu();
        ProfileMenuContent profileMenuContent = new ProfileMenuContent(this.currentUser, this.getUserManager());
        System.out.println(dm.displayMenu(profileMenuContent));
        String next = IOController.getAnswer("");
        if (next.equalsIgnoreCase("change name")){

        }
    }

    public void changeName(){
        this.userManager.getUserInfo().get(this.currentUser).setName(IOController.getAnswer("what is your new name?"));
    }

    public UserManager getUserManager() { return this.userManager; }

}
