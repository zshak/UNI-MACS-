import Quiz.Models.*;
import Quiz.Models.enums.questionType;
import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class QuizHistoryTest {

    Timestamp time = new Timestamp(System.currentTimeMillis());
    @Test
    public void testConstructor1() {
        QuizHistory q = new QuizHistory(1, "user", 1, 1, 10.0, 3, time);
        assertNotNull(q);
    }

    @Test
    public void testSetter1() {
        QuizHistory q = new QuizHistory(1, "user", 1, 1, 10.0, 3, time);
        q.setQuizId(2);
        q.setScore(3);
        q.setUsername("a");
        assertEquals(2,  q.getQuizId());
        assertEquals(3, q.getScore());
        assertEquals("a",q.getUsername());
    }

    @Test
    public void testGetter1() {
        QuizHistory q = new QuizHistory(1, "user", 1, 1, 10.0, 3, time);
        assertEquals(1, q.getQuizId());
        assertEquals(1, q.getScore());
        assertEquals(10.0, q.getPercent());
        assertEquals(3, q.getQuizDuration());
        assertEquals(time, q.getFinishTime());
        assertEquals("user", q.getUsername());

    }
}
