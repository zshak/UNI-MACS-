package QuizPage.Servlets;

import Quiz.Models.Question;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CorrectionPageServlet", value = "/CorrectionPageServlet")
public class CorrectionPageServlet extends HttpServlet {
    private int questionIndex = 0;
    private String feedback;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Question> questions = (List<Question>) request.getSession().getAttribute("curQuestionsList");
        String answered = request.getParameter("response" + questions.get(questionIndex).getId());
        System.out.println(answered);
        boolean isCorrectAnswer = questions.get(questionIndex).isAnswerCorrect(answered);
        if (isCorrectAnswer){
            int score = (int) request.getSession().getAttribute("curScore");
            request.getSession().setAttribute("curScore", ++score);
            feedback = "Correct Answer!";
        } else {
            feedback = "Wrong Answer :(";
        }
        questionIndex++;
        if(questionIndex == questions.size()){
            questionIndex = 0;
        }
        request.getSession().setAttribute("curFeedback", feedback);
        request.getRequestDispatcher("/HTML/QuizPage/CorrectionPage.jsp").forward(request, response);
    }

}