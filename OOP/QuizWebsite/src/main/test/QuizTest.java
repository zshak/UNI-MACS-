import Quiz.Models.*;
import Quiz.Models.enums.questionType;
import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class QuizTest {

    Timestamp time = new Timestamp(System.currentTimeMillis());

    @Test
    public void testConstructor1() {
        Quiz q = new Quiz(1, "test1", "none", 1, true, true, true, true, time, 0);
        assertNotNull(q);
    }

    @Test
    public void testConstructor2() {
        Quiz q = new Quiz("test1", "none", 1, true, true, true, true, time, 0);
        assertNotNull(q);
    }

    @Test
    public void testGetter1() {
        Quiz q = new Quiz(1,"test1", "none", 1, true, true, true, true, time, 0);
        q.setId(2);
        q.setName("a");
        q.setDescription("b");

        assertEquals(2, q.getId());
        assertEquals("a", q.getName());
        assertEquals("b", q.getDescription());
    }

    @Test
    public void testSetter1() {
        Quiz q = new Quiz(1,"test1", "none", 1, true, true, true, true, time, 0);
        assertEquals(1, q.getId());
        assertEquals("test1", q.getName());
        assertEquals(1, q.getCreatorId());
        assertEquals(true, q.hasRandomQuestions());
        assertEquals(true, q.isOnePage());
        assertEquals(true, q.isImmediateCorrection());
        assertEquals(true, q.hasPracticeMode());
        assertEquals(time, q.getCreationDateTime());
        assertEquals(0, q.getSubmissionCount());
    }

    @Test
    public void testToString() {
        Quiz q = new Quiz(1,"test1", "none", 1, true, true, true, true, time, 0);
        assertTrue(("Quiz{id=1, name='test1', description='none', creatorId=1, hasRandomQuestions=true, isOnePage=true, isImmediateCorrection=true, hasPracticeMode=true, creationDateTime=" + time + ", submissionCount=0}").equals(q.toString()));
    }
}
