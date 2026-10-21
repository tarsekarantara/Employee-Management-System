package com.ems;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if ("admin".equals(username) && "Admin@123".equals(password)) {
            req.getSession(true).setAttribute("username", "admin");
            req.getSession().setAttribute("role", "ADMIN");
            resp.sendRedirect("employees");
        } else if ("user".equals(username) && "User@123".equals(password)) {
            req.getSession(true).setAttribute("username", "user");
            req.getSession().setAttribute("role", "USER");
            resp.sendRedirect("employees");
        } else {
            req.setAttribute("error", "Invalid username or password.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
