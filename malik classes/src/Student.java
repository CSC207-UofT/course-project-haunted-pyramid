import java.util.Date;

public class Student extends User{

    private int numberOfCourses;
    private Course[] courseEnrolled;

    public Student(String name, Date birthDay) {
        super(name, birthDay);
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

}
