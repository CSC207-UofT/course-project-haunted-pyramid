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
    public void testAddNewStudent() {
        UUID x = UUID.randomUUID();
        studentManager.addNewStudent(x, "Sebin Im", "sebinUsername", "sebinPassword");
        Student sebin = new Student(x, "Sebin Im", "sebinUsername", "sebinPassword");
        assertTrue(studentManager.getStudentInfo().containsKey(x));
        assertEquals(studentManager.getStudentInfo().get(x).getName(), sebin.getName());
        assertEquals(studentManager.getStudentInfo().get(x).getUsername(), sebin.getUsername());
        assertEquals(studentManager.getStudentInfo().get(x).getPassword(), sebin.getPassword());
    }

    @Test
    public void testGetAllStudents() {
        assertEquals(studentManager.getAllStudents(), studentList);
    }

    @Test(timeout = 100)
    public void testCheckUsernameAndPassword() {
        assertTrue(studentManager.checkUsernameAndPassword("malikUsername", "malikPassword"));
    }
}