package Admin.Servlets;

import Admin.Models.AnnouncementUserModel;
import Database.DatabaseManager;
import Quiz.Models.QuizHistory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ShowAnnouncementsServlet", value = "/ShowAnnouncementsServlet")
public class ShowAnnouncementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<AnnouncementUserModel> announcemets;
        try {
            announcemets = DatabaseManager.getAnnouncements();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getSession().setAttribute("announcements", announcemets);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/Announcements.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
