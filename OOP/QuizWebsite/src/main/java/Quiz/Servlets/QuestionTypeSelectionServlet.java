package Quiz.Servlets;

import Quiz.Models.Question;
import Quiz.Models.QuestionFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/QuestionTypeSelectionServlet")
public class QuestionTypeSelectionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request);
        String questionType = request.getParameter("questionType");
        Question curQuestion = QuestionFactory.createQuestion(questionType);
        request.getSession().setAttribute("question", curQuestion);
        request.getRequestDispatcher("/HTML/AddQuestions.jsp").forward(request, response);
    }

}
