package assignment1;

public class Course {
    private String courseID;
    private String courseName;
    private int credits;
    Course(){

    }
    Course(String courseID, String courseName, int credits){
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
    }
    public String getCourseID() {
        return courseID;
    }
    public String getCourseName() {
        return courseName;
    }
    public int getCredits() {
        return credits;
    }
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
}
