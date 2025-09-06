package Quiz.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import Quiz.Models.Quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(name = "QuizCreationServlet", value = "/QuizCreationServlet")
public class QuizCreationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int userId = user.getId();
        String title = (String) request.getParameter("quizTitle");
        String description = (String) request.getParameter("quizDescription");
        boolean hasRandomQuestions = request.getParameter("randomQuestions") != null;
        boolean isOnePage = request.getParameter("isOnePage") != null;
        boolean isImmediateCorrection = request.getParameter("isImmediateCorrection") != null;
        boolean hasPracticeMode = request.getParameter("hasPracticeMode") != null;

        Quiz createdQuiz = new Quiz(title, description, userId, hasRandomQuestions, isOnePage, isImmediateCorrection, hasPracticeMode, new Timestamp(System.currentTimeMillis()), 0);

        request.getSession().setAttribute("quiz", createdQuiz);

        request.getRequestDispatcher("/HTML/QuestionsGenerator.jsp").forward(request, response);
    }
}
