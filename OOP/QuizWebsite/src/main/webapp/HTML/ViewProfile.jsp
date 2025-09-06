<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Mails.Mail" %>
<%@ page import="Quiz.Models.Quiz" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<%
    User currentUser = (User) session.getAttribute("user");
    boolean areFriends = false;
    List<User> FriendRequestSent;
    try {
        FriendRequestSent = DatabaseManager.GetFriendsWithReqs(currentUser.getId());
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    List<User> currentUserFriends;
    try {
        currentUserFriends = DatabaseManager.GetUserFriends(currentUser.getId());
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    List<Mail> incomingMails;
    try {
        incomingMails = DatabaseManager.GetUserNotifications(currentUser.getId());
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    int userId = Integer.parseInt(request.getParameter("userId"));
    User user = null;
    try {
        user = DatabaseManager.getUser(userId);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    boolean requestSend = false;
    Mail correctMail = null;
    for (Mail mail : incomingMails) {
        if (mail.getFromId() == userId && mail.getType().equals("FRT")) {
            requestSend = true;
            correctMail = mail;
        }

    }
    if (currentUserFriends.contains(user)) {
        areFriends = true;
    }
    boolean currentUserAlreadySent = FriendRequestSent.contains(user);
%>
<head>
    <meta charset="UTF-8">
    <title>Welcome to <%= user.getUsername() %>'s Page</title>
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }

        body {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: linear-gradient(-45deg, #fdb8a0, #f5a0bf, #7ab7d0, #88d2c0);
            background-size: 400% 400%;
            animation: gradient 15s ease infinite;
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

        h1 {
            margin: 20px 0;
            padding: 20px;
            font-size: 24px;
            color: #fff;
            text-align: center;
            text-transform: uppercase;
            background: linear-gradient(45deg, #ff5c8a, #ff3366);
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h2 {
            margin: 20px 0;
            padding: 10px;
            font-size: 16px;
            color: #ff5c8a;
            text-align: center;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .empty-message {
            margin: 10px;
            padding: 10px;
            font-size: 18px;
            color: #ff5c8a;
            background-color: beige;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .button-container {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .action-button {
            display: inline-block;
            padding: 10px 20px;
            margin: 0 10px;
            background-color: #ff7eb9;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .action-button:hover {
            background-color: #ff5c8a;
        }

        .scrollable-list {
            overflow-y: auto;
            width: 50%;
            height: 100%;

        }

        .list-container {
            margin: 0;
            padding: 0;
            background: white;
            border-radius: 0;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border: 1px solid deeppink;
        }

        .list-item {
            margin: 10px 0;
            padding-left: 20px;
            list-style-type: square;
            font-size: 16px;
        }

        .pink-anchor {
            color: #ff5c8a;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .pink-anchor:hover {
            color: #ff3366;
        }

        .created-by {
            color: gray;
        }

        h3 {
            margin: 0;
            padding: 5px 0;
            font-size: 20px;
            color: #ff5c8a;
        }
    </style>
</head>
<body>
<h1>Welcome to <%= user.getUsername() %>'s Page</h1>


<div class="button-container">
    <%
        if (currentUser.getId() != userId) {
            if (areFriends) {
    %>
    <button class="action-button">Friends</button>
    <% } else if (requestSend) { %>
    <form method="post" action="../AcceptFriendRequestServlet">
        <input type="hidden" name="mailId" value="<%= correctMail.getId() %>">
        <input type="hidden" name="fromId" value="<%= correctMail.getFromId() %>">
        <button type="submit" class="action-button yes-button">Yes</button>
    </form>
    <form method="post" action="../RemoveFriendRequestServlet">
        <input type="hidden" name="mailId" value="<%= correctMail.getId() %>">
        <button type="submit" class="action-button yes-button">No</button>
    </form>
    <% } else if (currentUserAlreadySent) { %>
    <button class="action-button">Already Sent</button>
    <% } else { %>
    <form method="post" action="../AddFriendServlet">
        <input type="hidden" name="friendId" value="<%= user.getId() %>">
        <button type="submit" class="action-button">Send Friend Request</button>
    </form>
    <% }
    }
    %>

</div>

<h2>Friend List</h2>
<div class="scrollable-list">
    <div class="list-container">
        <ul>
            <% List<User> friendList = null;
                try {
                    friendList = DatabaseManager.GetUserFriends(user.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (friendList.isEmpty()) {
            %>
            <p class="empty-message">No friends yet.</p>
            <% } else {
                for (User friend : friendList) {
                    if (friend.getId() == currentUser.getId()) {
                        continue;
                    }
            %>
            <li class="list-item">
                <a class="pink-anchor h3"
                   href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= friend.getId() %>"
                   class="pink-anchor h3"><%= friend.getUsername() %>
                </a>
            </li>
            <% }
            }
            %>
        </ul>
    </div>
</div>

<h2>Quizzes Created</h2>
<div class="scrollable-list">
    <div class="list-container">
        <ul>
            <% var myQuizzes = DatabaseManager.getAllQuizzes();
                List<Quiz> results = myQuizzes.stream()
                        .filter(quiz -> quiz.getCreatorId() == userId)
                        .toList();
                if (results.isEmpty()) {
            %>
            <p class="empty-message">No quizzes created yet.</p>
            <% } else {
                for (int i = 0; i < results.size(); i++) {
                    Quiz quiz = results.get(i);
                    int creatorId = quiz.getCreatorId();
                    User creator = DatabaseManager.getUser(creatorId);
            %>
            <li class="list-item">
                <h3 class="quiz-name">
                    <a class="pink-anchor"
                       href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= quiz.getId() %>"><%= quiz.getName() %>
                    </a>
                </h3>
                <p class="created-by">
                    Created by <a class="pink-anchor"
                                  href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= creatorId %>"><%= creator.getUsername() %>
                </a>
                </p>
            </li>
            <% }
            }
            %>
        </ul>
    </div>
</div>

<h2>Achievements</h2>
<div class="scrollable-list">
    <div class="list-container">
        <ul>
            <% var achievements = DatabaseManager.getUserAchievements(DatabaseManager.getUser(userId));
                if (achievements.isEmpty()) {
            %>
            <p class="empty-message">No achievements yet.</p>
            <% } else {
                for (int i = 0; i < achievements.size(); i++) {
                    var name = achievements.get(i).getName();
                    var description = achievements.get(i).getRequirement();
            %>
            <li class="list-item">
                <h3 class="quiz-name"><%= name %>
                </h3>
                <p class="created-by"><%= description %>
                </p>
            </li>
            <% }
            }
            %>
        </ul>
    </div>
</div>

</body>
</html>
