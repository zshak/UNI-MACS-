package Quiz.Models;

import Quiz.Models.enums.questionType;

import java.util.List;

public class MultipleChoiceQuestion extends SingleAnswerQuestion{

    List<MultipleChoiceAnswer> possibleAnswers;
    public MultipleChoiceQuestion() {
    }

    public MultipleChoiceQuestion(int id, String content, int quizId, int type) {
        super(id, content, quizId, type);
    }

    public void AddAnswers(List<MultipleChoiceAnswer> ans){
        possibleAnswers = ans;
    }

    public List<MultipleChoiceAnswer> getPossibleAnswers(){
        return possibleAnswers;
    }

    @Override
    public questionType getType() {
        return questionType.Multiple_Choice;
    }

}
