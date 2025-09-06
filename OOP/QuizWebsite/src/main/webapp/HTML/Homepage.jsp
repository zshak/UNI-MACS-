<%@ page import="OAuth.Models.User" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="Quiz.Models.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="QuizPage.Models.Result" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(-45deg, #fdb8a0, #f5a0bf, #7ab7d0, #88d2c0);
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

        header {
            background-color: hotpink;
            color: #ffffff;
            padding: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .top-left {
            font-weight: bold;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
        }

        .action-button {
            background-color: hotpink;
            color: #ffffff;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.2s;
        }

        .action-button:hover {
            background-color: #0056b3;
        }

        .announcements-table {
            width: 30px;
            margin: 20px 0;
            border-collapse: collapse;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            float: left;
        }

        .announcements-table th,
        .announcements-table td {
            border: 1px solid #efe597;
            padding: 12px;
            text-align: center;
        }

        .announcements-table th {
            background-color: #efe597;
        }

        .announcements-table tr:hover {
            background-color: #efe597;
        }

        .box {
            width: 80px;
            height: 140px;
            margin: 20px;
            padding: 10px;
            border: 1px solid #ddd;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            float: left;
            background-color: #efe597;
            display: flex;
            flex-direction: column;
        }

        .box-title {
            font-weight: bold;
            margin-bottom: 10px;
        }

        .box-content {
            margin-bottom: 10px;
            flex-grow: 1;
        }

        .see-more-button {
            margin-top: 10px;
            align-self: flex-end;
            background-color: #b115a9;
            color: #ffffff;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.2s;
            position: absolute;
        }

        .see-more-button:hover {
            background-color: #b115a9;
        }

        .big-box {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            padding: 10px;
            border: 1px solid #030303;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            background-color: #efe597;
            margin: 20px;
        }

        .box {
            flex: 1 0 40%;
            padding: 5px;
            border: 1px solid #ddd;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            background-color: #f9f9f9;
        }

        .scrollable-box {
            overflow-y: auto;
        }

        .pink-box {
            background-color: #FCE4EC;
            border: 1px solid #FF69B4;
            padding: 15px;
        }

        .pink-text {
            font-size: 18px;
            font-weight: bold;
            color: deeppink;
            margin-bottom: 10px;
        }

        .quiz-list {
            list-style: none;
            padding: 0;
        }

        .quiz-item {
            margin-bottom: 5px;
            padding: 0px;
        }

        .quiz-details {
            font-size: 14px;
            margin: 5px 0;
        }

        .quiz-name {
            font-size: 16px;
            margin-bottom: 8px;
        }

        a,
        .created-by a {
            color: #FF69B4;
            font-weight: bold;
            text-decoration: none;
            transition: color 0.3s;
        }

        .quiz-name a:hover,
        .created-by a:hover {
            color: #f50d66;
            text-decoration: underline;
        }

        .created-by {
            color: #666;
            font-size: 12px;
            margin-top: 5px;
        }

        .pink-button {
            display: block;
            text-align: center;
            margin-top: 105px;
            color: #FFF;
            background-color: #FF69B4;
            padding: 8px 15px;
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s;
        }

        .find-button {
            color: #FFF;
            background-color: #FF69B4;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .quiz-icon {
            color: hotpink;
        }


        .pink-button:hover {
            background-color: #E9518B;
        }

        @media (max-width: 768px) {
            .big-box {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<header>
    <div class="top-left">
        <% User currentUser = (User) session.getAttribute("user");
            session.setAttribute("user", currentUser);
        %>
        Welcome, <%= currentUser.getUsername() %>!
    </div>
    <div class="action-buttons">
        <a class="action-button" href="${pageContext.request.contextPath}/LogOutServlet">Log out</a>
    </div>
</header>
<table class="announcements-table">
    <tr>
        <th>Main</th>
    </tr>
    <tr>
        <td>
            <a class="action-button"
               href="${pageContext.request.contextPath}/ShowAnnouncementsServlet">Announcements</a>
        </td>
    </tr>
    <tr>
        <td>
            <a class="action-button" href="${pageContext.request.contextPath}/HTML/notifications.jsp">Notifications</a>
        </td>
    </tr>
    <tr>
        <td>
            <a class="action-button" href="${pageContext.request.contextPath}/HTML/QuizCreation.jsp">Create Quiz</a>
        </td>
    </tr>
    <tr>
        <td>
            <a class="action-button" href="${pageContext.request.contextPath}/HTML/Friends.jsp">Friends</a>
        </td>
            <%
    boolean isAdmin = (boolean) session.getAttribute("is_admin");
    if (isAdmin) {
%>
    <tr>
        <td>
            <a class="action-button" href="${pageContext.request.contextPath}/HTML/Admin/AdminJsp.jsp">Admin Page</a>
        </td>
    </tr>
    <%
        }
    %>
</table>

<div class="big-box">
    <div class="box scrollable-box pink-box">
        <div class="box-title pink-text">Popular Quizzes</div>
        <div class="box-content">
            <ul class="quiz-list">
                <%
                    List<Quiz> quizzes = DatabaseManager.getAllQuizzes();
                    quizzes.sort(Comparator.comparingInt(Quiz::getSubmissionCount).reversed());

                    for (int i = 0; i < Math.min(quizzes.size(), 3); i++) {
                        Quiz quiz = quizzes.get(i);
                        int creatorId = quiz.getCreatorId();
                        int quizId = quiz.getId();
                        User creator = DatabaseManager.getUser(creatorId);
                %>
                <li class="quiz-item">
                    <div class="quiz-details">
                        <h2 class="quiz-name">
                            <span class="quiz-icon">&#x25ba </span>
                            <a class="pink-anchor"
                               href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= quizId %>">
                                <%= quiz.getName()%>
                            </a>
                        </h2>
                        <p class="created-by">
                            Created by <a class="pink-anchor"
                                          href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= creatorId %>">
                            <%= creator.getUsername()%>
                        </a>
                        </p>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
        </div>
        <a class="see-more-button pink-button"
           href="${pageContext.request.contextPath}/HTML/HomePage/seeMorePopularQuizzes.jsp">See More</a>
    </div>

    <div class="box scrollable-box pink-box">
        <div class="box-title pink-text">Recently Created Quizzes</div>
        <div class="box-content">
            <ul class="quiz-list">
                <%
                    quizzes.sort(Comparator.comparing(Quiz::getCreationDateTime).reversed());

                    for (int i = 0; i < Math.min(quizzes.size(), 3); i++) {
                        Quiz quiz = quizzes.get(i);
                        int creatorId = quiz.getCreatorId();
                        int quizId = quiz.getId();
                        User creator = DatabaseManager.getUser(creatorId);
                %>
                <li class="quiz-item">
                    <div class="quiz-details">
                        <h2 class="quiz-name">
                            <span class="quiz-icon">&#x25ba </span>
                            <a class="pink-anchor"
                               href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= quizId %>">
                                <%= quiz.getName()%>
                            </a>
                        </h2>
                        <p class="created-by">
                            Created by <a class="pink-anchor"
                                          href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= creatorId %>">
                            <%= creator.getUsername()%>
                        </a>
                        </p>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
        </div>
        <a class="see-more-button pink-button"
           href="${pageContext.request.contextPath}/HTML/HomePage/seeMoreRecentlyCreatedQuizzes.jsp">See More</a>
    </div>

    <div class="box scrollable-box pink-box">
        <div class="box-title pink-text">Quizzes I Took</div>
        <div class="box-content">
            <ul class="quiz-list">
                <%
                    int userId = currentUser.getId();
                    List<Result> results = DatabaseManager.getAllQuizResultsByUserId(userId);

                    results.sort(Comparator.comparing(Result::getFinishTime).reversed());

                    for (int i = 0; i < Math.min(results.size(), 3); i++) {
                        Result result = results.get(i);
                        int quizId = result.getQuizId();
                        Quiz quiz = DatabaseManager.GetQuizById(quizId);
                        int creatorId = quiz.getCreatorId();
                        User creator = DatabaseManager.getUser(creatorId);
                %>
                <li class="quiz-item">
                    <div class="quiz-details">
                        <h2 class="quiz-name">
                            <span class="quiz-icon">&#x25ba </span>
                            <a class="pink-anchor"
                               href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= quizId %>">
                                <%= quiz.getName()%>
                            </a>
                        </h2>
                        <p class="created-by">
                            Created by <a class="pink-anchor"
                                          href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= creatorId %>">
                            <%= creator.getUsername()%>
                        </a>
                        </p>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
        </div>
        <a class="see-more-button pink-button"
           href="${pageContext.request.contextPath}/HTML/HomePage/seeMoreQuizzesITook.jsp">See
            More</a>
    </div>

    <div class="box scrollable-box pink-box">
        <div class="box-title pink-text">Quizzes I Created Recently</div>
        <div class="box-content">
            <ul class="quiz-list">
                <%
                    int currUserId = currentUser.getId();
                    List<Quiz> myQuizzes = DatabaseManager.getAllQuizzes();
                    myQuizzes.sort(Comparator.comparing(Quiz::getCreationDateTime).reversed());

                    List<Quiz> filteredQuizzes = myQuizzes.stream()
                            .filter(quiz -> quiz.getCreatorId() == currUserId)
                            .toList();

                    for (int i = 0; i < filteredQuizzes.size(); i++) {
                        Quiz quiz = filteredQuizzes.get(i);
                        int quizId = quiz.getId();
                %>
                <li class="quiz-item">
                    <div class="quiz-details">
                        <h2 class="quiz-name">
                            <span class="quiz-icon">&#x25ba </span>
                            <a class="pink-anchor"
                               href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= quizId %>">
                                <%= quiz.getName()%>
                            </a>
                        </h2>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
        </div>
        <a class="see-more-button pink-button"
           href="${pageContext.request.contextPath}/HTML/HomePage/seeMoreQuizzesICreatedRecently.jsp">See More</a>
    </div>
    <div class="box scrollable-box pink-box">
        <div class="box-title pink-text">My Achievements</div>
        <div class="box-content">
        </div>
        <a class="see-more-button pink-button"
           href="${pageContext.request.contextPath}/AchievementServlet">See
            More</a>
    </div>
    <div class="box pink-box">
        <div class="box-title pink-text">Search for Users</div>
        <div class="box-content">
            <form method="post" action="${pageContext.request.contextPath}/UserSearchServlet">
                <label for="searchField">Search Friends:</label>
                <input type="text" id="searchField" name="searchQuery">
                <button type="submit" class="find-button">Find</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>

