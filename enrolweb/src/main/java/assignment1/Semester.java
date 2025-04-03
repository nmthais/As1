package assignment1;

import java.io.Serializable;

public class Semester implements Serializable{
    private int semesterID;
    private int semester;
    private int year;
    private int openForEnroll;
    Semester(){

    }

    Semester(int semesterID, int semester, int year, int openForEnroll){
        this.semesterID=semesterID;
        this.semester=semester;
        this.year=year;
        this.openForEnroll=openForEnroll;
    }
    public int getOpenForEnroll() {
        return openForEnroll;
    }
    public int getSemester() {
        return semester;
    }
    public int getSemesterID() {
        return semesterID;
    }
    public int getYear() {
        return year;
    }
    public void setOpenForEnroll(int openForEnroll) {
        this.openForEnroll = openForEnroll;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }
    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
