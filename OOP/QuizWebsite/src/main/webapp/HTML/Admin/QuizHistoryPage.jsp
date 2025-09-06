<%@ page import="java.util.List" %>
<%@ page import="Quiz.Models.QuizHistory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz History</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }

        h1, h2 {
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #ff69b4;
            color: white;
        }

        tr:hover {
            background-color: #f2f2f2;
        }
    </style>

</head>
<body>
<h1>Quiz History</h1>

<table>
    <tr>
        <th>Username</th>
        <th>Score</th>
        <th>Percentage</th>
        <th>Duration</th>
        <th>Finish Time</th>
    </tr>

    <% for (QuizHistory quizResult : (List<QuizHistory>) request.getSession().getAttribute("quizHistory")) { %>
    <tr>
        <td><%= quizResult.getUsername() %>
        </td>
        <td><%= quizResult.getScore() %>
        </td>
        <td><%= quizResult.getPercent() %>
        </td>
        <td><%= quizResult.getQuizDuration() %>
        </td>
        <td><%= quizResult.getFinishTime() %>
        </td>
    </tr>
    <% } %>
</table>


</body>
</html>
