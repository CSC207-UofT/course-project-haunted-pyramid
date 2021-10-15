package usecases;

import entities.Course;
import entities.Event;
import entities.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseManager {

    private ArrayList<Course> courseList;

    public CourseManager(ArrayList<Course> courses){
        this.courseList.addAll(courses);
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public HashMap<String, ArrayList<Event>> getCourseInformation(Course course){
        for(Course cour : this.courseList){
            if(cour.equals(course)){
                return cour.getTaskList();
            }
        }
        return new HashMap<>();
    }

    public boolean addCourse(Course course){
        this.courseList.add(course);
        return true;
    }

    public boolean removeCourse(Course course){
        this.courseList.remove(course);
        return true;
    }

    public boolean createCourseList(Student student){
        for(Course course : this.courseList){
            student.addCourse(course);
        }
        return true;
    }



}
