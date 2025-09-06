package Mails;
import java.util.Date;

public class Note extends Mail {
    private String title;
    private String content;

    public Note(int mailId, int fromId, int toId, String title, String content, Date sentDate) {
        super(mailId, fromId, toId, sentDate);
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getType() {
        return "NOTE";
    }
}