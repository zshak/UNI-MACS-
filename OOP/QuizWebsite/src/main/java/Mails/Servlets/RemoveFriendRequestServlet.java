package Mails.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import com.mysql.cj.Session;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RemoveFriendRequestServlet", value = "/RemoveFriendRequestServlet")
public class RemoveFriendRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("referer");
        User user = (User)request.getSession().getAttribute("user");
        int mailId = Integer.parseInt(request.getParameter("mailId"));
        try{
            DatabaseManager.DeleteMail(mailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (referer != null && referer.contains("/HTML/notifications.jsp")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/notifications.jsp");
            dispatcher.forward(request, response);
        } else if (referer != null && referer.contains("/HTML/ViewProfile.jsp")) {
            String url = "/HTML/ViewProfile.jsp?userId=" + user.getId();
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        }else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/notifications.jsp");
            dispatcher.forward(request, response);
        }
    }
}
