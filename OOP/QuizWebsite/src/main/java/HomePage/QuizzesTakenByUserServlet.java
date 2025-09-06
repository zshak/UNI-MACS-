package HomePage;

import Database.DatabaseManager;
import OAuth.Models.User;
import Quiz.Models.Quiz;
import QuizPage.Models.Result;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "QuizzesTakenByUserServlet", value = "/QuizzesTakenByUserServlet")
public class QuizzesTakenByUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currUser = (User) request.getAttribute("user");
        int userId = currUser.getId();
        List<Result> results = DatabaseManager.getAllQuizResultsByUserId(userId);
        List<Quiz> quizzes = null;

        results.sort(Comparator.comparing(Result::getFinishTime).reversed());

        for (Result result : results) {
            int quizId = result.getQuizId();
            Quiz quiz;
            try {
                quiz = DatabaseManager.GetQuizById(quizId);
                quizzes.add(quiz);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        request.setAttribute("quizzesITook", quizzes);
        RequestDispatcher dis = request.getRequestDispatcher("/HTML/HomePage/seeMoreQuizzesITook.jsp");
        dis.forward(request, response);
    }
}
