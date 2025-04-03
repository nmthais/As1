package assignment1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class Logout extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            // Redirect if session doesn't exist or user is already logged out
            response.sendRedirect("/Login");
            return;
        }

        // Invalidate the session
        session.invalidate();
        System.out.println("Session removed");

        // Redirect to login page
        response.sendRedirect("/Login");
    }
}
