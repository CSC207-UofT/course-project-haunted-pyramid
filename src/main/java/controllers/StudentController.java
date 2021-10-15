package controllers;

import entities.Student; //TODO: DELETE THIS AFTER TESTING

import gateways.IOSerializable;
import usecases.StudentManager;

import java.util.ArrayList;
import java.util.UUID;

public class StudentController {

    private IOSerializable ioSerializable;
    private StudentManager studentManager;

    public StudentController(boolean hasSavedData, IOSerializable ioSerializable) {
        this.ioSerializable = ioSerializable;
        if (hasSavedData) {
            this.studentManager = new StudentManager(this.ioSerializable.studentsReadFromSerializable());
        } else {
            ArrayList<Student> arrL = new ArrayList<>();
            Student sebin = new Student(UUID.randomUUID(), "Sebin Im", "sebinUsername", "sebinPassword");
            Student sean = new Student(UUID.randomUUID(), "Sean Yi", "seanUsername", "seanPassword");
            Student taite = new Student(UUID.randomUUID(), "Taite Cullen", "taiteUsername", "taitePassword");
            Student shameel = new Student(UUID.randomUUID(), "Shameel Farooq", "shameelUsername", "shameelPassword");
            Student teddy = new Student(UUID.randomUUID(), "Teddy Yan", "teddyUsername", "teddyPassword");
            Student malik = new Student(UUID.randomUUID(), "Malik Lahlou", "malikUsername", "malikPassword");
            arrL.add(sebin);
            arrL.add(sean);
            arrL.add(taite);
            arrL.add(shameel);
            arrL.add(teddy);
            arrL.add(malik);
            this.studentManager = new StudentManager(arrL);
            //All the above is testing, below is what should be here.
            //this.studentManager = new StudentManager(new ArrayList<>());
        }
    }

    public StudentManager getStudentManager() {
        return this.studentManager;
    }

}
