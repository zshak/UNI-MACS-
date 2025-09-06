package DB;

import Database.DatabaseManager;
import Quiz.Models.Quiz;
import QuizPage.Models.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizStuffTests {
    @BeforeEach
    public void setUp() {
        DatabaseManager.runDatabaseCreationScript("tank_database_test");
    }


    Timestamp time = new Timestamp(System.currentTimeMillis());

    @Test
    public void testAddQuiz() throws SQLException {
        Quiz q = new Quiz(1, "test1", "none", 1, true, true, true, true, time, 0);
        Quiz res = DatabaseManager.AddQuiz(q);
        assertEquals(q.getId(), res.getId());
        assertEquals(q.getName(), res.getName());
        assertEquals(q.getDescription(), res.getDescription());
        assertEquals(q.getCreatorId(), res.getCreatorId());
        assertEquals(q.hasRandomQuestions(), res.hasRandomQuestions());
        assertEquals(q.isOnePage(), res.isOnePage());
        assertEquals(q.isImmediateCorrection(), res.isImmediateCorrection());
        assertEquals(q.hasPracticeMode(), res.hasPracticeMode());
        assertEquals(q.getSubmissionCount(), res.getSubmissionCount());
    }

    @Test
    public void testGetResultById() throws SQLException {
        Result res = new Result(1, 1, 1, 1, 50, 2, time);
        DatabaseManager.insertQuizResult(res);
        Result ans = DatabaseManager.GetResultById(1);
        assertEquals(res.getQuizId(), ans.getQuizId());
        assertEquals(res.getId(), ans.getId());
        assertEquals(res.getScore(), ans.getScore());
        assertEquals(res.getId(), ans.getId());
        assertEquals(res.getPercent(), ans.getPercent());
        assertEquals(res.getDuration(), ans.getDuration());
    }

    @Test
    public void testgetUsersPastPerformances() throws SQLException {
        Result res = new Result(1, 1, 1, 1, 50, 2, time);
        DatabaseManager.insertQuizResult(res);
        Result res1 = new Result(2, 1, 1, 2, 100, 4, time);
        DatabaseManager.insertQuizResult(res1);
        List<Result> performeances = DatabaseManager.getUsersPastPerformances(1, 1);
        assertEquals(res1.getQuizId(), performeances.get(1).getQuizId());
        assertEquals(res1.getId(), performeances.get(1).getId());
        assertEquals(res1.getScore(), performeances.get(1).getScore());
        assertEquals(res1.getId(), performeances.get(1).getId());
        assertEquals(res1.getPercent(), performeances.get(1).getPercent());
        assertEquals(res1.getDuration(), performeances.get(1).getDuration());
        res = new Result(1, 1, 1, 1, 50, 2, time);
        assertEquals(res.getQuizId(), performeances.get(0).getQuizId());
        assertEquals(res.getId(), performeances.get(0).getId());
        assertEquals(res.getScore(), performeances.get(0).getScore());
        assertEquals(res.getId(), performeances.get(0).getId());
        assertEquals(res.getPercent(), performeances.get(0).getPercent());
        assertEquals(res.getDuration(), performeances.get(0).getDuration());
    }

    @Test
    public void testgetHighestPerformers() throws SQLException {
        Result res = new Result(1, 1, 1, 2, 100, 4, time);
        DatabaseManager.insertQuizResult(res);
        Result res1 = new Result(2, 1, 1, 1, 50, 2, time);
        DatabaseManager.insertQuizResult(res1);
        List<Result> performeances = DatabaseManager.getHighestPerformers( 1);
        assertEquals(res1.getQuizId(), performeances.get(1).getQuizId());
        assertEquals(res1.getId(), performeances.get(1).getId());
        assertEquals(res1.getScore(), performeances.get(1).getScore());
        assertEquals(res1.getId(), performeances.get(1).getId());
        assertEquals(res1.getPercent(), performeances.get(1).getPercent());
        assertEquals(res1.getDuration(), performeances.get(1).getDuration());
        res = new Result(1, 1, 1, 1, 50, 2, time);
        assertEquals(res.getQuizId(), performeances.get(0).getQuizId());
        assertEquals(res.getId(), performeances.get(0).getId());
        assertEquals(res.getId(), performeances.get(0).getId());
    }

    @Test
    public void testgetHighestPerformersLastDay() throws SQLException {
        Result res = new Result(1, 1, 1, 2, 100, 4, time);
        DatabaseManager.insertQuizResult(res);
        Result res1 = new Result(2, 1, 1, 1, 50, 2, time);
        DatabaseManager.insertQuizResult(res1);
        List<Result> performeances = DatabaseManager.getHighestPerformersLastDay( 1);
        assertEquals(res1.getQuizId(), performeances.get(1).getQuizId());
        assertEquals(res1.getId(), performeances.get(1).getId());
        assertEquals(res1.getScore(), performeances.get(1).getScore());
        assertEquals(res1.getId(), performeances.get(1).getId());
        assertEquals(res1.getPercent(), performeances.get(1).getPercent());
        assertEquals(res1.getDuration(), performeances.get(1).getDuration());
        res = new Result(1, 1, 1, 1, 50, 2, time);
        assertEquals(res.getQuizId(), performeances.get(0).getQuizId());
        assertEquals(res.getId(), performeances.get(0).getId());
        assertEquals(res.getId(), performeances.get(0).getId());
    }

//    @Test
//    public void testgetRecentResults() throws SQLException {
//        Result res = new Result(1, 1, 1, 2, 100, 4, time);
//        DatabaseManager.insertQuizResult(res);
//        Result res1 = new Result(2, 1, 1, 1, 50, 2, time);
//        DatabaseManager.insertQuizResult(res1);
//        List<Result> performeances = DatabaseManager.getRecentResults( 1);
//        assertEquals(null, performeances);
//        assertEquals(res1.getQuizId(), performeances.get(1).getQuizId());
//        assertEquals(res1.getId(), performeances.get(1).getId());
//        assertEquals(res1.getScore(), performeances.get(1).getScore());
//        assertEquals(res1.getId(), performeances.get(1).getId());
//        assertEquals(res1.getPercent(), performeances.get(1).getPercent());
//        assertEquals(res1.getDuration(), performeances.get(1).getDuration());
//        res = new Result(1, 1, 1, 1, 50, 2, time);
//        assertEquals(res.getQuizId(), performeances.get(0).getQuizId());
//        assertEquals(res.getId(), performeances.get(0).getId());
//        assertEquals(res.getId(), performeances.get(0).getId());
//    }

    @Test
    public void testgetQuizAvarage() throws SQLException {
        Result res = new Result(1, 1, 1, 2, 100, 4, time);
        DatabaseManager.insertQuizResult(res);
        Result res1 = new Result(2, 1, 1, 1, 50, 2, time);
        DatabaseManager.insertQuizResult(res1);
        int performeances = (int) DatabaseManager.getQuizAvarage(1);
        assertEquals(75, performeances);
    }
}

