package controllers;

import controllers.StudentController;

import java.util.Scanner;

public class LoginController {

    private boolean loggedIn;

    private StudentController studentController;

    private Scanner scanner = new Scanner(System.in);

    public LoginController(StudentController studentController) {
        this.loggedIn = false;
        this.studentController = studentController;
    }

    public void login() {

    }

    /**
     * Sign up a new account for the login verification.
     * Temporary for now since the presenter isn't fully implemented.
     * Username is the name and password is the birthday. Will extend soon.
     */
    public void signUp() {
        System.out.println("Type your full name. This will be your username for now.");
        String name = scanner.nextLine();
        System.out.println("Type your birthday");
        this.studentController.getStudentManager().addNewStudent(name, null, null); //TODO
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}
