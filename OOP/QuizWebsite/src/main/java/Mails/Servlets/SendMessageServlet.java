package Mails.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "SendMessageServlet", value = "/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        int toUserId = Integer.parseInt(request.getParameter("recipientId"));
        Date currentDateTime = new Date(System.currentTimeMillis());
        String content = request.getParameter("message");
        try {
            DatabaseManager.SendMail(currentUser.getId(),toUserId,"NOTE","New Note",content,currentDateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/Friends.jsp");
        dispatcher.forward(request, response);
    }
}
