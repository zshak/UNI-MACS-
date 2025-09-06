<%@ page import="Database.DatabaseManager" %>
<%@ page import="Quiz.Models.Question" %>
<%@ page import="Quiz.Models.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Front Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            max-width: 600px;
            padding: 40px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1 {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
        }

        h1 {
            font-size: 20px;
            color: #333;
            margin-bottom: 20px;
        }

        p {
            font-size: 16px;
            color: #555;
        }

        button.start-button {
            padding: 12px 24px;
            font-size: 18px;
            background-color: #DE3163;
            color: #ffffff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button.start-button:hover {
            background-color: #F33A6A;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <%
        Quiz quiz = (Quiz) request.getSession().getAttribute("curQuiz");
        int score = (int) request.getSession().getAttribute("curScore");
        String quizName = quiz.getName();
        List<Question> questions = (List<Question>) request.getSession().getAttribute("curQuestionsList");
        int percent = (int) ((double) score / questions.size() * 100);
        request.getSession().setAttribute("resultPercent", percent);
        int time = (int) request.getSession().getAttribute("timeNeeded");
        Timestamp finishTime = new Timestamp(System.currentTimeMillis());
        request.getSession().setAttribute("finishTime", finishTime);
    %>
</head>
<body>
<div class="container">
    <h1><%=quizName%>
    </h1>
    <h1><%=score%> /<%=questions.size()%>
    </h1>
    <h1><%=percent%>%</h1>
    <h1>Time: <%=time%> mins</h1>
    <form action="${pageContext.request.contextPath}/QuizResultDisplayServlet" method="post">
        <button type="submit" class="start-button">Close</button>
    </form>
</div>
</body>
</html>
