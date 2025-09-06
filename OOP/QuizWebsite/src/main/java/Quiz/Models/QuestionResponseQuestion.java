package Quiz.Models;

import Quiz.Models.enums.questionType;

import java.util.List;

public class QuestionResponseQuestion extends MultipleAnswerQuestion{
    public QuestionResponseQuestion(){
        super();
    }

    public QuestionResponseQuestion(int quizId) {
        super(quizId);
    }

    public QuestionResponseQuestion(int id, String content, int quizId, int type) {
        super(id, content, quizId, type);
    }
    @Override
    public questionType getType() {
        return questionType.Question_Response;
    }

}
