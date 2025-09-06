package Quiz.Models;

public class MultipleChoiceAnswer extends Answer{
    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    private boolean isTrue;

    public MultipleChoiceAnswer(int questionId, String content, boolean isTrue) {
        super(questionId, content);
        this.isTrue = isTrue;
    }

    public MultipleChoiceAnswer(int id, int questionId, String content) {
        super(id, questionId, content);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + super.getId() +
                ", questionId=" + super.getQuestionId() +
                ", content='" + super.getContent() +
                ", isTrue=" + isTrue + '\'' +
                 '}';

    }

}
