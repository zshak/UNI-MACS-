package QuizPage.Models;

import Quiz.Models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetrieve {


    public static List<Question> queryQuestions(int quizId, Connection connection) throws SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM questions WHERE quiz_id = ?;");
        st.setInt(1, quizId);
        ResultSet rs = st.executeQuery();
        List<Question> questions = new ArrayList<>();
        if (!rs.next()) {
            return null; // No records found
        } else {
            do {
                int questionType = rs.getInt(3);
                int id = rs.getInt(1);
                int questionId = rs.getInt(2);
                Question tmp = QuestionFactory.createQuestionById(questionType, id, rs.getString(4), questionId);
                questions.add(tmp);
            } while (rs.next());
        }
        return questions;
    }

    public static List<MultipleChoiceAnswer> queryAnswers(Question question, Connection connection) throws SQLException {
        int questionId = question.getId();
        PreparedStatement stAnswers = connection.prepareStatement("SELECT * FROM answers WHERE question_id = ?;");
        stAnswers.setInt(1, questionId);
        ResultSet rsAnswers = stAnswers.executeQuery();
        List<MultipleChoiceAnswer> answers = new ArrayList<>();
        if (!rsAnswers.next()) {
            return null; // No records found
        } else {
            do {
                MultipleChoiceAnswer tmp = new MultipleChoiceAnswer(rsAnswers.getInt(2), rsAnswers.getInt(1), rsAnswers.getString(3));
                if (question.getTypeId() == 2 && rsAnswers.getBoolean(4)){
                    tmp.setTrue(true);
                    answers.add(tmp);
                } else {
                    answers.add(tmp);
                }
            }while (rsAnswers.next());
        }
        return answers;
    }

    public static void appendAnswersToQuestion(Question q, List<MultipleChoiceAnswer> answers){
        if (q.getTypeId() == 0){
            ((QuestionResponseQuestion)q).AddAnswers(answers);
        } else if (q.getTypeId() == 1){
            ((FillInTheBlankQuestion)q).AddAnswers(answers);
        } else if (q.getTypeId() == 2){
            ((MultipleChoiceQuestion)q).AddAnswers(answers);
            for (MultipleChoiceAnswer a: answers){
                if (a.isTrue()){
                    q.AddAnswer(a);
                }
            }
        } else if (q.getTypeId() == 3){
            ((PictureResponseQuestion)q).AddAnswers(answers);
        }
    }
}
