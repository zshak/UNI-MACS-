package Mails.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import Quiz.Models.Quiz;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "ChallengeFriend", value = "/ChallengeFriend")
public class ChallengeFriend extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("referer");
        User currentUser = (User) request.getSession().getAttribute("user");
        int toUserId = Integer.parseInt(request.getParameter("friendId"));
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        Quiz quiz = null;
        try {
            quiz = DatabaseManager.GetQuizById(quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Date currentDateTime = new Date(System.currentTimeMillis());
        try {
            DatabaseManager.SendMail(currentUser.getId(),toUserId,"CH",quiz.getName(),Integer.toString(quizId),currentDateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (referer != null && referer.contains("/HTML/Challenge.jsp")) {
            String url = "/HTML/Challenge.jsp?userId=" + currentUser.getId();
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        }else{
            String url = "/HTML/Challenge.jsp?userId=" + currentUser.getId();
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        }

    }
}
