<%@ page import="Quiz.Models.Question" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Input Answer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #FCE4EC;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #FF80AB;
            background-color: #FFFFFF;
            border-radius: 5px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #FF1493; /* Deep Pink */
        }

        .question {
            margin-bottom: 20px;
        }

        .question-description {
            padding: 10px;
            border-radius: 5px;
            background-color: #FFC0CB;
        }

        .select-option {
            margin: 10px 0;
            padding: 10px;
            border-radius: 5px;
            background-color: #FFC0CB;
        }

        .button-container {
            text-align: center;
        }

        .button {
            margin: 10px;
            padding: 10px 20px;
            background-color: #FF80AB;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 3px;
            font-weight: bold;
            transition: background-color 0.3s ease-in-out;
        }

        .button:hover {
            background-color: #FF1493;
        }

        .question-label {
            display: inline-block;
            font-weight: bold;
            margin-bottom: 5px;
            color: #FF1493;
        }

        .option-heading {
            color: #FF1493;
        }

        .option-label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #FF1493;
        }

        textarea {
            width: 100%;
            padding: 10px;
            border-radius: 3px;
            border: 1px solid #ccc;
        }

        #stringContainer input {
            width: 100%;
            margin-bottom: 5px;
            padding: 5px;
            border-radius: 3px;
            border: 1px solid #ccc;
        }


    </style>
</head>
<body>

<div class="container">
    <h1>Input Answer</h1>
    <form id="quizForm" action="${pageContext.request.contextPath}/SubmitQuizServlet" method="post">
        <div class="question">
            <% Question question = (Question) request.getSession().getAttribute("question"); %>
            <%= question.getCreatingHtml() %>
        </div>
        <div class="button-container" style="text-align: center;">
            <button type="button" class="button"
                    onclick="submitForm('${pageContext.request.contextPath}/SubmitQuizServlet', 'submitQuiz')">Submit
                Quiz
            </button>
            <button type="button" class="button"
                    onclick="submitForm('${pageContext.request.contextPath}/AnswerSelectionServlet', 'submitQuestion')">
                Submit Question
            </button>
        </div>
    </form>
</div>

<script>
    function submitForm(actionUrl, actionValue) {
        var form = document.getElementById('quizForm');
        form.action = actionUrl;
        var actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = actionValue;
        form.appendChild(actionInput);
        form.submit();
    }
</script>


</body>
</html>
