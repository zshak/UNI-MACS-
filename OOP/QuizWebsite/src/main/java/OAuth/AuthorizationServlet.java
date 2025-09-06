package OAuth;

import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "login", value = "/HTML/login")
public class AuthorizationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        var session = req.getSession(true);
        try {
            User currentUser = DatabaseManager.userIsRegistered(username, password);
            RequestDispatcher dis;
            if (currentUser != null) {
                dis = req.getRequestDispatcher("/HTML/Homepage.jsp");
                session.setAttribute("user", currentUser);
                session.setAttribute("is_admin", DatabaseManager.getUserAdminStatus(currentUser));
            } else {
                dis = req.getRequestDispatcher("/HTML/TryAgainAuthorization.jsp");
            }

            dis.forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}