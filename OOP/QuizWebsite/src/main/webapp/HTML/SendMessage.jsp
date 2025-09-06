<%@ page import="OAuth.Models.User" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="CommonJsp.jsp" %>
<html>
<head>
    <title>Send Message</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            height: 100%;
        }

        .send-message-container {
            background: linear-gradient(-45deg, #fdb8a0, #f5a0bf, #7ab7d0, #88d2c0);
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin: auto;
            margin-top: 30px;
            width: 100%;
            height: 100%;
        }

        .send-message-container h1 {
            margin-bottom: 20px;
            color: #333;
        }

        .message-form label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
            color: #555;
        }

        .message-form textarea {
            width: 100%;
            height: 150px;
            border: 2px solid #ddd;
            border-radius: 5px;
            padding: 10px;
            font-size: 16px;
            color: #333;
            resize: vertical;
        }

        .send-button {
            margin-top: 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .send-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("user");
    int to_id = Integer.parseInt(request.getParameter("friendId"));
    User friendUser = null;
    try {
        friendUser = DatabaseManager.getUser(to_id);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<div class="send-message-container">
    <h1>Send Message</h1>
    <form class="message-form" method="post" action="../SendMessageServlet">
        <h2>Send a message to <%=friendUser.getUsername()%>
        </h2>
        <input type="hidden" name="recipientId" value="<%=friendUser.getId()%>">
        <label for="message">Message:</label>
        <textarea id="message" name="message" placeholder="Write your message (up to 1000 characters)" maxlength="1000"
                  required></textarea>

        <button type="submit" class="send-button">Send</button>
    </form>
</div>
</body>
</html>
