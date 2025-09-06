package Quiz.Models;

import java.util.ArrayList;
import java.util.List;

public abstract class MultipleAnswerQuestion extends Question{
    List<MultipleChoiceAnswer> answers;


    public MultipleAnswerQuestion(int quizId) {
        super(quizId);
    }


    public MultipleAnswerQuestion(int id, String content, int quizId, int type) {
        super(id, content, quizId, type);
    }

    public MultipleAnswerQuestion() {}

    public void AddAnswers(List<MultipleChoiceAnswer> answer){
        answers = answer;
    }
    @Override
    public String getCreatingAnswersHtml() {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<h1 class=\"option-heading\">Input Strings</h1>");
        htmlBuilder.append("<label class=\"option-label\">Enter Answers:</label>");
        htmlBuilder.append("<div id=\"stringContainer\">");
        htmlBuilder.append("<input type=\"text\" name=\"strings[]\" placeholder=\"Enter An Answer\"><br>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<button type=\"button\" id=\"addString\">Add Answer</button><br>");
        htmlBuilder.append("<script>");
        htmlBuilder.append("document.getElementById(\"addString\").addEventListener(\"click\", function() {");
        htmlBuilder.append("var stringContainer = document.getElementById(\"stringContainer\");");
        htmlBuilder.append("var input = document.createElement(\"input\");");
        htmlBuilder.append("input.type = \"text\";");
        htmlBuilder.append("input.name = \"strings[]\";");
        htmlBuilder.append("input.placeholder = \"Enter a string\";");
        htmlBuilder.append("stringContainer.appendChild(input);");
        htmlBuilder.append("stringContainer.appendChild(document.createElement(\"br\"));");
        htmlBuilder.append("});");
        htmlBuilder.append("</script>");
        return htmlBuilder.toString();
    }

    @Override
    public boolean isAnswerCorrect(String answer) {
        return answers.stream().anyMatch(x -> x.getContent().equals(answer));
    }

    @Override
    public void AddAnswer(Answer answer) {
        answers.add((MultipleChoiceAnswer) answer);
    }
}
