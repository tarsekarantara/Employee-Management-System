<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html><head><title>EMS Login</title><link rel="stylesheet" href="style.css"></head>
<body><main class="card narrow">
<h1>Employee Management System</h1><h2>Login</h2>
<% if (request.getAttribute("error") != null) { %>
<p class="error"><%= request.getAttribute("error") %></p><% } %>
<form method="post" action="login">
<label>Username<input name="username" required></label>
<label>Password<input type="password" name="password" required></label>
<button type="submit">Login</button>
</form>
<p class="hint">Demo: admin / Admin@123<br>View-only: user / User@123</p>
</main></body></html>
