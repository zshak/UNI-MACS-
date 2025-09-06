package Achievements.Servlets;

import Achievements.Models.Achievement;
import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AchievementServlet", value = "/AchievementServlet")
public class AchievementServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var session = request.getSession(true);
        var user = (User)session.getAttribute("user");

        List<Achievement> achievements = DatabaseManager.getUserAchievements(user);
        request.setAttribute("achievements", achievements);

        RequestDispatcher dis = request.getRequestDispatcher("/HTML/Achievements.jsp");
        dis.forward(request, response);
    }
}
