package assignment1;

import java.util.ArrayList;

public interface CourseInterface {
    void addCourse(Course course);
    Course getCourse(String courseID);
    ArrayList<Course> getAllCourse();
    void udpateCourse(Course course);
    void deleteCourse(String courseID);
}
