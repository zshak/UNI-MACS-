<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Creation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .form-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 400px;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
            text-align: center;
        }

        label {
            font-weight: bold;
            display: block;
            margin-bottom: 10px;
        }

        input[type="text"],
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            margin-bottom: 15px;
        }

        .checkbox-group {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        input[type="checkbox"] {
            margin-right: 10px;
        }

        .button {
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
            width: 100%;
            transition: background-color 0.3s ease;
        }

        .button:hover {
            background-color: #ff4081;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h1>Create a New Quiz</h1>
    <form action="${pageContext.request.contextPath}/QuizCreationServlet" method="post">
        <label for="quizTitle">Quiz Title</label>
        <input type="text" id="quizTitle" name="quizTitle" required>

        <label for="quizDescription">Quiz Description</label>
        <textarea id="quizDescription" name="quizDescription" rows="4" required></textarea>

        <div class="checkbox-group">
            <input type="checkbox" id="randomQuestions" name="randomQuestions">
            <label for="randomQuestions">Randomize Questions</label>
        </div>

        <div class="checkbox-group">
            <input type="checkbox" name="isOnePage" id="isOnePage">
            <label for="isOnePage">Single Page Quiz</label>
        </div>

        <div class="checkbox-group">
            <input type="checkbox" name="isImmediateCorrection" id="isImmediateCorrection">
            <label for="isImmediateCorrection">Immediate Correction</label>
        </div>

        <script>
            const isOnePageCheckbox = document.getElementById("isOnePageCheckbox");
            const isImmediateCorrectionCheckbox = document.getElementById("isImmediateCorrectionCheckbox");

            isOnePageCheckbox.addEventListener("change", function () {
                if (this.checked) {
                    isImmediateCorrectionCheckbox.checked = false;
                }
            });

            isImmediateCorrectionCheckbox.addEventListener("change", function () {
                if (this.checked) {
                    isOnePageCheckbox.checked = false;
                }
            });
        </script>


        <button type="submit" class="button">Create Quiz</button>
    </form>
</div>
</body>
</html>
