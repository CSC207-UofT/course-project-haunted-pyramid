package controllers;

import entities.Student;
import gateways.IOSerializable;
import usecases.StudentManager;

import java.util.ArrayList;

public class StudentController {

    private IOSerializable ioSerializable;
    private StudentManager studentManager;

    public StudentController(boolean hasSavedData) {
        if (hasSavedData) {
            this.studentManager = new StudentManager(this.ioSerializable.studentsReadFromSerializable());
        } else {
            this.studentManager = new StudentManager(new ArrayList<>());
        }
    }

    public StudentManager getStudentManager() {
        return this.studentManager;
    }

}
