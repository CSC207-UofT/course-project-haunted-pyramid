package usecases;

import entities.Course;
import entities.Student;
import org.junit.Before;
import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StudentTest {
    Student student = new Student(UUID.randomUUID(), "malik", "malikl", "pass");
    Course[] courses = new Course[2];


    @Before
    public void setUp() {
        Course course1 = new Course("CSC207");
        Course course2 = new Course("CSC209");
        courses[0] = course1;
        courses[1] = course2;
    }


    @Test
    public void testAddCourse() {
        student.addCourse(courses[0]);
        student.addCourse(courses[1]);
        assertEquals(student.getNumberOfCourses(), 2);
        assertEquals(student.getCourseEnrolled(), courses);
    }

    @Test
    public void testRemoveCourse() {
        student.addCourse(courses[0]);
        student.addCourse(courses[1]);
        student.removeCourse(courses[0]);
        Course[] cour = new Course[1];
        cour[0] = courses[1];
        assertEquals(student.getNumberOfCourses(), 1);
        assertEquals(student.getCourseEnrolled(), cour);
    }
}