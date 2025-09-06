<%@ page import="Database.DatabaseManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../CommonJsp.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin</title>
    <style>

        .button-container {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            margin-top: 20px;
        }

        .button {
            background-color: #ff69b4;
            color: white;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
            margin-bottom: 10px;
        }

        .button:hover {
            background-color: #ff4081;
        }

        .announcement-form {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .announcement-form label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }

        .announcement-form textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            margin-bottom: 10px;
        }

        .announcement-form input[type="submit"] {
            background-color: #ff69b4;
            color: white;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
        }

        .announcement-form input[type="submit"]:hover {
            background-color: #ff4081;
        }
    </style>
</head>
<body>
<h1>Administrator Interface</h1>

<div class="announcement-form">
    <h2>Create Announcement</h2>
    <form action="${pageContext.request.contextPath}/CreateAnnouncementServlet" method="post">
        <!-- Change to your servlet URL -->
        <label for="announcement">Announcement:</label><br>
        <textarea id="announcement" name="announcement" rows="4" cols="50" required></textarea><br><br>
        <input type="submit" value="Create Announcement">
    </form>
</div>

<div class="button-container">
    <a href="${pageContext.request.contextPath}/ManageQuizzesServlet" class="button">Quizzes</a>
    <a href="${pageContext.request.contextPath}/ManageUsersServlet" class="button">Users</a>
</div>

<h2>Site Statistics</h2>
<p>Number of Users: <%=String.valueOf(DatabaseManager.getNumberOfUsers())%>

</p>
<p>Number of Quizzes Taken: <%=String.valueOf(DatabaseManager.getNumberOfQuizzesTaken())%>
</p>
</body>
</html>
