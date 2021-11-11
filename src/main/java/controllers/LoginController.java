package controllers;

import java.util.Scanner;
import java.util.UUID;

public class LoginController {

    private boolean loggedIn;

    private final UserController userController;
    private final StudentController studentController;

    private final Scanner scanner = new Scanner(System.in);

    public LoginController(UserController userController, StudentController studentController) {
        this.userController = userController;
        this.studentController = studentController;
        this.loggedIn = false;
    }

    /**
     * Login to verify account to access program.
     * Temporary for now since the presenter isn't fully implemented.
     */
    public void login() {
        System.out.println("Type your username.");
        String username = scanner.nextLine();
        System.out.println("Type your password.");
        String password = scanner.nextLine();
        if (this.userController.getUserManager().checkUsernameAndPassword(username, password) != null) {
            this.loggedIn = true;
            System.out.println("Login success!");
            this.userController.setCurrentUser(this.userController.getUserManager().checkUsernameAndPassword(username,
                    password).getId());
        } else {
            System.out.println("Login failed. Check for possible spelling mistakes.");
        }
    }

    public void logout(){
        this.loggedIn = false;
    }

    /**
     * Sign up a new account for the login verification.
     * Temporary for now since the presenter isn't fully implemented.
     */
    public void signUp() {
        System.out.println("Type your full name.");
        String name = scanner.nextLine();
        System.out.println("Type your desired username.");
        String username = scanner.nextLine();
        System.out.println("Type your desired password.");
        String password = scanner.nextLine();
        this.userController.getUserManager().addNewUser(UUID.randomUUID(), name, username, password);
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}
