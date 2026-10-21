<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.ems.Employee" %>
<%
    List<Employee> employees = (List<Employee>) request.getAttribute("employees");
    String role = (String) session.getAttribute("role");
    boolean admin = "ADMIN".equals(role);
%>
<!DOCTYPE html><html><head><title>Employees</title><link rel="stylesheet" href="style.css"></head>
<body><main class="wide">
<header><div><h1>Employee Management System</h1><p>Signed in as <b><%= session.getAttribute("username") %></b> (<%= role %>)</p></div>
<a class="button secondary" href="logout">Logout</a></header>
<section class="toolbar">
<form method="get" action="employees"><input name="search" placeholder="Search name or department" value="<%= request.getParameter("search") == null ? "" : request.getParameter("search") %>"><button>Search</button></form>
<% if (admin) { %><a class="button" href="employee-form.jsp">+ Add Employee</a><% } %>
</section>
<div class="table-wrap"><table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th><th>Job Role</th><th>Salary</th><% if(admin) { %><th>Actions</th><% } %></tr></thead><tbody>
<% if (employees == null || employees.isEmpty()) { %><tr><td colspan="7">No employees found.</td></tr><% } else { for(Employee e : employees) { %>
<tr><td><%= e.getId() %></td><td><%= e.getName() %></td><td><%= e.getEmail() %></td><td><%= e.getDepartment() %></td><td><%= e.getJobRole() %></td><td><%= e.getSalary() %></td>
<% if(admin) { %><td class="actions"><a href="employees?action=edit&id=<%= e.getId() %>">Edit</a>
<form method="post" action="employees" onsubmit="return confirm('Delete this employee?')"><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%= e.getId() %>"><button class="danger" type="submit">Delete</button></form></td><% } %></tr>
<% } } %>
</tbody></table></div>
<p class="hint">ADMIN can manage records. USER has view-only access.</p>
</main></body></html>
