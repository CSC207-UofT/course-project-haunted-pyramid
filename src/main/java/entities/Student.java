package entities;

import java.util.Date;
import java.util.UUID;

public class Student extends User {

    private int numberOfCourses;
    private Course[] courseEnrolled;

    public Student(String name, Date birthDay, UUID id) {
        super(name, birthDay, id);
        this.numberOfCourses = 0;
        this.courseEnrolled = new Course[numberOfCourses];
    }

    public void addCourse(Course course){
        Course[] new_content = new Course[this.numberOfCourses + 1];
        System.arraycopy(this.courseEnrolled, 0, new_content, 0, this.numberOfCourses);
        this.courseEnrolled = new_content;
        this.courseEnrolled[this.numberOfCourses] = course;
        this.numberOfCourses += 1;
    }

    public void removeCourse(Course course){
        Course[] new_content = new Course[this.numberOfCourses - 1];
        int i = 0;
        for(Course cour : this.courseEnrolled){
            if(cour != course){
                new_content[i] = cour;
                i += 1;
            }
        }
        this.courseEnrolled = new_content;
        this.numberOfCourses -= 1;
    }

    public Course[] getCourseEnrolled() {
        return courseEnrolled;
    }
}
