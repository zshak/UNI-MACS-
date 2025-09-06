package Admin.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CreateAnnouncementServlet", value = "/CreateAnnouncementServlet")
public class CreateAnnouncementServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String announcement = request.getParameter("announcement");
        request.getSession().setAttribute("user", new User(1, "admini"));
        try {
            DatabaseManager.addAnnouncement(announcement, ((User)request.getSession().getAttribute("user")).getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/Admin/AdminJsp.jsp");
        dispatcher.forward(request, response);

    }
}
