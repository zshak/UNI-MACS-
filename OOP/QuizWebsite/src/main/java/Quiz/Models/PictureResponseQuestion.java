package Quiz.Models;

import Quiz.Models.enums.questionType;

import java.util.List;

public class PictureResponseQuestion extends MultipleAnswerQuestion{
    public PictureResponseQuestion() {
    }

    public PictureResponseQuestion(int id, String content, int quizId, int type) {
        super(id, content, quizId, type);
    }

    @Override
    public questionType getType(){
        return questionType.Picture_Response_Question;
    }
    @Override
    public String getCreatingDescriptionHtml() {
        String res = "<div class=\"question-description\">" +
                "<label class=\"question-label\" for='pictureUrl'>Picture URL:</label><br> " +
                "<textarea id='pictureUrl' name='pictureUrl' rows='1' cols='30'></textarea><br><br>" +
                "</div>";;
        return res;
    }

}
