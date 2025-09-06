<%@ page import="Database.DatabaseManager" %>
<%@ page import="Quiz.Models.Question" %>
<%@ page import="Quiz.Models.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="javax.xml.stream.FactoryConfigurationError" %>
<%@ page import="QuizPage.Models.Result" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Summary Page</title>
    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
            justify-content: center;
            align-items: center;
            display: flex;
            height: auto;
        }

        .container {
            max-width: 800px;
            min-width: 700px;
            margin: 0 auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 30px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }


        header h1 {
            margin: 0;
            padding: 10px 0;
            font-size: 28px;
        }


        section {
            margin: 20px 0;
        }

        h2 {
            font-size: 20px;
            margin-bottom: 10px;
            margin-top: 30px;
            color: #e1275f;
        }


        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;

        }

        table th, table td {
            padding: 8px 12px;
            border: 1px solid #ddd;
            text-align: left;
        }


        a.btn-primary,
        a.btn-secondary {
            display: inline-block;
            padding: 10px 20px;
            margin-right: 10px;
            text-align: center;
            text-decoration: none;
            color: #fff;
            border-radius: 5px;
            cursor: pointer;
        }

        a.btn-primary {
            background-color: #007bff;
        }

        a.btn-secondary {
            background-color: #6c757d;
        }

        /* Links */
        a {
            color: #007bff;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        button.start-button {
            padding: 12px 24px;
            font-size: 18px;
            background-color: #DE3163;
            color: #ffffff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
            justify-content: center;
            align-items: center;
        }


        button.start-button:hover {
            background-color: #F33A6A;
        }

        .center-content {
            text-align: center;
            font-size: 24px;
        }
    </style>
    <%
        Quiz quiz = DatabaseManager.GetQuizById(Integer.valueOf(request.getParameter("id")));
        request.getSession().setAttribute("curQuiz", quiz);
        String quizSummary = quiz.getDescription();
        User creator = DatabaseManager.getUser(quiz.getCreatorId());
        String quizName = quiz.getName();
        int quizCreator = quiz.getCreatorId();
        User user = (User) request.getSession().getAttribute("user");
    %>
</head>
<body>

<div class="container">
    <header class="center-content">
        <h1><%=quizName%>
        </h1>
        <h2 style="font-size: 24px;">Quiz Summary Page</h2>
    </header>
    <section class="quiz-details">
        <h2>Quiz Description</h2>
        <p><%=quizSummary%>
        </p>
        <h2>Quiz Creator</h2>
        <%
            if (quizCreator == ((User) request.getSession().getAttribute("user")).getId()) {
        %>
        <p>Created by <a href="${pageContext.request.contextPath}/HTML/Homepage.jsp"><%=creator.getUsername()%>
        </a></p>
        <% } else { %>
        <p>Created by <a
                href="${pageContext.request.contextPath}/HTML/ViewProfile.jsp?userId=<%= creator.getId() %>"><%=creator.getUsername()%>
        </a></p>
        <% } %>
    </section>

    <section class="user-performance">
        <h2>Your Performance</h2>
        <label for="sortCriteria">Sort by:</label>
        <select id="sortCriteria">
            <option value="date">Date</option>
            <option value="percent">Percentage</option>
            <option value="time">Time Taken</option>
        </select>
        <table id="performanceTable">
            <thead>
            <tr>
                <th>Date</th>
                <th>Percentage</th>
                <th>Time Taken</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Result> performances = DatabaseManager.getUsersPastPerformances(user.getId(), quiz.getId());
                if (performances != null) {
                    for (Result performance : performances) {
            %>
            <tr>
                <td><%=performance.getFinishTime()%>
                </td>
                <td><%=performance.getPercent()%>
                </td>
                <td><%=performance.getDuration()%> minutes</td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
        <script>
            const sortCriteria = document.getElementById('sortCriteria');
            const performanceTable = document.getElementById('performanceTable').getElementsByTagName('tbody')[0];

            sortCriteria.addEventListener('change', () => {
                const criteria = sortCriteria.value;
                const rows = [...performanceTable.rows];

                rows.sort((row1, row2) => {
                    const value1 = row1.cells[criteria === 'date' ? 0 : criteria === 'percent' ? 1 : 2].textContent;
                    const value2 = row2.cells[criteria === 'date' ? 0 : criteria === 'percent' ? 1 : 2].textContent;

                    if (criteria === 'date') {
                        return new Date(value1) - new Date(value2);
                    } else {
                        return parseFloat(value1) - parseFloat(value2);
                    }
                });

                while (performanceTable.firstChild) {
                    performanceTable.removeChild(performanceTable.firstChild);
                }

                rows.forEach(row => performanceTable.appendChild(row));
            });
        </script>
    </section>

    <section class="leaderboards">
        <h2>Highest Performers</h2>
        <ul>
            <%
                List<Result> temp = DatabaseManager.getHighestPerformers(quiz.getId());
                if (temp == null) {
            %>
            <p>Nobody has written the quiz</p>
            <%} else {%>
            <%
                for (Result performance : temp) {
            %>
            <li><%=DatabaseManager.getUser(performance.getUserId()).getUsername()%> (<%=performance.getPercent()%>%)
            </li>
            <% }
            } %>
        </ul>
    </section>

    <section class="leaderboards">
        <h2>Highest Performers In The Last Day</h2>
        <ul>
            <%
                temp = DatabaseManager.getHighestPerformersLastDay(quiz.getId());
                if (temp == null) {
            %>
            <p>Nobody has written the quiz today</p>
            <%} else {%>
            <%
                for (Result performance : temp) {
            %>
            <li><%=DatabaseManager.getUser(performance.getUserId()).getUsername()%>
            </li>
            <% }
            } %>
        </ul>
    </section>

    <section class="leaderboards">
        <h2>Recent Results</h2>
        <ul>
            <%
                temp = DatabaseManager.getRecentResults(quiz.getId());
                if (temp == null) {
            %>
            <p>Nobody has written the quiz Recently</p>
            <%} else {%>
            <%
                for (Result performance : temp) {
            %>
            <li><%=DatabaseManager.getUser(performance.getUserId()).getUsername()%> (<%=performance.getPercent()%>%)
            </li>
            <% }
            } %>
        </ul>
    </section>

    <section class="summary-statistics">
        <h2>Summary Statistics</h2>
        <%
            double summary = DatabaseManager.getQuizAvarage(quiz.getId());
            if (summary == -1) {
        %>
        <p>No Summary Data</p>
        <%} else {%>
        <p>Overall Average: <%=DatabaseManager.getQuizAvarage(quiz.getId())%>%</p>
        <%
            }
        %>
    </section>
    <%
        if (quizCreator == ((User) request.getSession().getAttribute("user")).getId()) {
    %>
    <script>
        alert("You are the creator of the quiz. You cannot take it.");
    </script>
    <section class="actions">
        <a href="${pageContext.request.contextPath}/HTML/Homepage.jsp">Go to Home Page</a>
    </section>
    <%
    } else {
    %>
    <form action="${pageContext.request.contextPath}/QuizFrontPageServlet" method="post">
        <section class="actions">
            <button type="submit" class="start-button">Start Quiz</button>
        </section>
    </form>
    <% } %>
</div>
</body>
</html>
