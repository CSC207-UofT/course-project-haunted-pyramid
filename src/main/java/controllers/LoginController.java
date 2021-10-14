package controllers;

import controllers.StudentController;

public class LoginController {

    private boolean loggedIn;
    private StudentController studentController;

    public LoginController(StudentController studentController) {
        this.loggedIn = false;
        if (this.studentController.getStudentManager().getStudentInfo().isEmpty()) {
            this.signUp();
        } else {
            this.login();
        }
        this.studentController = studentController;
    }

    public void login() {

    }

    public void signUp() {

    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}
