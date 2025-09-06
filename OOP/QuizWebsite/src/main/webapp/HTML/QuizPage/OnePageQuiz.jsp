<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="java.util.List" %>
<%@ page import="Quiz.Models.*" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">
    <%
        Quiz quiz = (Quiz) request.getSession().getAttribute("curQuiz");
    %>
    <title><%= quiz.getName()%>
    </title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 0;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: auto;
        }

        .container {
            max-width: 600px;
            margin-top: auto;
            padding: 40px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h1 {
            font-size: 24px;
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }

        p.question {
            font-size: 18px;
            color: #555;
            margin-bottom: 20px;
        }

        textarea.response-textarea {
            width: 96%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            resize: vertical;
        }

        input.submit-button {
            padding: 12px 24px;
            font-size: 18px;
            background-color: #DE3163;
            color: #ffffff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
            display: block;
            margin-top: 20px;
            margin-left: auto;
            margin-right: auto;
        }

        input.submit-button:hover {
            background-color: #F33A6A;
        }

        .question-container {
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
            padding: 20px;
            background-color: #f9f9f9;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .image-container img {
            max-width: 550px;
            max-height: 400px;
            display: block;
            margin-top: 10px;
            margin-left: auto;
            margin-right: auto;
        }

        .radio-container {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .radio-container input[type="radio"] {
            margin-right: 8px;
        }

        .radio-container label {
            font-size: 16px;
            color: #555;
        }
    </style>
    <%
        List<Question> questions = (List<Question>) request.getSession().getAttribute("curQuestionsList");
        String questionDisplay = "";
    %>
</head>
<body>
<div class="container">
    <h1><%= quiz.getName()%>
    </h1>
    <form action="${pageContext.request.contextPath}/SinglePageServlet" method="post">
        <%
            for (Question q : questions) {
                int questionType = q.getTypeId();
        %>

        <div class="question-container">
            <%
                if (questionType == 0 || questionType == 1) {
                    questionDisplay = q.getContent();
            %>
            <p class="question"><%=questionDisplay%>
            </p>
            <textarea name="response<%=q.getId()%>" rows="4" cols="50" class="response-textarea " required
                      placeholder="Enter your answer here"></textarea>
            <%
            } else if (questionType == 2) {
            %>
            <p class="question"><%=q.getContent()%>
            </p>
            <%
                for (Answer ans : ((MultipleChoiceQuestion) q).getPossibleAnswers()) {
            %>
            <div class="radio-container">
                <input type="radio" name="response<%=q.getId()%>" value=<%=ans.getContent()%> required>
                <label><%= ans.getContent() %>
                </label>
            </div>
            <br>
            <%
                }
            } else if (questionType == 3) {
            %>
            <div class="image-container">
                <img class="image-container" src="<%=q.getContent()%>" alt="Image Preview">
            </div>
            <br>
            <textarea name="response<%=q.getId()%>" rows="4" cols="50" class="response-textarea" required
                      placeholder="Enter your answer here"></textarea>
            <% } %>
        </div>
        <% } %>
        <input type="submit" value="Submit Quiz" class="submit-button">
    </form>
</div>
</body>
</html>
