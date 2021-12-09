package controllers;

import java.util.Scanner;
import java.util.UUID;

/**
 * A controller that must be used at every boot, to verify the user logging in.
 * It should contact MainController and MainController only.
 *
 * @author Sebin Im
 */

public class LoginController {

    private boolean loggedIn;

    private final UserController userController;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for this class.
     *
     * @param userController getting all users to verify that user is in the repository.
     */
    public LoginController(UserController userController) {
        this.userController = userController;
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
        checkUser(username, password);
    }

    /**
     * Login to verify account to access program.
     * Temporary for now since the presenter isn't fully implemented.
     */
    public void login(String username, String password) {
        checkUser(username, password);
    }

    /**
     * Check if the user is able to log in successfully.
     *
     * @param username username of the login procedure
     * @param password password of the login procedure
     */
    private void checkUser(String username, String password) {
        if (this.userController.getUserManager().checkUsernameAndPassword(username, password) != null) {
            this.loggedIn = true;
            System.out.println("Login success!");
            this.userController.setCurrentUser(this.userController.getUserManager().checkUsernameAndPassword(username,
                    password).getId());
        } else {
            System.out.println("Login failed. Check for possible spelling mistakes.");
        }
    }

    /**
     * Log out of the program.
     */
    public void logout() {
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

    /**
     * Sign up a new account for the login verification.
     * Temporary for now since the presenter isn't fully implemented.
     */
    public void signUp(String name, String username, String password) {
        this.userController.getUserManager().addNewUser(UUID.randomUUID(), name, username, password);
    }

    /**
     * Check if this account is logged in.
     *
     * @return boolean variable on whether they are logged in or not
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}
