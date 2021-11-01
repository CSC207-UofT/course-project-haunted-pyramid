package usecases;

import entities.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StudentManagerTest {
    StudentManager studentManager;
    ArrayList<Student> studentList= new ArrayList<>();


    @Before
    public void setUp() {
        Student malik = new Student(UUID.randomUUID(), "Malik Lahlou", "malikUsername", "malikPassword");
        Student teddy = new Student(UUID.randomUUID(), "Teddy Yan", "teddyUsername", "teddyPassword");
        studentList.add(teddy);
        studentList.add(malik);
        studentManager = new StudentManager(studentList);
    }

    @Test
    public void testGetAllStudents() {
        assertTrue(studentManager.getAllStudents().contains(studentList.get(0)));
        assertTrue(studentManager.getAllStudents().contains(studentList.get(1)));
    }
}