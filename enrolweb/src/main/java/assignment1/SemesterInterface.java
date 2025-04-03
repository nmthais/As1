package assignment1;

import java.util.ArrayList;

public interface SemesterInterface {
    void addSemester(Semester semester);
    Semester getSemester(int semesterID);
    ArrayList<Semester> getAllSemesters();
    void updateSemester(Semester semester);
    void deleteSemester(int semesterID);
}
