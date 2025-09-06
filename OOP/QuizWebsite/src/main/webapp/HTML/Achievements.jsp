<%@ page import="java.util.List" %>
<%@ page import="Achievements.Models.Achievement" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Achievements</title>
    <style>
        body {
            background: linear-gradient(-45deg, #a45163, #964b7a, #526d8b, #5f8c7d);
            color: #800080;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
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

        .go-back a {
            display: inline-block;
            background-color: #f4c2c2;
            color: #fff;
            padding: 5px 10px;
            border-radius: 3px;
            text-decoration: none;
        }

        .achievements-list {
            margin-top: 20px;
            list-style: none;
            padding: 0;
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

        .achievement {
            margin-bottom: 20px;
            padding: 20px;
            background-color: #ff99ff;
            border-radius: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>User Achievements</h1>

    <button class="go-back-button" onclick="goBack()">Go Back</button>

    <ul class="achievements-list">

        <% List<Achievement> achievements = (List<Achievement>) request.getAttribute("achievements"); %>


        <% for (Achievement achievement : achievements) { %>
        <li class="achievement">
            <h3><%= achievement.getName() %>
            </h3>
            <p>Congratulations! You <%= achievement.getRequirement() %>.</p>
        </li>
        <% } %>
    </ul>
</div>
<script>
    function goBack() {
        window.history.back();
    }
</script>
</body>
</html>