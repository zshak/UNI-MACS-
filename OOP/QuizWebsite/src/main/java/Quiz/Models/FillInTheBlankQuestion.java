package Quiz.Models;

import Quiz.Models.enums.questionType;

import java.util.List;

public class FillInTheBlankQuestion extends MultipleAnswerQuestion{
    public FillInTheBlankQuestion() {
    }

    public FillInTheBlankQuestion(int id, String content, int quizId, int type) {
        super(id, content, quizId, type);
    }

    @Override
    public questionType getType() {
        return questionType.Fill_In_The_Blank;
    }

}
