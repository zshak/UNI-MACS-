
package QuizPage.Servlets;

import Quiz.Models.Question;
import Quiz.Models.Quiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SinglePageServlet", value = "/SinglePageServlet")
public class SinglePageServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long endTime = System.currentTimeMillis();
        Quiz quiz = (Quiz) request.getSession().getAttribute("curQuiz");
        List<Question> questions = (List<Question>) request.getSession().getAttribute("curQuestionsList");

        int score = 0;
        for(int i = 0; i < questions.size(); i++){
            String answered = request.getParameter("response" + questions.get(i).getId());
            boolean isCorrectAnswer = questions.get(i).isAnswerCorrect(answered);
            if (isCorrectAnswer){
                score++;
            }
        }
        long startTime = (long)request.getSession().getAttribute("startTime");
        request.getSession().setAttribute("timeNeeded", (int)((endTime - startTime) /60000));
        request.getSession().setAttribute("curScore", score);
        request.getRequestDispatcher("/HTML/QuizPage/QuizResultDisplay.jsp").forward(request, response);
    }
    }


