package controllers;

import gateways.IOSerializable;
import usecases.UserManager;

import java.util.ArrayList;

public class UserController {

    private final UserManager userManager;

    public UserController(boolean hasSavedData, IOSerializable ioSerializable) {
        if (hasSavedData) {
            this.userManager = new UserManager(ioSerializable.usersReadFromSerializable());
        } else {
            this.userManager = new UserManager(new ArrayList<>());
        }
    }

    public UserManager getUserManager() { return this.userManager; }
}
