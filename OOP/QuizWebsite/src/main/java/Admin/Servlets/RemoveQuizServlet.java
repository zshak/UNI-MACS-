package Admin.Servlets;

import Database.DatabaseManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RemoveQuizServlet", value = "/RemoveQuizServlet")
public class RemoveQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        try {
            DatabaseManager.DeleteQuiz(quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("ManageQuizzesServlet");

    }
}
