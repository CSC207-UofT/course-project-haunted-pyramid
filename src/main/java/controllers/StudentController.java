package controllers;

// Bottom two are for type checking only, thus not violating clean architecture
import entities.Student;
import entities.User;

import usecases.StudentManager;

import java.util.ArrayList;

public class StudentController {

    private final StudentManager studentManager;

    public StudentController(UserController userController) {
        if (!userController.getUserManager().getUserInfo().isEmpty()) {
            ArrayList<Student> arrayList = new ArrayList<>();
            for (User user : userController.getUserManager().getUserInfo().values()) {
                if (user instanceof Student) {
                    arrayList.add((Student) user);
                }
            }
            this.studentManager = new StudentManager(arrayList);
        } else {
            this.studentManager = new StudentManager(new ArrayList<>());
        }
    }

    public StudentManager getStudentManager() { return this.studentManager; }

}
