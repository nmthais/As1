package assignment1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginVerification extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentService service = new StudentService();
        if(service.checkLogin(request, response) !=null){   //check if username and password is correct, checkLogin returns a student obj
            Student student = service.checkLogin(request, response);
            SemesterImplementation semImp = new SemesterImplementation();
            HttpSession session = request.getSession(false);
            
            session = request.getSession();
            session.setMaxInactiveInterval(1800);
            ArrayList<Semester> semList = semImp.getAllSemesters();

            session.setAttribute("student", student);
            session.setAttribute("semesters", semList);
            response.sendRedirect(request.getContextPath() + "/ChoosingSem");
                      
        }
        else{
            request.setAttribute("invalidMessage", "Error: username or password is incorrect, please try again");
            sendToMain(request, response);
        }
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendToMain(request, response);
    }

    protected void sendToMain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        File main = new File("index.jsp");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(main.toString());
        requestDispatcher.forward(request, response);
    }

}
