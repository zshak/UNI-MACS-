package QuizPage.Models;

import java.sql.Timestamp;

public class Result {
    private int id;
    private int userId;
    private int quizId;
    private int score;
    private int percent;
    private int duration;
    private Timestamp finishTime;

    public Result(int id, int userId, int quizId, int score, int percent, int duration, Timestamp finishTime){
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.percent = percent;
        this.duration = duration;
        this.finishTime = finishTime;
    }

    public Result(int userId, int quizId, int score, int percent, int duration, Timestamp finishTime){
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.percent = percent;
        this.duration = duration;
        this.finishTime = finishTime;
    }

    public  int getId(){ return id; }
    public int getUserId() { return userId; }

    public int getQuizId() { return quizId; }

    public int getScore() { return score; }

    public int getPercent() { return percent; }

    public int getDuration() { return duration; }

    public Timestamp getFinishTime() { return finishTime; }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public void setPercent(int percent) {
        this.percent = percent;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", score=" + score +
                ", percent=" + percent +
                ", duration=" + duration +
                ", finishTime=" + finishTime +
                '}';
    }
}
