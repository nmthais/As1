package assignment1;

import java.util.ArrayList;

public interface StudentInterface {
    void addStudent(Student student);
    Student getStudentByStdNo(String stdNo);
    ArrayList<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(String stdNo);
    void hashPass(Student student);
}
