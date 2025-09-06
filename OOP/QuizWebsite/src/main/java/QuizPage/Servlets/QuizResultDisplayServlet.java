package QuizPage.Servlets;

import Database.DatabaseManager;
import OAuth.Models.User;
import Quiz.Models.Quiz;
import QuizPage.Models.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet(name = "QuizResultDisplayServlet", value = "/QuizResultDisplayServlet")
public class QuizResultDisplayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Timestamp finishTime = (Timestamp) request.getSession().getAttribute("finishTime");
        int userId = ((User)request.getSession().getAttribute("user")).getId();
        int quizId  = (int) ((Quiz)request.getSession().getAttribute("curQuiz")).getId();
        int score = (int) request.getSession().getAttribute("curScore");
        int percent = (int) request.getSession().getAttribute("resultPercent");
        int duration = (int) request.getSession().getAttribute("timeNeeded");
        Result result = new Result(userId, quizId, score, percent, duration, finishTime);
        try {
            DatabaseManager.insertQuizResult(result);
            DatabaseManager.addQuizMachineAchievement(userId);
            Quiz quiz = DatabaseManager.GetQuizById(result.getQuizId());
            int submissionCount = quiz.getSubmissionCount();
            DatabaseManager.updateSubmissionCount(quiz.getId(), submissionCount + 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("/HTML/Homepage.jsp").forward(request, response);
    }

}