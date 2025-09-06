package Quiz.Models;

public class CorrectAnswers extends Answer{
    public CorrectAnswers(int questionId, String content) {
        super(questionId, content);
    }

    public CorrectAnswers(int id, int questionId, String content) {
        super(id, questionId, content);
    }

}
