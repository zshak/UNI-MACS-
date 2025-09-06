package Quiz.Servlets;

import Database.DatabaseManager;
import Quiz.Models.*;
import Quiz.Models.enums.questionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "SubmitQuizServlet", value = "/SubmitQuizServlet")
public class SubmitQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<List<Object>> userInputs = (List<List<Object>>) request.getSession().getAttribute("userInputs");
        if (userInputs == null)
            userInputs = new ArrayList<>();
        Question q = (Question) request.getSession().getAttribute("question");
        String st = request.getParameter("questionDescription");
        String questionDescription = QuestionDescriptionFactory.getDescription(q.getType(), request);
        q.setContent(questionDescription);
        userInputs.add(Arrays.asList(q, request.getParameter("chosenOption"), request.getParameterValues("optionsArray"), request.getParameterValues("strings[]")));

        Quiz quiz;
        try {
            quiz = DatabaseManager.AddQuiz((Quiz) request.getSession().getAttribute("quiz"));
            DatabaseManager.updateCreationDateAndTime(quiz.getId());
            DatabaseManager.addAchievementToUser(quiz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < userInputs.size(); i++) {
            Question question = (Question) userInputs.get(i).get(0);
            try {
                int id = DatabaseManager.InsertQuestion(question, quiz.getId());
                question.setId(id);
                CallInsertingAnswers(question, userInputs.get(i));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        request.getSession().removeAttribute("userInputs");
        request.getSession().removeAttribute("quiz");
        request.getSession().removeAttribute("question");
      
        request.setAttribute("user", request.getSession().getAttribute("user"));
        request.getRequestDispatcher("/HTML/Homepage.jsp").forward(request, response);
    }

    private void CallInsertingAnswers(Question q, List<Object> inputs) throws SQLException {
        if (q.getType() == questionType.Multiple_Choice) {
            String chosenOption = (String) inputs.get(1);
            String[] inputtedOptions = (String[]) inputs.get(2);
            String answers = inputtedOptions[0];
            inputtedOptions = answers.split(",");
            for (int i = 0; i < inputtedOptions.length; i++) {
                MultipleChoiceAnswer answer = new MultipleChoiceAnswer(q.getId(), inputtedOptions[i], chosenOption.equals(inputtedOptions[i]));
                DatabaseManager.InsertMultipleChoiceAnswer(answer, q.getId());
            }
        } else {
            String[] inputArray = (String[]) inputs.get(3);
            for (int i = 0; i < inputArray.length; i++) {
                CorrectAnswers answers = new CorrectAnswers(q.getId(), inputArray[i]);
                DatabaseManager.InsertCorrectAnswer(answers, q.getId());
            }
        }
    }
}
