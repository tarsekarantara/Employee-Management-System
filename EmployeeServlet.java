package com.ems;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession(false) != null &&
               req.getSession(false).getAttribute("role") != null;
    }

    private boolean isAdmin(HttpServletRequest req) {
        return isLoggedIn(req) &&
               "ADMIN".equals(req.getSession(false).getAttribute("role"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            if (!isAdmin(req)) { resp.sendError(403, "Admin access required."); return; }
            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE id=?")) {
                ps.setInt(1, Integer.parseInt(req.getParameter("id")));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Employee e = new Employee(rs.getInt("id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("department"),
                            rs.getString("job_role"), rs.getBigDecimal("salary"));
                        req.setAttribute("employee", e);
                    }
                }
            } catch (Exception e) {
                throw new ServletException("Could not load employee.", e);
            }
            req.getRequestDispatcher("/employee-form.jsp").forward(req, resp);
            return;
        }

        List<Employee> employees = new ArrayList<>();
        String search = req.getParameter("search");
        String sql = "SELECT * FROM employees WHERE name LIKE ? OR department LIKE ? ORDER BY id DESC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + (search == null ? "" : search.trim()) + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("department"),
                        rs.getString("job_role"), rs.getBigDecimal("salary")));
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Could not retrieve employees. Check MySQL settings.", e);
        }
        req.setAttribute("employees", employees);
        req.getRequestDispatcher("/employees.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!isLoggedIn(req)) { resp.sendRedirect("login.jsp"); return; }

        String action = req.getParameter("action");
        if (!isAdmin(req)) { resp.sendError(403, "Only ADMIN can change employee records."); return; }

        try (Connection con = DB.getConnection()) {
            if ("delete".equals(action)) {
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM employees WHERE id=?")) {
                    ps.setInt(1, Integer.parseInt(req.getParameter("id")));
                    ps.executeUpdate();
                }
            } else if ("save".equals(action)) {
                String name = required(req, "name");
                String email = required(req, "email");
                String department = required(req, "department");
                String jobRole = required(req, "jobRole");
                BigDecimal salary = new BigDecimal(required(req, "salary"));
                if (salary.signum() < 0) throw new IllegalArgumentException("Salary cannot be negative.");

                String idText = req.getParameter("id");
                if (idText == null || idText.isBlank()) {
                    String sql = "INSERT INTO employees(name,email,department,job_role,salary) VALUES(?,?,?,?,?)";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        bind(ps, name, email, department, jobRole, salary);
                        ps.executeUpdate();
                    }
                } else {
                    String sql = "UPDATE employees SET name=?,email=?,department=?,job_role=?,salary=? WHERE id=?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        bind(ps, name, email, department, jobRole, salary);
                        ps.setInt(6, Integer.parseInt(idText));
                        ps.executeUpdate();
                    }
                }
            }
            resp.sendRedirect("employees");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Invalid form data: " + e.getMessage());
            req.getRequestDispatcher("/employee-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                req.setAttribute("error", "That email address already exists.");
                req.getRequestDispatcher("/employee-form.jsp").forward(req, resp);
            } else {
                throw new ServletException("Database operation failed.", e);
            }
        }
    }

    private String required(HttpServletRequest req, String key) {
        String value = req.getParameter(key);
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(key + " is required");
        return value.trim();
    }

    private void bind(PreparedStatement ps, String name, String email, String department,
                      String jobRole, BigDecimal salary) throws SQLException {
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, department);
        ps.setString(4, jobRole);
        ps.setBigDecimal(5, salary);
    }
}
