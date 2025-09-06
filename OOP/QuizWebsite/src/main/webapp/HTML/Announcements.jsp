<%@ page import="Admin.Models.AnnouncementUserModel" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="CommonJsp.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Announcements</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }

        h1, h2 {
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #ff69b4;
            color: white;
        }

        tr:hover {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h1>Quiz History</h1>

<table>
    <tr>
        <th>Username</th>
        <th>Announcement</th>
    </tr>

    <% for (AnnouncementUserModel announcement : (List<AnnouncementUserModel>) request.getSession().getAttribute("announcements")) { %>
    <tr>
        <td><%= announcement.getAdminUsername() %>
        </td>
        <td><%= announcement.getAnnounement() %>
        </td>
    </tr>
    <% } %>
</table>
</body>
</html>
