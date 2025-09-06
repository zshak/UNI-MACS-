package Quiz.Models;

public  class QuestionFactory {
    public static Question createQuestion(String type){
        if ("question-response".equals(type)) {
            return new QuestionResponseQuestion();
        } else if ("fill-in-the-blank".equals(type)) {
            return new FillInTheBlankQuestion();
        } else if ("picture-response".equals(type)) {
            return new PictureResponseQuestion();
        } else if ("multiple-choice".equals(type)) {
            return new MultipleChoiceQuestion();
        }
        return null;
    }

//    public static Question createQuestionById(int type, int quizId, String content, int id){
//        if (type == 1) {
//            return new MultipleAnswerQuestion(quizId);
//        } else if (type == 2) {
//            return new MultipleAnswerQuestion(quizId);
//        } else if (type == 4) {
//            return new PictureResponseQuestion(quizId);
//        } else if (type == 3) {
//            return new SingleAnswerQuestion(id, content, quizId, type);
//        }
//        return null;
//    }

    public static Question createQuestionById(int type, int quizId, String content, int id){
        if (type == 0) {
            return new QuestionResponseQuestion(id, content, quizId, type);
        } else if (type == 1) {
            return new FillInTheBlankQuestion(id, content, quizId, type);
        } else if (type == 2) {
            return new MultipleChoiceQuestion(id, content, quizId, type);
        } else if (type == 3) {
            return new PictureResponseQuestion(id, content, quizId, type);
        }
        return null;
    }

}
