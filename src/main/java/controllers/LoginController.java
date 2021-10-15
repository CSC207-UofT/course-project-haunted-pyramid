package controllers;

import controllers.StudentController;

import java.util.Scanner;
import java.util.UUID;

public class LoginController {

    private boolean loggedIn;

    private StudentController studentController;

    private Scanner scanner = new Scanner(System.in);

    public LoginController(StudentController studentController) {
        this.loggedIn = false;
        this.studentController = studentController;
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
        if (this.studentController.getStudentManager().checkUsernameAndPassword(username, password)) {
            this.loggedIn = true;
            System.out.println("Login success!");
        } else {
            System.out.println("Login failed. Check for possible spelling mistakes.");
        }
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
        this.studentController.getStudentManager().addNewStudent(UUID.randomUUID(), name, username, password);
        System.out.println("Your new account was created.");
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}
