package assignment1;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChoosingSem extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        //check session then

        if (session == null || session.getAttribute("student") == null) {
            // Redirect if session doesn't exist or user is already logged out
            response.sendRedirect("/Login");
            return;
        }
        else{
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Code/ChoosingSem.jsp");
            requestDispatcher.forward(request, response);
        }
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(false);
        //check session then
        if (session == null || session.getAttribute("student") == null) {
            // Redirect if session doesn't exist or user is already logged out
            response.sendRedirect("/Login");
            return;
        }
        else{
            //Also check the course student enrolled to not display
            StudentService studentService = new StudentService();
            String stringPickedSem = request.getParameter("semester");
            int pickedSemID = Integer.parseInt(stringPickedSem.split(" ")[0]);
            Student student = (Student) session.getAttribute("student");
            ArrayList<Course> finishedCourses = studentService.checkFinishedCourse(student);
            ArrayList<String> finishedCoursesStr = new ArrayList<>();
            for(Course course : finishedCourses){
                finishedCoursesStr.add(course.getCourseID() + " - " + course.getCourseName());
            }
            ArrayList<Course> unfinishedCourseList = studentService.updateCourses(studentService.checkOfferedCourse(pickedSemID), finishedCourses);
            
            session.setAttribute("finishedCourses", finishedCoursesStr);
            session.setAttribute("semName", stringPickedSem);
            session.setAttribute("student", session.getAttribute("student"));
            session.setAttribute("semester", pickedSemID);
            session.setAttribute("unfinishedCourses", unfinishedCourseList);

            response.sendRedirect("Enrollment");
        }

    }
}
