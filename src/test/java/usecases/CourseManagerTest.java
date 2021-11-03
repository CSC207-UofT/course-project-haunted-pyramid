package usecases;

import entities.Course;
import entities.Event;
import entities.Student;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CourseManagerTest {

    CourseManager courseManager;
    Event event;


    @Before
    public void setUp() {
        ArrayList<Course> courseList = new ArrayList<>();
        Course course1 = new Course("CSC207");
        Course course2 = new Course("CSC209");
        event = new Event(1, "1", LocalDateTime.of(2021, 10, 15, 0, 0,
                0), LocalDateTime.of(2021, 10, 15, 3, 0, 0), recurring);
        course1.addEvent(event);
        courseList.add(course1);
        courseList.add(course2);
        courseManager = new CourseManager(courseList);
    }


    @Test
    public void testGetCourseInformation() {
        Course course1 = courseManager.getCourseList().get(0);
        Course course2 = courseManager.getCourseList().get(1);
        assertEquals(courseManager.getCourseInformation(course1).get("Others").get(0), event);
        assertEquals(courseManager.getCourseInformation(course2).keySet().size(), 6);
    }

    @Test
    public void testCreatCourseList() {
        Student student = new Student(UUID.randomUUID(), "malik", "malikl", "pass");
        courseManager.createCourseList(student);
        Course course1 = new Course("CSC207");
        Course course2 = new Course("CSC209");
        assertEquals(student.getCourseEnrolled()[0].getName(), course1.getName());
        assertEquals(student.getCourseEnrolled()[1].getName(), course2.getName());
    }



}




