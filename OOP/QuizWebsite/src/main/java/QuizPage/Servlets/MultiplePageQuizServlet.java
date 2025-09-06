package QuizPage.Servlets;

import Quiz.Models.Question;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MultiplePageQuizServlet", value = "/MultiplePageQuizServlet")
public class MultiplePageQuizServlet extends HttpServlet {

    private int questionIndex = 0;

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
        }
        questionIndex++;
        if(questionIndex < questions.size()){
            request.getSession().setAttribute("curQuestion", questions.get(questionIndex));
            request.getRequestDispatcher("/HTML/QuizPage/MultiplePageQuiz.jsp").forward(request, response);
        } else {
            long endTime = System.currentTimeMillis();
            questionIndex = 0;
            long startTime = (long)request.getSession().getAttribute("startTime");
            request.getSession().setAttribute("timeNeeded", (int)((endTime - startTime) /60000));
            request.getRequestDispatcher("/HTML/QuizPage/QuizResultDisplay.jsp").forward(request, response);
        }
    }

}