<%@ page import="Database.DatabaseManager" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Quiz.Models.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            background: linear-gradient(-45deg, #fdb8a0, #f5a0bf, #7ab7d0, #88d2c0);
            width: calc(100vw - 40px);
            height: calc(100vh - 40px);
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            font-family: Arial, sans-serif;
            overflow: hidden;
        }

        .quiz-container {
            background-color: #ffffff;
            border-radius: 5px;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 80%;
            max-width: 800px;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
            padding: 10px;
            border-bottom: 1px solid #ccc;
        }

        button {
            background-color: #e816a5;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        button:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<%
    int userId = Integer.parseInt(request.getParameter("userId"));
    User friend = null;
    try {
        friend = DatabaseManager.getUser(userId);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<div class="quiz-container">
    <h1>Challenge <%= friend.getUsername() %> in quiz</h1>
    <ul>
        <%
            User currentUser = (User) session.getAttribute("user");
            List<Quiz> quizList = null;
            try {
                quizList = DatabaseManager.getAllQuizes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (Quiz quiz : quizList) {
        %>
        <li>
            <%= quiz.getName() %>
            <form method="post" action="${pageContext.request.contextPath}/ChallengeFriend">
                <input type="hidden" name="friendId" value="<%= friend.getId() %>">
                <input type="hidden" name="quizId" value="<%= quiz.getId() %>">
                <button type="submit" class="challenge-button" data-friend-id="<%= friend.getId() %>"
                        data-quiz-id="<%= quiz.getId() %>">Challenge
                </button>
            </form>
        </li>
        <%
            }
        %>
    </ul>
</div>

<script>
    var challengeButtons = document.querySelectorAll('.challenge-button');
    challengeButtons.forEach(function (button) {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            var friendId = button.getAttribute('data-friend-id');
            var quizId = button.getAttribute('data-quiz-id');
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '${pageContext.request.contextPath}/ChallengeFriend', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            var data = 'friendId=' + encodeURIComponent(friendId) + '&quizId=' + encodeURIComponent(quizId);
            xhr.send(data);
        });
    });
</script>
</body>
</html>
