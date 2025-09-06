package Admin.Servlets;

import Database.DatabaseManager;
import Quiz.Models.QuizHistory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "QuizHistoryServlet", value = "/QuizHistoryServlet")
public class QuizHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quizId = Integer.valueOf(request.getParameter("quizId"));
        List<QuizHistory> results;
        try {
            results = DatabaseManager.getQuizHistory(quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getSession().setAttribute("quizHistory", results);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/Admin/QuizHistoryPage.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
