package Quiz.Models;

import Quiz.Models.enums.questionType;

public abstract class Question {
    private int id;
    private String content;
    private int quizId;
    private int typeId;

    public Question(){}
    public Question(int quizId) {
        this.quizId = quizId;
    }
    public Question(int id, String content, int quizId) {
        this.id = id;
        this.content = content;
        this.quizId = quizId;
    }

    public  Question(int id, String content, int quizId, int typeId){
        this.id = id;
        this.content = content;
        this.quizId = quizId;
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
    public int getTypeId() {
        return typeId;
    }

    public abstract questionType getType();
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", content=" + content +
                ", quizId=" + quizId +
                '}';
    }

    public String getCreatingDescriptionHtml(){
        String html = "<div class=\"question-description\">" +
                "<label class=\"question-label\" for='questionDescription'>Question Description:</label><br>" +
                "<textarea id='questionDescription' name='questionDescription' rows='4' cols='50'></textarea><br><br>" +
                "</div>";
        return html;
    }

    public abstract String getCreatingAnswersHtml();
    public String getCreatingHtml(){
        return "<div class=\"question\">" +
                getCreatingDescriptionHtml() +
                "<div class=\"select-option\">" +
                getCreatingAnswersHtml() +
                "</div>" +
                "</div>";
    }
    public abstract boolean isAnswerCorrect(String answer);
    public abstract void AddAnswer(Answer answer);

}
