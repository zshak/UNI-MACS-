<%@ page import="java.util.List" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Friends</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        .container {
            background: linear-gradient(-45deg, #fdb8a0, #f5a0bf, #7ab7d0, #88d2c0);
            width: calc(100vw - 40px);
            height: calc(100vh - 40px);
            margin: 0;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            overflow: hidden;

        }

        h1 {
            text-align: center;
            margin-bottom: 20px;
        }

        form {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        label {
            font-weight: bold;
            margin-right: 10px;
        }

        input[type="text"] {
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 3px;
            flex: 1;
        }

        button[type="submit"] {
            background-color: #f4c2c2;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 3px;
            cursor: pointer;
        }

        h2 {
            margin-top: 20px;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 5px;
            padding: 5px 10px;
            background-color: #f9f9f9;
            border: 1px solid #ccc;
            border-radius: 3px;
        }

        .buttons {
            display: flex;
            gap: 10px;
        }

        .add-friend-button {
            background-color: #f4c2c2;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 3px;
            cursor: pointer;
            display: block;
            margin: 0 auto;
        }


        .friends-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .action-link-button {
            background: none;
            border: none;
            padding: 0;
            font: inherit;
            color: #f4c2c2;
            cursor: pointer;
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="friends-header">
        Friends
        <a href="${pageContext.request.contextPath}/HTML/AddFriendPage.jsp" class="add-friend-button">+Add Friend</a>
    </h1>
    <form method="post" action="${pageContext.request.contextPath}/SearchFriendsServlet">
        <label for="searchField">Search Friends:</label>
        <input type="text" id="searchField" name="searchQuery">
        <button type="submit">Search</button>
    </form>
    <%
        User currentUser = (User) session.getAttribute("user");

    %>
    <h2>User's Friends:</h2>
    <ul>
        <%
            List<User> userFriends = null;
            try {
                userFriends = DatabaseManager.GetUserFriends(currentUser.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<User> displayList = userFriends;
            List<User> filteredFriends = (List<User>) request.getAttribute("filteredFriends");
            if (filteredFriends != null && !filteredFriends.isEmpty()) {
                displayList = filteredFriends;
            }
            for (User friend : displayList) {
        %>
        <li>
            <%= friend.getUsername() %>
            <div class="buttons">
                <a href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= friend.getId() %>"
                   class="action-link-button">
                    View Profile
                </a>
                <a href="${pageContext.request.contextPath}/HTML/SendMessage.jsp?friendId=<%= friend.getId() %>"
                   class="action-link-button">
                    Send Message
                </a>
                <a href="${pageContext.request.contextPath}/HTML/Challenge.jsp?userId=<%= friend.getId() %>"
                   class="action-link-button">
                    Challenge
                </a>
            </div>

        </li>
        <%
            }
        %>
    </ul>
</div>
</body>
</html>
