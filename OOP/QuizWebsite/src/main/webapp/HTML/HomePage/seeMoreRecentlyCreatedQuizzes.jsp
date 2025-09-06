<%@ page import="java.util.List" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="Quiz.Models.Quiz" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recently Created Quizzes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: pink;
            animation: gradient 15s ease infinite;
        }

        @keyframes gradient {
            0% {
                background-position: 0% 50%;
            }

            50% {
                background-position: 100% 50%;
            }

            100% {
                background-position: 0% 50%;
            }
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }

        h1 {
            text-align: center;
            margin-bottom: 20px;
            color: #e56d87;
            font-size: 24px;
        }

        .quiz {
            font-size: 17px;
            color: #555;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        ul {
            list-style-type: none;
            padding: 0;
            height: fit-content;
            overflow-y: auto;
        }

        li {
            margin-bottom: 15px;
            border: 1px solid #f0c7c9;
            padding: 15px;
            background-color: #fce4ec;
            transition: box-shadow 0.3s ease;
            border-radius: 5px;
        }

        li:hover {
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
        }

        .quiz-details {
            flex: 1;
        }

        .quiz-name {
            font-size: 20px;
            font-weight: bold;
            color: #e56d87;
            margin-bottom: 5px;
        }

        .quiz-description {
            color: #888;
            font-size: 16px;
            margin-top: 6px;
        }

        .created-by {
            font-size: 16px;
            color: #555;
            margin-top: 10px;
        }

        .pink-anchor {
            color: #ff6090;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .pink-anchor:hover {
            color: #ff427b;
            text-decoration: underline;
        }

        .creation-date {
            font-size: 16px;
            color: #888;
            float: right;
            padding-right: 5px;
        }

        .no-quizzes {
            font-size: 18px;
            font-weight: bold;
            color: #999;
            text-align: center;
            margin-top: 50px;
        }

        .go-back-button {
            background-color: #ff6090;
            color: #fff;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
            position: fixed;
            top: 20px;
            left: 20px;
            z-index: 1000;
        }

        .go-back-button:hover {
            background-color: #d15273;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Recently Created Quizzes</h1>
    <%
        List<Quiz> quizzes = DatabaseManager.getAllQuizzes();

        quizzes.sort(Comparator.comparing(Quiz::getCreationDateTime));

        if (quizzes.isEmpty()) {
    %>
    <p class="no-quizzes">There are no quizzes</p>
    <%
    } else {
    %>
    <h2 class="quiz">Quiz Details
        <span class="creation-flex">
            Creation Date and Time
        </span>
    </h2>
    <ul>
        <%
            for (int i = 0; i < quizzes.size(); i++) {
                Quiz quiz = quizzes.get(i);
                int creatorId = quiz.getCreatorId();
                int quizId = quiz.getId();
                User creator = DatabaseManager.getUser(creatorId);
        %>
        <li>
            <div class="quiz-details">
                <h2 class="quiz-name">
                    <a class="pink-anchor"
                       href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= quizId %>">
                        <%= quiz.getName()%>
                    </a>
                    <span class="creation-date">
                        <%= new SimpleDateFormat("EEE, MMM d, yyyy HH:mm").format(quiz.getCreationDateTime())%>
                    </span>
                </h2>
                <p class="quiz-description"><%= quiz.getDescription() %>
                </p>
                <p class="created-by">
                    Created by <a class="pink-anchor"
                                  href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= creatorId %>">
                    <%= creator.getUsername()%>
                </a>
                </p>
            </div>
        </li>
        <%
                }
            }
        %>
    </ul>
    <button class="go-back-button" onclick="goBack()">Go Back</button>
</div>
<script>
    function goBack() {
        window.history.back();
    }
</script>
</body>
</html>