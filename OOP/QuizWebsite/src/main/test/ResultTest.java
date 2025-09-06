import QuizPage.Models.Result;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

// tests for result class


public class ResultTest {

    private int id = 1;
    private int userId = 1;
    private int quizId = 1;
    private int score = 5;
    private int percent = 50;
    private int duratin = 5;
    Timestamp time = new Timestamp(System.currentTimeMillis());
    private Result res1 = new Result(id, userId, quizId, score, percent, duratin, time);
    private Result res2 = new Result(userId, quizId, score, percent, duratin, time);

    // basic tests
    @Test
    public void testConstructors() {
        assertNotNull(res1);
        assertNotNull(res2);
    }

    @Test
    public void testGetMethods1() {
        assertEquals(1, res1.getId());
        assertEquals(1, res1.getUserId());
        assertEquals(1, res1.getQuizId());
        assertEquals(5, res1.getScore());
        assertEquals(50, res1.getPercent());
        assertEquals(5, res1.getDuration());
        assertEquals(time, res1.getFinishTime());
    }

    @Test
    public void testGetMethods2() {
        assertEquals(0, res2.getId());
        assertEquals(1, res2.getUserId());
        assertEquals(1, res2.getQuizId());
        assertEquals(5, res2.getScore());
        assertEquals(50, res2.getPercent());
        assertEquals(5, res2.getDuration());
        assertEquals(time, res2.getFinishTime());

    }

    @Test
    public void testGetMethods3() {
        assertEquals(res1.getId(), 1);
        assertEquals(res2.getId(), 0);
        assertEquals(res1.getUserId(), res2.getUserId());
        assertEquals(res1.getQuizId(), res2.getQuizId());
        assertEquals(res1.getScore(), res2.getScore());
        assertEquals(res1.getPercent(), res2.getPercent());
        assertEquals(res1.getDuration(), res2.getDuration());
        assertEquals(res1.getFinishTime(), res2.getFinishTime());
    }

    @Test
    public void testToString() {
        assertTrue("Result{id=1, userId=1, quizId=1, score=5, percent=50, duration=5".equals(res1.toString().substring(0, 64)));
        assertTrue("Result{id=0, userId=1, quizId=1, score=5, percent=50, duration=5".equals(res2.toString().substring(0, 64)));
        assertTrue(res1.toString().substring(13, 64).equals(res1.toString().substring(13, 64)));
    }

    @Test
    public void testSetGet1() {
        assertEquals(1, res1.getId());
        res1.setId(2);
        assertEquals(2, res1.getId());

        assertEquals(0, res2.getId());
        res2.setId(1);
        assertEquals(1, res2.getId());
        res2.setId(2);
        assertEquals(2, res2.getId());
    }

    @Test
    public void testSetGet2() {
        assertEquals(1, res1.getUserId());
        res1.setUserId(2);
        assertEquals(2, res1.getUserId());
        assertEquals(1, res2.getUserId());
        res2.setUserId(2);
        assertEquals(2, res2.getUserId());
    }

    @Test
    public void testSetGet3() {
        assertEquals(1, res1.getQuizId());
        res1.setQuizId(2);
        assertEquals(2, res1.getQuizId());
        assertEquals(1, res2.getQuizId());
        res2.setQuizId(2);
        assertEquals(2, res2.getQuizId());
    }

    @Test
    public void testSetGet4() {
        assertEquals(5, res1.getScore());
        res1.setScore(2);
        assertEquals(2, res1.getScore());
        assertEquals(5, res2.getScore());
        res2.setScore(2);
        assertEquals(2, res2.getScore());
    }

    @Test
    public void testSetGet5() {
        assertEquals(50, res1.getPercent());
        res1.setPercent(25);
        assertEquals(25, res1.getPercent());
        assertEquals(50, res2.getPercent());
        res2.setPercent(25);
        assertEquals(25, res2.getPercent());
    }

    @Test
    public void testSetGet6() {
        assertEquals(5, res1.getDuration());
        res1.setDuration(2);
        assertEquals(2, res1.getDuration());
        assertEquals(5, res2.getDuration());
        res2.setDuration(2);
        assertEquals(2, res2.getDuration());
    }

    @Test
    public void testSetGet7() {
        Timestamp time2 = new Timestamp(System.currentTimeMillis());
        assertEquals(time, res1.getFinishTime());
        res1.setFinishTime(time2);
        assertEquals(time2, res1.getFinishTime());
        assertEquals(time, res2.getFinishTime());
        res2.setFinishTime(time2);
        assertEquals(time2, res2.getFinishTime());
    }

}
