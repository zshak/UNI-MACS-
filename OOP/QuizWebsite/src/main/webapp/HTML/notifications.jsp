<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Mails.Mail" %>
<%@ page import="OAuth.Models.User" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="Database.DatabaseManager" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="CommonJsp.jsp" %>
<% User currentUser = (User) session.getAttribute("user");
    List<Mail> mails = null;
    try {
        mails = DatabaseManager.GetUserNotifications(currentUser.getId());
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<html>
<head>
    <title>Mails</title>
</head>
<body>
<div class="d-flex flex-column justify-content-center w-100 h-100"></div>
<div class="form">
    <h1>Your Mails</h1>
    <table class="topscorers">
        <tr>
            <th>Title</th>
            <th>Content</th>
            <th>From</th>
            <th>Date</th>
        </tr>
        <%
            for (Mail mail : mails) {
                String type = mail.getType();
        %>
        <tr id="<%= mail.getId() %>">
            <td><%= mail.getTitle() %>
            </td>
            <td>
                <% if ("NOTE".equals(type)) { %>
                <%= mail.getContent() %>
                <% } else if ("CH".equals(type)) { %>
                You Have Been Challenged: <a
                    href="${pageContext.request.contextPath}/HTML/QuizPage/QuizFrontPage.jsp?id=<%= Integer.parseInt(mail.getContent()) %>"><%= mail.getTitle() %>
            </a>
                <% } else if ("FRT".equals(type)) { %>
                <form method="post" action="${pageContext.request.contextPath}/AcceptFriendRequestServlet">
                    <input type="hidden" name="mailId" value="<%= mail.getId() %>">
                    <input type="hidden" name="fromId" value="<%= mail.getFromId() %>">
                    <button type="submit" class="action-button yes-button">Yes</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/RemoveFriendRequestServlet">
                    <input type="hidden" name="mailId" value="<%= mail.getId() %>">
                    <input type="hidden" name="fromId" value="<%= mail.getFromId() %>">
                    <button type="submit" class="action-button no-button">No</button>
                </form>
                <% } %>
            </td>
            <td><%= DatabaseManager.getUser(mail.getFromId()).getUsername() %>
            </td>
            <td><%= mail.getDate().toString() %>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
</body>
</html>
<style>
    input {
        font-family: Arial, Helvetica, sans-serif;
        border: 2px solid gray;
        border-radius: 5px;
        display: inline-block;
        float: left;
    }

    .action-button {
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .yes-button {
        background-color: #4CAF50;
        color: white;
    }

    .no-button {
        background-color: #E57373;
        color: white;
    }

    .button1 {
        background-color: #4CAF50; /* Green */
        border: none;
        color: white;
        padding: 15px 32px;
        text-align: center;
        text-decoration: none;
        margin-top: 10px;
        display: inline-block;
        font-size: 16px;
        border-radius: 8px;
        transition-duration: 0.4s;
    }

    .button1:hover {
        box-shadow: 0 12px 16px 0 rgba(0, 0, 0, 0.24), 0 17px 50px 0 rgba(0, 0, 0, 0.19);
    }

    .tabs {
        background-color: orange; /* Green */
        border: none;
        color: white;
        padding: 7px 16px;
        text-align: center;
        text-decoration: none;
        margin-top: 10px;
        margin-bottom: 15px;
        margin-left: 10px;
        display: inline-block;
        font-size: 15px;
        border-radius: 8px;
        transition-duration: 0.4s;
    }

    .tabs:hover {
        box-shadow: 0 6px 8px 0 rgba(0, 0, 0, 0.24), 0 9px 25px 0 rgba(0, 0, 0, 0.19);
    }

    div.form {
        font-family: Arial, Helvetica, sans-serif;
        display: block;
        text-align: center;
    }

    form {
        display: inline-block;
        margin-left: auto;
        margin-right: auto;
        text-align: left;
    }

    body {
        background: linear-gradient(-45deg, #fdb8a0, #f5a0bf, #7ab7d0, #88d2c0);
        background-size: 400% 400%;
        animation: gradient 15s ease infinite;
        height: 100vh;
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

    .topscorers {
        font-family: Arial, Helvetica, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }

    .topscorers td, #topscorers th {
        border: 1px solid #ddd;
        padding: 8px;
    }

    .topscorers tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .topscorers tr:hover {
        background-color: #ddd;
    }

    .topscorers th {
        padding-top: 12px;
        padding-bottom: 12px;
        text-align: left;
        background-color: #04AA6D;
        color: white;
    }

    .container {
        display: block;
        position: relative;
        padding-left: 35px;
        margin-bottom: 12px;
        cursor: pointer;
        font-size: 22px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    /* Hide the browser's default checkbox */
    .container input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
        height: 0;
        width: 0;
    }


    .checkmark {
        position: absolute;
        top: 0;
        left: 0;
        height: 25px;
        width: 25px;
        background-color: #eee;
    }


    .container:hover input ~ .checkmark {
        background-color: #ccc;
    }


    .container input:checked ~ .checkmark {
        background-color: #2196F3;
    }


    .checkmark:after {
        content: "";
        position: absolute;
        display: none;
    }


    .container input:checked ~ .checkmark:after {
        display: block;
    }


    .container .checkmark:after {
        left: 9px;
        top: 5px;
        width: 5px;
        height: 10px;
        border: solid white;
        border-width: 0 3px 3px 0;
        -webkit-transform: rotate(45deg);
        -ms-transform: rotate(45deg);
        transform: rotate(45deg);
    }

    #div2 {
        display: none;
    }

    #div3 {
        display: none;
    }

    #div4 {
        display: none;
    }

    #div5 {
        display: none;
    }

    #div6 {
        display: none;
    }

    #div7 {
        display: none;
    }

</style>