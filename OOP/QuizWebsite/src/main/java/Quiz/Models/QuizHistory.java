package Quiz.Models;

import java.sql.Timestamp;

public class QuizHistory {
    private int quizId;
    private String username;
    private int resultId;
    private int score;
    private double percent;
    private int quizDuration;
    private Timestamp finishTime;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getPercent() {
        return percent;
    }


    public int getQuizDuration() {
        return quizDuration;
    }


    public Timestamp getFinishTime() {
        return finishTime;
    }


    public QuizHistory(int quizId, String username, int resultId, int score, double percent, int quizDuration, Timestamp finishTime) {
        this.quizId = quizId;
        this.username = username;
        this.resultId = resultId;
        this.score = score;
        this.percent = percent;
        this.quizDuration = quizDuration;
        this.finishTime = finishTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
