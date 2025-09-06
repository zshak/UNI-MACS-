import Quiz.Models.Answer;
import Quiz.Models.CorrectAnswers;
import Quiz.Models.MultipleChoiceAnswer;
import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

// tests for result class


public class AnswerTest {
    private Answer answers1 = new CorrectAnswers(1, "aaaa");
    private Answer answers2 = new CorrectAnswers(2, 1, "aaaa");
    private MultipleChoiceAnswer answers3 = new MultipleChoiceAnswer(2, "oe", true);
    private Answer answers4 = new MultipleChoiceAnswer(1, 2, "oe");

    // basic tests
    @Test
    public void testConstructors() {
        assertNotNull(answers1);
        assertNotNull(answers2);
        assertNotNull(answers3);
        assertNotNull(answers4);
    }

    @Test
    public void testGetMethods1() {
        assertEquals(1, answers1.getQuestionId());
        assertEquals("aaaa", answers1.getContent());
        assertEquals(2, answers2.getId());
        assertTrue(answers3.isTrue());
    }


    @Test
    public void testToString() {
        assertEquals("Answer{" +
                "id=" + 2 +
                ", questionId=" + 1 +
                ", content='" + "aaaa" + '\'' +
                '}', answers2.toString());

        assertEquals("Answer{" +
                "id=" + answers3.getId() +
                ", questionId=" + answers3.getQuestionId() +
                ", content='" + answers3.getContent() +
                ", isTrue=" + answers3.isTrue() + '\'' +
                '}', answers3.toString());
    }

    @Test
    public void testSetGet1() {
        answers1.setContent("c");
        answers1.setId(1);
        answers1.setQuestionId(3);
        answers3.setTrue(false);
        assertEquals(answers1.getId(), 1);
        assertEquals(answers1.getQuestionId(), 3);
        assertFalse(answers3.isTrue());
        assertEquals(answers1.getContent(), "c");
    }

    @Test
    public void testCompare() {
        Answer t2 = new CorrectAnswers(3,1,"Aaa");
        assertTrue(t2.compareTo(answers2) > 0);
    }
}
