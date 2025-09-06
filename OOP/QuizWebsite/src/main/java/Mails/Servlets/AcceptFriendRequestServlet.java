package Mails.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AcceptFriendRequestServlet", value = "/AcceptFriendRequestServlet")
public class AcceptFriendRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("referer");
        User currentUser = (User) request.getSession().getAttribute("user");
        int mailId = Integer.parseInt(request.getParameter("mailId"));
        int fromId = Integer.parseInt(request.getParameter("fromId"));
        try {
            DatabaseManager.MakeFriends(currentUser.getId(),fromId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            DatabaseManager.DeleteMail(mailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (referer != null && referer.contains("http://localhost:8081/QuizWebsite_war_exploded/HTML/notifications.jsp")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/notifications.jsp");
           dispatcher.forward(request, response);
        } else if (referer != null && referer.contains("/HTML/ViewProfile.jsp")) {
            String url = "/HTML/ViewProfile.jsp?userId=" + currentUser.getId();
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        }else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/notifications.jsp");
            dispatcher.forward(request, response);
        }

    }
}
