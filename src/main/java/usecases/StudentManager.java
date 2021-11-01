package usecases;

import entities.Course;
import entities.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StudentManager {

    private final HashMap<UUID, Course[]> studentMap;
    private final HashMap<UUID, Student> studentInfo;

    public StudentManager(ArrayList<Student> students){
        this.studentMap = new HashMap<>();
        this.studentInfo = new HashMap<>();
        for(Student student : students){
            this.studentMap.put(student.getId(), student.getCourseEnrolled());
            this.studentInfo.put(student.getId(), student);
        }
    }

    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(this.studentInfo.values());
    }

    public HashMap<UUID, Student> getStudentInfo() {
        return this.studentInfo;
    }

    public HashMap<UUID, Course[]> getStudentMap() {
        return this.studentMap;
    }

    // In the CRC cards, its written 'Store student information to studentInfo', but students info
    // have different types (string, int, date, list, etc) and I can't think of any good way to store the info
    // other than the Student object itself.
    // Same for the 'Create a studentMap with given information and acquired courseList from CourseManager' method.
}
