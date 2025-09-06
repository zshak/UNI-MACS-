<%@ page import="Quiz.Models.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="OAuth.Models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }

        h1, h2 {
            color: #ff69b4;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin: 10px 0;
            padding: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        a {
            color: #ff69b4;
            text-decoration: none;
        }

        a:hover {
            color: #ff4081;
        }

        .button-container {
            margin-top: 10px;
        }

        .button {
            background-color: #ff69b4;
            color: white;
            border: none;
            padding: 5px 10px;
            font-size: 14px;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 10px;
            margin-bottom: 5px;
        }

        .button:hover {
            background-color: #ff4081;
        }
    </style>

</head>
<body>
<h1>Users</h1>

<ul>
    <% for (User user : (List<User>) request.getSession().getAttribute("users")) { %>
    <li>
        <h2><a href="HTML/ViewProfile.jsp?userId=<%= user.getId() %>"><%= user.getUsername() %>
        </a></h2>

        <div class="button-container">
            <form action="${pageContext.request.contextPath}/RemoveUserServlet" method="post">
                <input type="hidden" name="userId" value="<%= user.getId() %>">
                <button class="button" type="submit">Remove User</button>
            </form>

            <form action="${pageContext.request.contextPath}/PromoteUserServlet" method="post">
                <input type="hidden" name="userId" value="<%= user.getId() %>">
                <button class="button" type="submit">Promote User</button>
            </form>

        </div>
    </li>
    <% } %>
</ul>


</body>
</html>
