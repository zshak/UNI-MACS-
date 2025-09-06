<%@ page import="OAuth.Models.User" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Users</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            flex-direction: column;
        }

        h1, h2 {
            margin: 10px;
            text-align: center;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: white;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h1>Users</h1>
<h2>Search Results:</h2>
<table>
    <tr>
        <th>Username</th>
    </tr>
    <%
        List<User> filtered = (List<User>) request.getAttribute("allUsers");
        User currentUser = (User) request.getSession().getAttribute("user");

        for (User user : filtered) {
            if (user.getId() != currentUser.getId()) {
    %>
    <tr>
        <td>
            <a href="HTML/ViewProfile.jsp?userId=<%= user.getId() %>" class="action-link-button">
                <%=user.getUsername()%>
            </a>
        </td>
    </tr>
    <%
            }
        }
    %>

</table>
</body>
</html>
