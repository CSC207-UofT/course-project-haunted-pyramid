package usecases;

import entities.Course;
import entities.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentManager {

    private HashMap<Student, Course[]> studentMap;
    private HashMap<Integer, Student> studentInfo;

    public StudentManager(ArrayList<Student> students){
        HashMap<Student, Course[]> studentToCourses = new HashMap<>();
        HashMap<Integer, Student> idToStudents = new HashMap<>();
        for(Student student : students){
            studentToCourses.put(student, student.getCourseEnrolled());
            idToStudents.put(student.getId(), student);
        }
    }

    // In the CRC cards, its written 'Store student information to studentInfo', but students info
    // have different types (string, int, date, list, etc) and I can't think of any good way to store the info
    // other than the Student object itself.
    // Same for the 'Create a studentMap with given information and acquired courseList from CourseManager' method.
}
