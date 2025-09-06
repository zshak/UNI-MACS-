package QuizPage.Servlets;

import Database.DatabaseManager;
import Quiz.Models.Question;
import Quiz.Models.Quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@WebServlet(name = "QuizFrontPageServlet", value = "/QuizFrontPageServlet")
public class QuizFrontPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Quiz quiz = (Quiz) request.getSession().getAttribute("curQuiz");
        List<Question> questions;
        try {
            questions = DatabaseManager.getQuestionById(quiz.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean hasRandomQuestions = quiz.hasRandomQuestions();
        boolean isOnePage = quiz.isOnePage();
        boolean isImmediateCorrection = quiz.isImmediateCorrection();
        boolean hasPracticeMode = quiz.hasPracticeMode();
        if (hasRandomQuestions){
            shuffleQuestions(questions);
        }
        request.getSession().setAttribute("curQuestionsList", questions); // set Questions attribute
        int score =  0;
        request.getSession().setAttribute("curScore", score);
        long startTime = System.currentTimeMillis();
        request.getSession().setAttribute("startTime", startTime);
        if (isOnePage){
            request.getRequestDispatcher("/HTML/QuizPage/OnePageQuiz.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("curQuestion", questions.get(0));
            if (isImmediateCorrection){
                request.getRequestDispatcher("/HTML/QuizPage/MultiplePageImmediateCorrectionQuiz.jsp").forward(request, response);
            }
            request.getRequestDispatcher("/HTML/QuizPage/MultiplePageQuiz.jsp").forward(request, response);
        }
    }


    // Fisher-Yates shuffle algorithm
    private void shuffleQuestions(List<Question> questions) {
        Random rand = new Random();
        for (int i = questions.size() - 1; i > 0; i--){
            int nextPos = rand.nextInt(i + 1);
            Question tmp = questions.get(i);
            questions.set(i, questions.get(nextPos));
            questions.set(nextPos, tmp);
        }
    }
}
