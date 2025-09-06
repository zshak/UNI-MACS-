package Quiz.Models;

import java.sql.Timestamp;

public class Quiz {
    private int id;
    private String name;
    private String description;
    private final int creatorId;
    private boolean hasRandomQuestions;
    private boolean isOnePage;
    private boolean isImmediateCorrection;
    private boolean hasPracticeMode;
    private Timestamp creationDateTime;
    private int submissionCount;

    public Quiz(int id, String name, String description, int creatorId, boolean hasRandomQuestions, boolean isOnePage, boolean isImmediateCorrection, boolean hasPracticeMode, Timestamp creationDateTime, int submissionCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.hasRandomQuestions = hasRandomQuestions;
        this.isOnePage = isOnePage;
        this.isImmediateCorrection = isImmediateCorrection;
        this.hasPracticeMode = hasPracticeMode;
        this.creationDateTime = creationDateTime;
        this.submissionCount = submissionCount;
    }

    public Quiz(String name, String description, int creatorId, boolean hasRandomQuestions, boolean isOnePage, boolean isImmediateCorrection, boolean hasPracticeMode, Timestamp creationDateTime, int submissionCount) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.hasRandomQuestions = hasRandomQuestions;
        this.isOnePage = isOnePage;
        this.isImmediateCorrection = isImmediateCorrection;
        this.hasPracticeMode = hasPracticeMode;
        this.creationDateTime = creationDateTime;
        this.submissionCount = submissionCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public boolean hasRandomQuestions() {
        return hasRandomQuestions;
    }

    public boolean isOnePage() {
        return isOnePage;
    }

    public boolean isImmediateCorrection() {
        return isImmediateCorrection;
    }

    public boolean hasPracticeMode() {
        return hasPracticeMode;
    }

    public Timestamp getCreationDateTime() {
        return creationDateTime;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId=" + creatorId +
                ", hasRandomQuestions=" + hasRandomQuestions +
                ", isOnePage=" + isOnePage +
                ", isImmediateCorrection=" + isImmediateCorrection +
                ", hasPracticeMode=" + hasPracticeMode +
                ", creationDateTime=" + creationDateTime +
                ", submissionCount=" + submissionCount +
                '}';
    }
}
