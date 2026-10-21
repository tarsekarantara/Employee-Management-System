<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ems.Employee" %>
<%
    if (session.getAttribute("role") == null) { response.sendRedirect("login.jsp"); return; }
    if (!"ADMIN".equals(session.getAttribute("role"))) { response.sendError(403, "Admin access required."); return; }
    Employee e = (Employee) request.getAttribute("employee");
    boolean editing = e != null;
%>
<!DOCTYPE html><html><head><title><%= editing ? "Edit" : "Add" %> Employee</title><link rel="stylesheet" href="style.css"></head>
<body><main class="card">
<h1><%= editing ? "Edit Employee" : "Add Employee" %></h1>
<% if (request.getAttribute("error") != null) { %><p class="error"><%= request.getAttribute("error") %></p><% } %>
<form method="post" action="employees">
<input type="hidden" name="action" value="save">
<% if(editing) { %><input type="hidden" name="id" value="<%= e.getId() %>"><% } %>
<label>Full Name<input name="name" required maxlength="100" value="<%= editing ? e.getName() : "" %>"></label>
<label>Email<input type="email" name="email" required maxlength="150" value="<%= editing ? e.getEmail() : "" %>"></label>
<label>Department<input name="department" required maxlength="100" value="<%= editing ? e.getDepartment() : "" %>"></label>
<label>Job Role<input name="jobRole" required maxlength="100" value="<%= editing ? e.getJobRole() : "" %>"></label>
<label>Salary<input type="number" name="salary" required min="0" step="0.01" value="<%= editing ? e.getSalary() : "" %>"></label>
<div class="form-actions"><button type="submit">Save Employee</button><a class="button secondary" href="employees">Cancel</a></div>
</form></main></body></html>
