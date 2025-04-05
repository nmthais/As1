package assignment1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Enrollment extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("student") == null){
            response.sendRedirect("Login");
            return;
        }
        else{
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Code/Enroll.jsp");
            requestDispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(false);
        
        if(session ==null){
            System.out.println("session removed before register :D");
            response.sendRedirect("Login");
        }
        else{
            
            //initiate vars
            StudentService studentService = new StudentService();
            Student student = (Student) session.getAttribute("student");
            int pickedSemID = (Integer) session.getAttribute("semester");
            String [] coursesArray = request.getParameterValues("course[]");
            //enroll student
            ArrayList<String> courseSubmitted = new ArrayList<>(Arrays.asList(coursesArray));
            EnrollMessage enrollMessageObj = studentService.StudentEnroll(student, courseSubmitted, pickedSemID);
            //update list of completed, uncompleted
            @SuppressWarnings("unchecked")
            ArrayList<Course> unfinishedCourses = (ArrayList<Course>) session.getAttribute("unfinishedCourses");
            ArrayList<Course> finishedCourses = studentService.checkFinishedCourse(student);
            ArrayList<String> finishedCoursesStr = new ArrayList<>();
            for(Course course : finishedCourses){
                finishedCoursesStr.add(course.getCourseID() + " - " + course.getCourseName());
            }
            ArrayList<Course> updatedCourseList = studentService.updateCourses(unfinishedCourses, finishedCourses);
            
            session.setAttribute("unfinishedCourses", updatedCourseList);
            session.setAttribute("finishedCourses", finishedCoursesStr);
            session.setAttribute("eMessage", enrollMessageObj.getEnrollMessage()); 
            session.setAttribute("displayAlert", true);
            

            
            response.sendRedirect("Enrollment");
                
            
        }
    }
    
}
