<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="java.util.List" %>
<%@ page import="Quiz.Models.*" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <%
        Quiz quiz = (Quiz) request.getSession().getAttribute("curQuiz");
        List<Question> questions = (List<Question>) request.getSession().getAttribute("curQuestionsList");
        int questionIndex = 0;
        String questionDisplay = "";
        String answerDisplay = "";
    %>
    <meta charset="UTF-8">
    <title><%= quiz.getName()%>
    </title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 0;
            margin: 0;
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

        .feedback {
            font-size: 24px;
            color: #006400;
            text-align: center;
            margin-bottom: 20px;
        }

        .incorrect-feedback {
            font-size: 24px;
            color: #8B0000;
            text-align: center;
            margin-bottom: 20px;
        }

    </style>
</head>
<body>
<div class="container">
    <form action="${pageContext.request.contextPath}/MultiplePageImmediateCorrectionServlet" method="post">
        <%
            String feedback = (String) request.getSession().getAttribute("curFeedback");
            String feedbackClass = "";
        %>
        <div class="question-container">
            <%
                if (feedback.equals("Correct Answer!")) {
                    feedbackClass = "feedback";
                } else {
                    feedbackClass = "incorrect-feedback";
                }
            %>
            <h1 class="<%=feedbackClass%>"><%=feedback %>
            </h1 class="<%=feedbackClass%>">
        </div>
        <input type="submit" value="Next Question" class="submit-button">
    </form>
</div>
</body>
</html>