import Quiz.Models.*;
import Quiz.Models.enums.questionType;
import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class QuestionResponseQuestionTest {
    @Test
    public void testConstructor1() {
        QuestionResponseQuestion q = new QuestionResponseQuestion();
        assertEquals(QuestionResponseQuestion.class, q.getClass());
        assertNotNull(q);
    }

    @Test
    public void testConstructor2() {
        QuestionResponseQuestion q = new QuestionResponseQuestion(1);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
        assertNotNull(q);
    }

    @Test
    public void testConstructor3() {
        QuestionResponseQuestion q = new QuestionResponseQuestion(1);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
        assertNotNull(q);
    }

    @Test
    public void testConstructor4() {
        QuestionResponseQuestion q = new QuestionResponseQuestion(1, "none", 1, 1);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
        assertNotNull(q);
    }

    @Test
    public void testConstructor5() {
        QuestionResponseQuestion q = new QuestionResponseQuestion(1);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
        assertNotNull(q);
    }

    @Test
    public void testGetType1() {
        QuestionResponseQuestion q = new QuestionResponseQuestion(1);
        assertEquals(QuestionResponseQuestion.class, q.getClass());
        assertNotNull(q);
        assertEquals(questionType.Question_Response, q.getType());
    }

}
