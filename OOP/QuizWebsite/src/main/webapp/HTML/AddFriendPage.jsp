<%@ page import="java.util.List" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Friends</title>
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
            margin: 0 auto;
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

        button {
            background-color: #f4c2c2;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 3px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="container">
    <% User currentUser = (User) session.getAttribute("user"); %>
    <h1>Add Friends</h1>
    <form method="post" action="${pageContext.request.contextPath}/UserSearchServlet">
        <label for="searchField">Search Users:</label>
        <input type="text" id="searchField" name="searchQuery">
        <button type="submit">Search</button>
    </form>
    <h2>Current User: <%= currentUser.getUsername() %>
    </h2>

    <h2>All Users:</h2>
    <ul>
        <%
            List<User> allUsers = null;
            List<User> userFriends = null;
            List<User> SendFriendRequests = null;
            try {
                allUsers = DatabaseManager.GetAllUsers();
                userFriends = DatabaseManager.GetUserFriends(currentUser.getId());
                SendFriendRequests = DatabaseManager.GetFriendsWithReqs(currentUser.getId());
                userFriends.addAll(SendFriendRequests);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<User> filteredUsers = new ArrayList<>();
            for (User user : allUsers) {
                if (user.getId() != currentUser.getId() && !userFriends.contains(user)) {
                    filteredUsers.add(user);
                }
            }
            for (User filterUser : filteredUsers) {
        %>
        <li>
            <%= filterUser.getUsername() %>
            <form method="post" action="${pageContext.request.contextPath}/AddFriendServlet">
                <input type="hidden" name="friendId" value="<%= filterUser.getId() %>">
                <button type="submit" class="send-btn" data-id="<%= filterUser.getId() %>">Add Friend</button>
            </form>
        </li>
        <%
            }
        %>
    </ul>
</div>

<script>
</script>

</body>
</html>
