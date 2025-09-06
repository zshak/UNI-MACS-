package Quiz.Models;

public abstract class Answer implements Comparable<Answer>{
    private int id;
    private int questionId;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Answer(int id, int questionId, String content) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
    }

    public Answer(int questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }
    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                '}';
    }
    @Override
    public int compareTo(Answer o) {
        int intCompare = Integer.compare(this.getId(), o.getId());
        if(intCompare != 0) {
            return intCompare;
        }
        return String.CASE_INSENSITIVE_ORDER.compare(o.getContent(), this.getContent());
    }
}
