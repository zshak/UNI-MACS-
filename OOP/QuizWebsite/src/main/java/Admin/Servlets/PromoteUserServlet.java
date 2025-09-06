package Admin.Servlets;

import Database.DatabaseConstant;
import Database.DatabaseManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "PromoteUserServlet", value = "/PromoteUserServlet")
public class PromoteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        try {
            DatabaseManager.promoteUser(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect("ManageUsersServlet");
    }
}
