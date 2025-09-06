package Quiz.Models;

import Quiz.Models.enums.questionType;
import jakarta.servlet.http.HttpServletRequest;

public class QuestionDescriptionFactory {
    public static String getDescription(questionType type, HttpServletRequest request){
        if(type == questionType.Fill_In_The_Blank || type == questionType.Question_Response || type == questionType.Multiple_Choice){
            return request.getParameter("questionDescription");
        } else if (type == questionType.Picture_Response_Question) {
            return request.getParameter("pictureUrl");
        }
        return null;
    }
}
