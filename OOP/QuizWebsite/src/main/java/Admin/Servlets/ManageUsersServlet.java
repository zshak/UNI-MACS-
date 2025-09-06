package Admin.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import Quiz.Models.Quiz;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ManageUsersServlet", value = "/ManageUsersServlet")
public class ManageUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = null;
        try {
            users = DatabaseManager.GetAllNonAdminUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.getSession().setAttribute("users", users);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/Admin/UsersPage.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
