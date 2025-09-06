import Quiz.Models.*;
import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
public class QuestionFactoryTest {
    @Test
    public void testcreateQuestion1() {
        Question q = QuestionFactory.createQuestion("question-response");
        assertEquals(QuestionResponseQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestion2() {
        Question q = QuestionFactory.createQuestion("fill-in-the-blank");
        assertEquals(FillInTheBlankQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestion3() {
        Question q = QuestionFactory.createQuestion("picture-response");
        assertEquals(PictureResponseQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestion4() {
        Question q = QuestionFactory.createQuestion("multiple-choice");
        assertEquals(MultipleChoiceQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestion5() {
        Question q = QuestionFactory.createQuestion("none");
        assertEquals(null, q);
    }

    private static int quizId = 1;
    private static String content = "no content";
    private static int id = 1;
    @Test
    public void testcreateQuestionById1() {
        Question q = QuestionFactory.createQuestionById(0, quizId, content, id);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestionById2() {
        Question q = QuestionFactory.createQuestionById(0, quizId, content, id);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestionById3() {
        Question q = QuestionFactory.createQuestionById(1, quizId, content, id);
        assertEquals(FillInTheBlankQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestionById4() {
        Question q = QuestionFactory.createQuestionById(2, quizId, content, id);
        assertEquals(MultipleChoiceQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestionById5() {
        Question q = QuestionFactory.createQuestionById(3, quizId, content, id);
        assertEquals(PictureResponseQuestion.class, q.getClass());
    }

    @Test
    public void testcreateQuestionById6() {
        Question q = QuestionFactory.createQuestionById(4, quizId, content, id);
        assertEquals(null, q);
    }


}
