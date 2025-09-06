package Admin.Servlets;

import Database.DatabaseManager;
import Quiz.Models.Quiz;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ManageQuizzesServlet", value = "/ManageQuizzesServlet")
public class ManageQuizzesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Quiz> quizzes = null;
        try {
            quizzes = DatabaseManager.getAllQuizes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.getSession().setAttribute("quizzes", quizzes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/Admin/QuizzesPage.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
