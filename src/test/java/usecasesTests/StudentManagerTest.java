package usecasesTests;

import entities.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import usecases.CalendarManager;
import usecases.StudentManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class StudentManagerTest {
    StudentManager studentManager;

    @Before
    public void setUp() {
        Student malik = new Student(UUID.randomUUID(), "Malik Lahlou", "malikUsername", "malikPassword");
        Student teddy = new Student(UUID.randomUUID(), "Teddy Yan", "teddyUsername", "teddyPassword");
        ArrayList<Student> arrL = new ArrayList<>();
        arrL.add(teddy);
        arrL.add(malik);
        studentManager = new StudentManager(arrL);
    }


    @Test
    public void testAddNewStudent() {
        UUID x = UUID.randomUUID();
        studentManager.addNewStudent(x, "Sebin Im", "sebinUsername", "sebinPassword");
        Student sebin = new Student(x, "Sebin Im", "sebinUsername", "sebinPassword");
        assertTrue(studentManager.getStudentInfo().containsKey(x));
        assertEquals(studentManager.getStudentInfo().get(x), sebin);
    }

    @Test(timeout = 100)
    public void testGetAllStudents() {
        Student malik = new Student(UUID.randomUUID(), "Malik Lahlou", "malikUsername", "malikPassword");
        Student teddy = new Student(UUID.randomUUID(), "Teddy Yan", "teddyUsername", "teddyPassword");
        ArrayList<Student> arrL = new ArrayList<>();
        arrL.add(teddy);
        arrL.add(malik);
        assertEquals(studentManager.getAllStudents(), arrL);
    }

    @Test(timeout = 100)
    public void testCheckUsernameAndPassword() {
        assertTrue(studentManager.checkUsernameAndPassword("malikUsername", "malikPassword"));
    }
}