package Quiz.Servlets;

import Database.DatabaseManager;
import Quiz.Models.*;
import Quiz.Models.enums.questionType;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "AnswerSelectionServlet", value = "/AnswerSelectionServlet")
public class AnswerSelectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Question q = (Question) request.getSession().getAttribute("question");
        String st = request.getParameter("questionDescription");
        String questionDescription = QuestionDescriptionFactory.getDescription(q.getType(),request);
        q.setContent(questionDescription);


        List<List<Object>> userInputs = (List<List<Object>>) request.getSession().getAttribute("userInputs");
        if(userInputs == null)
            userInputs = new ArrayList<>();
        userInputs.add(Arrays.asList(q, request.getParameter("chosenOption"), request.getParameterValues("optionsArray"), request.getParameterValues("strings[]")));
        request.getSession().setAttribute("userInputs", userInputs);
        request.getRequestDispatcher("/HTML/QuestionsGenerator.jsp").forward(request, response);
    }

}
