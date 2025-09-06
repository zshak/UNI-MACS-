package HomePage;

import Database.DatabaseManager;
import OAuth.Models.User;
import Quiz.Models.Quiz;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "RecentlyCreatedQuizzesByUserServlet", value = "/RecentlyCreatedQuizzesByUserServlet")
public class RecentlyCreatedQuizzesByUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currUser = (User) request.getAttribute("user");
        int currUserId = currUser.getId();
        List<Quiz> quizzes = DatabaseManager.getAllQuizzes();
        quizzes.sort(Comparator.comparing(Quiz::getCreationDateTime).reversed());

        List<Quiz> filteredQuizzes = quizzes.stream()
                .filter(quiz -> quiz.getCreatorId() == currUserId)
                .toList();

        request.setAttribute("recentQuizzesByUser", filteredQuizzes);
        RequestDispatcher dis = request.getRequestDispatcher("/HTML/HomePage/seeMoreQuizzesICreatedRecently.jsp");
        dis.forward(request, response);
    }
}
