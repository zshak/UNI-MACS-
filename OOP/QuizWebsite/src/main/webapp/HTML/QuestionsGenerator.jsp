<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Questions to Quiz</title>
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
            text-align: center;
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

        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            margin-bottom: 15px;
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
    <h1>Add Questions to Quiz</h1>

    <form action="${pageContext.request.contextPath}/QuestionTypeSelectionServlet" method="post">
        <label for="questionType">Select question type:</label>
        <select id="questionType" name="questionType">
            <option value="question-response">Question-Response</option>
            <option value="fill-in-the-blank">Fill in the Blank</option>
            <option value="picture-response">Picture-Response Question</option>
            <option value="multiple-choice">Multiple Choice</option>
        </select>
        <br>
        <button type="submit" class="button">Next</button>
    </form>
</div>
</body>
</html>
