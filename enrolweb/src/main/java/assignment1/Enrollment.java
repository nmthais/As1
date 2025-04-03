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
            session.setAttribute("enroll", null);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Code/Enroll.jsp");
            requestDispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //handle enrollment     
        //also handle log out invalidate each session instead of alltogether
        HttpSession session = request.getSession(false);
        
        if(session ==null){
            System.out.println("session removed before register :D");
            response.sendRedirect("Login");
        }
        else{
            StudentService studentService = new StudentService();
            Student student = (Student) session.getAttribute("student");
            int pickedSem = (Integer) session.getAttribute("semester");
            String [] coursesArray = request.getParameterValues("course[]");
            ArrayList<String> courseSubmitted = new ArrayList<>(Arrays.asList(coursesArray));
            EnrollMessage enrollMessageObj = studentService.StudentEnroll(student, courseSubmitted, pickedSem);
            boolean isEnroll = enrollMessageObj.getisEnroll();
            
            if(isEnroll)
            {
                request.setAttribute("eMessage", enrollMessageObj.getEnrollMessage());
                request.setAttribute("enroll", isEnroll);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Code/Enroll.jsp");
                requestDispatcher.forward(request, response);
                
            }
            else{
                request.setAttribute("eMessage", enrollMessageObj.getEnrollMessage());
                System.out.println(enrollMessageObj.getEnrollMessage());
                request.setAttribute("enroll", isEnroll);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Code/Enroll.jsp");
                requestDispatcher.forward(request, response);
            }
        }
    }
    
}
